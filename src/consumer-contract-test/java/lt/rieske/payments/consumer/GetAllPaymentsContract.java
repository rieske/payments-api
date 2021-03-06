package lt.rieske.payments.consumer;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class GetAllPaymentsContract extends PaymentsContract {

    @Pact(consumer = CONSUMER)
    public RequestResponsePact getAllPaymentsContract(PactDslWithProvider builder) {
        return builder
                .given("payment exists", "paymentId", PAYMENT_ID)
                .uponReceiving("get all payments")
                .path(PAYMENTS_BASE_PATH)
                .matchHeader("Accept", "application/json")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(paymentSchema(PAYMENT_ID, new PactDslJsonBody()
                        .object("_embedded")
                        .array("payments").object())
                        .closeObject()
                        .closeArray()
                        .closeObject()
                        .asBody())
                .matchHeader("Content-Type", "application/json")
                .toPact();
    }

    @Test
    @PactVerification(fragment = "getAllPaymentsContract")
    public void returnsListOfPayments() {
        // @formatter:off
        given()
            .accept("application/json")
        .when()
            .get()
        .then()
            .statusCode(200);
        // @formatter:on
    }

    @Pact(consumer = CONSUMER)
    public RequestResponsePact getAllPaymentsWhenNoneExistContract(PactDslWithProvider builder) {
        return builder
                .uponReceiving("get all payments when none exist")
                .path(PAYMENTS_BASE_PATH)
                .matchHeader("Accept", "application/json")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .object("_embedded")
                        .array("payments").closeArray()
                        .closeObject()
                        .asBody())
                .matchHeader("Content-Type", "application/json")
                .toPact();
    }

    @Test
    @PactVerification(fragment = "getAllPaymentsWhenNoneExistContract")
    public void returnsEmptyListWhenNoPaymentsExist() {
        // @formatter:off
        given()
            .accept("application/json")
        .when()
            .get()
        .then()
            .statusCode(200);
        // @formatter:on
    }

}
