package lt.rieske.payments.consumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class DeletePaymentContract extends PaymentsContract {

    @Pact(consumer = CONSUMER)
    public RequestResponsePact deletePaymentContract(PactDslWithProvider builder) {
        return builder
                .given("payment exists", "paymentId", PAYMENT_ID)
                .uponReceiving("delete payment")
                .path(PAYMENTS_BASE_PATH + PAYMENT_ID)
                .method(HttpMethod.DELETE)
                .willRespondWith()
                .status(204)
                .toPact();
    }

    @Test
    @PactVerification(fragment = "deletePaymentContract")
    public void deletesExistingPayment() {

        Response response = paymentsApi().path(PAYMENT_ID)
                .request().delete();

        assertThat(response.getStatus()).isEqualTo(204);
    }

    @Pact(consumer = CONSUMER)
    public RequestResponsePact deleteNonExistingPaymentContract(PactDslWithProvider builder) {
        return builder
                .uponReceiving("delete payment")
                .path(PAYMENTS_BASE_PATH + PAYMENT_ID)
                .method(HttpMethod.DELETE)
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Test
    @PactVerification(fragment = "deleteNonExistingPaymentContract")
    public void deletesNonExistingPayment() {

        Response response = paymentsApi().path(PAYMENT_ID)
                .request().delete();

        assertThat(response.getStatus()).isEqualTo(404);
    }
}
