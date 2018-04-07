package lt.rieske.payments.consumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.UUID;

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
                .matchHeader("Location", PAYMENTS_BASE_PATH + ".*", PAYMENTS_BASE_PATH + PAYMENT_ID)
                .toPact();
    }

    @Test
    @PactVerification(fragment = "updateExistingPaymentContract")
    public void updatesExistingPayment() {

        Response response = paymentsApi().path(PAYMENT_ID)
                .request().method(HttpMethod.PATCH, Entity.json(TRANSACTION_JSON));

        assertThat(response.getStatus()).isEqualTo(204);
        assertThat(response.readEntity(String.class)).isEmpty();
        assertThat(response.getHeaderString("Location")).isEqualTo(PAYMENTS_BASE_PATH + PAYMENT_ID);
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


    private final String differentExistingPaymentId = UUID.randomUUID().toString();

    @Pact(consumer = CONSUMER)
    public RequestResponsePact badRequestWhenUpdatingPaymentIdContract(PactDslWithProvider builder) {
        return builder
                .given("payment exists", "paymentId", differentExistingPaymentId)
                .uponReceiving("update a payment with a changed id in request")
                .path(PAYMENTS_BASE_PATH + differentExistingPaymentId)
                .matchHeader("Content-Type", "application/json")
                .method(HttpMethod.PATCH)
                .body(paymentSchema(PAYMENT_ID))
                .willRespondWith()
                .status(400)
                .toPact();
    }

    @Test
    @PactVerification(fragment = "badRequestWhenUpdatingPaymentIdContract")
    public void badRequestWhenUpdatingPaymentId() {

        Response response = paymentsApi().path(differentExistingPaymentId)
                .request().method(HttpMethod.PATCH, Entity.json(TRANSACTION_JSON));

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.readEntity(String.class)).isEmpty();
    }
}
