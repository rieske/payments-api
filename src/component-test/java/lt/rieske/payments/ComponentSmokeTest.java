package lt.rieske.payments;

import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

public class ComponentSmokeTest {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentSmokeTest.class);

    private static final String SERVICE_CONTAINER = "payments-api_1";
    private static final int SERVICE_PORT = 8080;

    private static DockerComposeContainer environment =
            new DockerComposeContainer(Paths.get("docker-compose.yml").toFile())
                    .withLocalCompose(true)
                    .withLogConsumer(SERVICE_CONTAINER, new Slf4jLogConsumer(LOG).withPrefix(SERVICE_CONTAINER))
                    .withExposedService(SERVICE_CONTAINER, SERVICE_PORT, Wait.forListeningPort())
                    .withExposedService(SERVICE_CONTAINER, SERVICE_PORT, Wait.forHttp("/actuator/health").forStatusCode(200));

    @BeforeClass
    public static void setUp() {
        environment.start();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = serviceUrl();
    }

    @AfterClass
    public static void teardown() {
        environment.stop();
    }

    private static String serviceUrl() {
        return String.format("http://%s:%d",
                environment.getServiceHost(SERVICE_CONTAINER, SERVICE_PORT),
                environment.getServicePort(SERVICE_CONTAINER, SERVICE_PORT));
    }

    @Test
    public void serviceHasStartedAndIsHealthy() {
        // @formatter:off
        when()
          .get("/actuator/health")
        .then()
          .statusCode(200)
          .body("status", equalTo("UP"))
          .body("components.db.status", equalTo("UP"))
          .body("components.db.details.database", equalTo("PostgreSQL"));
        // @formatter:on
    }

    @Test
    public void canSaveUpdateAndRetrievePayments() throws IOException {
        assertNoPaymentsExist();

        String paymentUri = createPayment();

        assertCanFetchPaymentAndAmountIs(paymentUri, "100.21");

        assertSinglePaymentWithAmountExists();

        assertCanNotUpdateAmountToNegative(paymentUri);

        assertCanFetchPaymentAndAmountIs(paymentUri, "100.21");

        assertCanUpdateAmount(paymentUri, "42.00");

        assertCanFetchPaymentAndAmountIs(paymentUri, "42.00");

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

    private void assertSinglePaymentWithAmountExists() {
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

    private void assertCanUpdateAmount(String paymentUri, String amount) {
        // @formatter:off
        given()
          .body("{\"attributes\": { \"amount\": \"" + amount + "\"}}")
          .contentType("application/json")
          .accept("")
        .when()
          .patch(paymentUri)
        .then()
          .log().all()
          .statusCode(204);
        // @formatter:on
    }

    private void assertCanNotUpdateAmountToNegative(String paymentUri) {
        // @formatter:off
        given()
          .body("{\"attributes\": { \"amount\": \"-42.00\"}}")
          .contentType("application/json")
          .accept("")
        .when()
          .patch(paymentUri)
        .then()
          .log().all()
          .statusCode(400)
          .body("errors", hasSize(1));
        // @formatter:on
    }

    private void assertCanFetchPaymentAndAmountIs(String paymentUri, String amount) {
        // @formatter:off
        when()
          .get(paymentUri)
        .then()
          .log().all()
          .statusCode(200)
          .body("id", notNullValue())
          .body("attributes.amount", equalTo(amount));
        // @formatter:on
    }

    private void deletePayment(String paymentUri) {
        // @formatter:off
        when()
          .delete(paymentUri)
        .then()
          .log().all()
          .statusCode(204)
          .body(emptyOrNullString());
        // @formatter:on
    }

    private void assertPaymentDoesNotExist(String paymentUri) {
        // @formatter:off
        when()
          .get(paymentUri)
        .then()
          .log().all()
          .statusCode(404)
          .body(emptyOrNullString());
        // @formatter:on
    }

    private String fileBody(String path) throws IOException {
        return Files.readString(Paths.get("src/component-test/resources/" + path));
    }
}
