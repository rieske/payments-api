package lt.rieske.payments.consumer;


import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class CreatePaymentContract extends PaymentsContract {

    @Pact(consumer = CONSUMER)
    public RequestResponsePact createNewPaymentContract(PactDslWithProvider builder) {
        return builder
          .uponReceiving("create new payment")
          .path(PAYMENTS_BASE_PATH)
          .matchHeader("Content-Type", "application/json")
          .method(HttpMethod.POST)
          .body(paymentSchema(PAYMENT_ID))
          .willRespondWith()
          .status(201)
          .matchHeader("Location", ".*" + PAYMENTS_BASE_PATH + ".*", PAYMENTS_BASE_PATH + PAYMENT_ID)
          .toPact();
    }

    @Test
    @PactVerification(fragment = "createNewPaymentContract")
    public void createsNewPayment() {

        Response response = paymentsApi().request()
          .post(Entity.json(TRANSACTION_JSON));

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getHeaderString("Location")).isEqualTo(PAYMENTS_BASE_PATH + PAYMENT_ID);
    }
}
