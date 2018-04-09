package lt.rieske.payments;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class ComponentTest {

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
}
