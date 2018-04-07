package lt.rieske.payments.consumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class GetAllPaymentsContract extends PaymentsContract{

    @Pact(consumer = CONSUMER)
    public RequestResponsePact getAllPaymentsWhenNoneExistContract(PactDslWithProvider builder) {
        return builder
          .uponReceiving("get all payments when none exist")
          .path(PAYMENTS_BASE_PATH)
          .matchHeader("Accept", "application/json")
          .method(HttpMethod.GET)
          .willRespondWith()
          .status(200)
          .body("[]")
          .matchHeader("Content-Type", "application/json;charset=UTF-8")
          .toPact();
    }

    @Test
    @PactVerification(fragment = "getAllPaymentsWhenNoneExistContract")
    public void returnsEmptyListWhenNoPaymentsExist() {
        Response response = paymentsApi().request(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(String.class)).isEqualTo("[]");
    }
}
