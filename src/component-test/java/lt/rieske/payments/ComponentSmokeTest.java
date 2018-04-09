package lt.rieske.payments;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import io.restassured.RestAssured;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.notNullValue;

public class ComponentSmokeTest {

    private static final String DATABASE_SERVICE = "db";
    private static final String PAYMENTS_API = "payments-api";

    private static final int SERVICE_PORT = 8080;
    private static final String SERVICE_URI_FORMAT = "http://$HOST:$EXTERNAL_PORT";

    @ClassRule
    public static DockerComposeRule docker = DockerComposeRule.builder()
      .file("docker-compose.yml")
      .waitingForService(DATABASE_SERVICE, HealthChecks.toHaveAllPortsOpen())
      .waitingForService(PAYMENTS_API, HealthChecks.toHaveAllPortsOpen())
      .waitingForService(PAYMENTS_API, HealthChecks
        .toRespond2xxOverHttp(SERVICE_PORT, (port) -> port.inFormat("http://$HOST:$EXTERNAL_PORT/ping")))
      .build();

    @Before
    public void setUp() {
        RestAssured.baseURI = docker.containers().container(PAYMENTS_API).port(SERVICE_PORT)
          .inFormat(SERVICE_URI_FORMAT);
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void serviceHasStartedAndIsHealthy() {
        // @formatter:off
        when()
          .get("/healthcheck")
        .then()
          .statusCode(200)
          .body("status", equalTo("UP"))
          .body("details.db.status", equalTo("UP"))
          .body("details.db.details.database", equalTo("PostgreSQL"));
        // @formatter:on
    }

    @Test
    public void canSaveAndRetrievePayments() throws IOException {
        assertNoPaymentsExist();

        String paymentUri = createPayment();

        assertCanFetchPayment(paymentUri);

        assertSinglePaymentExists();

        deletePayment(paymentUri);

        assertPaymentDoesNotExist(paymentUri);

        assertNoPaymentsExist();
    }

    private void assertNoPaymentsExist() {
        // @formatter:off
        when()
          .get("/api/v1/payments")
        .then()
          .log().all()
          .statusCode(200)
          .body("_embedded.payments", empty());
        // @formatter:on
    }

    private void assertSinglePaymentExists() {
        // @formatter:off
        when()
          .get("/api/v1/payments")
        .then()
          .log().all()
          .statusCode(200)
          .body("_embedded.payments", hasSize(1));
        // @formatter:on
    }

    private String createPayment() throws IOException {
        // @formatter:off
        return
        given()
          .body(fileBody("/payment.json"))
          .contentType("application/json")
        .when()
          .post("/api/v1/payments")
        .then()
          .log().all()
          .statusCode(201)
          .extract().header("Location");
        // @formatter:on
    }

    private void assertCanFetchPayment(String paymentUri) {
        // @formatter:off
        when()
          .get(paymentUri)
        .then()
          .log().all()
          .statusCode(200)
          .body("id", notNullValue());
        // @formatter:on
    }

    private void deletePayment(String paymentUri) {
        // @formatter:off
        when()
          .delete(paymentUri)
        .then()
          .log().all()
          .statusCode(204)
          .body(isEmptyOrNullString());
        // @formatter:on
    }

    private void assertPaymentDoesNotExist(String paymentUri) {
        // @formatter:off
        when()
          .get(paymentUri)
        .then()
          .log().all()
          .statusCode(404)
          .body(isEmptyOrNullString());
        // @formatter:on
    }

    private String fileBody(String path) throws IOException {
        return IOUtils.resourceToString(path, Charsets.UTF_8);
    }
}
