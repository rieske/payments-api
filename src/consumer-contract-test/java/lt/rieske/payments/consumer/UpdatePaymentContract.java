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

public class UpdatePaymentContract extends PaymentsContract {

    @Pact(consumer = CONSUMER)
    public RequestResponsePact updateExistingPaymentContract(PactDslWithProvider builder) {
        return builder
                .given("payment exists", "paymentId", PAYMENT_ID)
                .uponReceiving("update a payment given one with same id already exists")
                .path(PAYMENTS_BASE_PATH + PAYMENT_ID)
                .matchHeader("Content-Type", "application/json")
                .method(HttpMethod.PATCH)
                .body(paymentSchema(PAYMENT_ID))
                .willRespondWith()
                .status(204)
                .toPact();
    }

    @Test
    @PactVerification(fragment = "updateExistingPaymentContract")
    public void updatesExistingPayment() {

        Response response = paymentsApi().path(PAYMENT_ID)
                .request().method(HttpMethod.PATCH, Entity.json(TRANSACTION_JSON));

        assertThat(response.getStatus()).isEqualTo(204);
        assertThat(response.readEntity(String.class)).isEmpty();
    }

    @Pact(consumer = CONSUMER)
    public RequestResponsePact notFoundWhenUpdatingNonExistingPaymentContract(PactDslWithProvider builder) {
        return builder
                .uponReceiving("update a payment given one does not exist")
                .path(PAYMENTS_BASE_PATH + PAYMENT_ID)
                .matchHeader("Content-Type", "application/json")
                .method(HttpMethod.PATCH)
                .body(paymentSchema(PAYMENT_ID))
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Test
    @PactVerification(fragment = "notFoundWhenUpdatingNonExistingPaymentContract")
    public void notFoundWhenUpdatingNonExistingPayment() {

        Response response = paymentsApi().path(PAYMENT_ID)
                .request().method(HttpMethod.PATCH, Entity.json(TRANSACTION_JSON));

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.readEntity(String.class)).isEmpty();
    }

}
