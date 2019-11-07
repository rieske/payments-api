package lt.rieske.payments.consumer;


import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;

public class UpdatePaymentContract extends PaymentsContract {

    @Pact(consumer = CONSUMER)
    public RequestResponsePact updateExistingPaymentContract(PactDslWithProvider builder) {
        return builder
                .given("payment exists", "paymentId", PAYMENT_ID)
                .uponReceiving("update a payment given one with same id already exists")
                .path(PAYMENTS_BASE_PATH + PAYMENT_ID)
                .matchHeader("Content-Type", "application/json; charset=UTF-8")
                .method("PATCH")
                .body(paymentSchema(PAYMENT_ID))
                .willRespondWith()
                .status(204)
                .toPact();
    }

    @Test
    @PactVerification(fragment = "updateExistingPaymentContract")
    public void updatesExistingPayment() {
        // @formatter:off
        given()
            .contentType(ContentType.JSON)
            .body(TRANSACTION_JSON)
        .when()
            .patch(PAYMENT_ID)
        .then()
            .statusCode(204)
            .body(emptyString());
        // @formatter:on
    }

    @Pact(consumer = CONSUMER)
    public RequestResponsePact notFoundWhenUpdatingNonExistingPaymentContract(PactDslWithProvider builder) {
        return builder
                .uponReceiving("update a payment given one does not exist")
                .path(PAYMENTS_BASE_PATH + PAYMENT_ID)
                .matchHeader("Content-Type", "application/json; charset=UTF-8")
                .method("PATCH")
                .body(paymentSchema(PAYMENT_ID))
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Test
    @PactVerification(fragment = "notFoundWhenUpdatingNonExistingPaymentContract")
    public void notFoundWhenUpdatingNonExistingPayment() {
        // @formatter:off
        given()
            .contentType(ContentType.JSON)
            .body(TRANSACTION_JSON)
        .when()
            .patch(PAYMENT_ID)
        .then()
            .statusCode(404)
            .body(emptyString());
        // @formatter:on
    }

}
