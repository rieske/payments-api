package lt.rieske.payments.consumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class GetSinglePaymentContract extends PaymentsContract {

    @Pact(consumer = CONSUMER)
    public RequestResponsePact getSingleNonExistingPaymentContract(PactDslWithProvider builder) {
        return builder
          .uponReceiving("get single non existing payment")
          .path(PAYMENTS_BASE_PATH + PAYMENT_ID)
          .matchHeader("Accept", "application/json")
          .method(HttpMethod.GET)
          .willRespondWith()
          .status(404)
          .toPact();
    }

    @Test
    @PactVerification(fragment = "getSingleNonExistingPaymentContract")
    public void returnsNotFoundWhenRequestedPaymentDoesNotExist() {
        Response response = paymentsApi()
          .path(PAYMENT_ID)
          .request(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.readEntity(String.class)).isEmpty();
    }

    @Pact(consumer = CONSUMER)
    public RequestResponsePact getSinglePaymentContract(PactDslWithProvider builder) {
        return builder
          .given("payment exists", "paymentId", PAYMENT_ID)
          .uponReceiving("get single payment")
          .path(PAYMENTS_BASE_PATH + PAYMENT_ID)
          .matchHeader("Accept", "application/json")
          .method(HttpMethod.GET)
          .willRespondWith()
          .status(200)
          .matchHeader("Content-Type", "application/json;charset=UTF-8")
          .body(paymentSchema(PAYMENT_ID))
          .toPact();
    }

    @Test
    @PactVerification(fragment = "getSinglePaymentContract")
    public void returnsAnExistingPayment() {
        Response response = paymentsApi()
          .path(PAYMENT_ID)
          .request(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(200);
    }
}
