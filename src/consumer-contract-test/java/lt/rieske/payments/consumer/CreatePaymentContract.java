package lt.rieske.payments.consumer;


import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class CreatePaymentContract extends PaymentsContract {

    @Pact(consumer = CONSUMER)
    public RequestResponsePact createNewPaymentContract(PactDslWithProvider builder) {
        return builder
                .uponReceiving("create new payment")
                .path(PAYMENTS_BASE_PATH)
                .matchHeader("Content-Type", "application/json; charset=UTF-8")
                .method("POST")
                .body(paymentSchema(PAYMENT_ID))
                .willRespondWith()
                .status(201)
                .matchHeader("Location", ".*" + PAYMENTS_BASE_PATH + ".*", PAYMENTS_BASE_PATH + PAYMENT_ID)
                .toPact();
    }

    @Test
    @PactVerification(fragment = "createNewPaymentContract")
    public void createsNewPayment() {

        // @formatter:off
        given()
            .contentType(ContentType.JSON)
            .body(TRANSACTION_JSON)
        .when()
            .post()
        .then()
            .statusCode(201)
            .header("Location", equalTo(PAYMENTS_BASE_PATH + PAYMENT_ID));
        // @formatter:on
    }
}
