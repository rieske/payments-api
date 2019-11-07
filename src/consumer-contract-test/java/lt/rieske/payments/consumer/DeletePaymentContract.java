package lt.rieske.payments.consumer;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.Test;

import static io.restassured.RestAssured.when;

public class DeletePaymentContract extends PaymentsContract {

    @Pact(consumer = CONSUMER)
    public RequestResponsePact deletePaymentContract(PactDslWithProvider builder) {
        return builder
                .given("payment exists", "paymentId", PAYMENT_ID)
                .uponReceiving("delete payment")
                .path(PAYMENTS_BASE_PATH + PAYMENT_ID)
                .method("DELETE")
                .willRespondWith()
                .status(204)
                .toPact();
    }

    @Test
    @PactVerification(fragment = "deletePaymentContract")
    public void deletesExistingPayment() {
        // @formatter:off
        when()
            .delete(PAYMENT_ID)
        .then()
            .statusCode(204);
        // @formatter:on
    }

    @Pact(consumer = CONSUMER)
    public RequestResponsePact deleteNonExistingPaymentContract(PactDslWithProvider builder) {
        return builder
                .uponReceiving("delete payment")
                .path(PAYMENTS_BASE_PATH + PAYMENT_ID)
                .method("DELETE")
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Test
    @PactVerification(fragment = "deleteNonExistingPaymentContract")
    public void deletesNonExistingPayment() {
        // @formatter:off
        when()
            .delete(PAYMENT_ID)
        .then()
            .statusCode(404);
        // @formatter:on
    }
}
