package lt.rieske.payments.consumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentsContract {

    private static final String CONSUMER = "some-external-consumer";

    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("payments-api", this);

    @Pact(consumer = CONSUMER)
    public RequestResponsePact getAllPaymentsWhenNoneExistContract(PactDslWithProvider builder) {
        return builder
          .uponReceiving("get all payments when none exist")
          .path("/api/v1/payments")
          .matchHeader("Accept", "application/json")
          .method("GET")
          .willRespondWith()
          .status(200)
          .body("[]")
          .matchHeader("Content-Type", "application/json;charset=UTF-8")
          .toPact();
    }

    @Test
    @PactVerification(fragment = "getAllPaymentsWhenNoneExistContract")
    public void getAllPaymentsWhenNoneExist() {
        Response response = paymentsApi().path("/payments").request(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(String.class)).isEqualTo("[]");
    }

    private WebTarget paymentsApi() {
        return ClientBuilder.newClient()
          .target(URI.create("http://localhost:" + mockProvider.getPort()))
          .path("/api/v1/");
    }
}
