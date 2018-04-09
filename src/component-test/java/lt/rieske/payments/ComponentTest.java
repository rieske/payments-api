package lt.rieske.payments;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import org.junit.ClassRule;
import org.junit.Test;

public class ComponentTest {

    private static final String DATABASE_SERVICE = "db";
    private static final String PAYMENTS_API = "payments-api";

    @ClassRule
    public static DockerComposeRule docker = DockerComposeRule.builder()
      .file("docker-compose.yml")
      .waitingForService(DATABASE_SERVICE, HealthChecks.toHaveAllPortsOpen())
      .waitingForService(PAYMENTS_API, HealthChecks.toHaveAllPortsOpen())
      .waitingForService(PAYMENTS_API, HealthChecks.toRespond2xxOverHttp(8080, (port) -> port.inFormat("http://$HOST:$EXTERNAL_PORT/ping")))
      .build();

    @Test
    public void serviceHasStartedAndIsHealthy() {

    }
}
