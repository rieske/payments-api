package lt.rieske.payments.consumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class GetSinglePaymentContract extends PaymentsContract {

    private final String nonExistingPaymentId = "some-non-existing-payment-id";
    private final String existingPaymentId = "existing-payment-id";

    @Pact(consumer = CONSUMER)
    public RequestResponsePact getSingleNonExistingPaymentContract(PactDslWithProvider builder) {
        return builder
          .uponReceiving("get single non existing payment")
          .path(PAYMENTS_BASE_PATH + nonExistingPaymentId)
          .matchHeader("Accept", "application/json")
          .method("GET")
          .willRespondWith()
          .status(404)
          .toPact();
    }

    @Test
    @PactVerification(fragment = "getSingleNonExistingPaymentContract")
    public void returnsNotFoundWhenRequestedPaymentDoesNotExist() {
        Response response = paymentsApi()
          .path(nonExistingPaymentId)
          .request(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.readEntity(String.class)).isEmpty();
    }

    @Pact(consumer = CONSUMER)
    public RequestResponsePact getSinglePaymentContract(PactDslWithProvider builder) {
        return builder
          .given("payment exists", "paymentId", existingPaymentId)
          .uponReceiving("get single payment")
          .path(PAYMENTS_BASE_PATH + existingPaymentId)
          .matchHeader("Accept", "application/json")
          .method("GET")
          .willRespondWith()
          .status(200)
          .matchHeader("Content-Type", "application/json;charset=UTF-8")
          .body(paymentSchema(existingPaymentId))
          .toPact();
    }

    @Test
    @PactVerification(fragment = "getSinglePaymentContract")
    public void returnsAnExistingPayment() {
        Response response = paymentsApi()
          .path(existingPaymentId)
          .request(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(200);
    }
}
