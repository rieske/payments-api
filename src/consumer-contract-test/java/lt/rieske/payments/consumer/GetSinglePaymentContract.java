package lt.rieske.payments.consumer;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class GetSinglePaymentContract extends PaymentsContract {

    @Pact(consumer = CONSUMER)
    public RequestResponsePact getSingleNonExistingPaymentContract(PactDslWithProvider builder) {
        return builder
          .uponReceiving("get single non existing payment")
          .path(PAYMENTS_BASE_PATH + PAYMENT_ID)
          .matchHeader("Accept", "application/json")
          .method("GET")
          .willRespondWith()
          .status(404)
          .toPact();
    }

    @Test
    @PactVerification(fragment = "getSingleNonExistingPaymentContract")
    public void returnsNotFoundWhenRequestedPaymentDoesNotExist() {
        // @formatter:off
        given()
            .accept("application/json")
        .when()
            .get(PAYMENT_ID)
        .then()
            .statusCode(404);
        // @formatter:on
    }

    @Pact(consumer = CONSUMER)
    public RequestResponsePact getSinglePaymentContract(PactDslWithProvider builder) {
        return builder
          .given("payment exists", "paymentId", PAYMENT_ID)
          .uponReceiving("get single payment")
          .path(PAYMENTS_BASE_PATH + PAYMENT_ID)
          .matchHeader("Accept", "application/json")
          .method("GET")
          .willRespondWith()
          .status(200)
          .matchHeader("Content-Type", "application/json")
          .body(paymentSchema(PAYMENT_ID))
          .toPact();
    }

    @Test
    @PactVerification(fragment = "getSinglePaymentContract")
    public void returnsAnExistingPayment() {
        // @formatter:off
        given()
            .accept("application/json")
        .when()
            .get(PAYMENT_ID)
        .then()
            .statusCode(200);
        // @formatter:on
    }
}
