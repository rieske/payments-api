package lt.rieske.payments.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentsApiAppTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void pingEndpointIsExposed() {
        ResponseEntity<String> response = restTemplate.getForEntity("/ping", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("pong");
    }

    @Test
    public void healthcheckEndpointIsExposed() {
        ResponseEntity<String> response = restTemplate.getForEntity("/healthcheck", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).startsWith("{\"status\":\"UP\"");
    }

    @Test
    public void swaggerApiIsExposed() {
        ResponseEntity<String> response = restTemplate.getForEntity("/v2/api-docs", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).startsWith("{\"swagger\":\"2.0\"");
    }

    @Test
    public void swaggerUiIsExposed() {
        ResponseEntity<String> response = restTemplate.getForEntity("/swagger-ui.html", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
