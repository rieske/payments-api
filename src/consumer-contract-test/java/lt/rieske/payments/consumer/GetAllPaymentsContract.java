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

import static org.assertj.core.api.Assertions.assertThat;

public class GetAllPaymentsContract extends PaymentsContract {

    @Pact(consumer = CONSUMER)
    public RequestResponsePact getAllPaymentsContract(PactDslWithProvider builder) {
        return builder
                .given("payment exists", "paymentId", PAYMENT_ID)
                .uponReceiving("get all payments")
                .path(PAYMENTS_BASE_PATH)
                .matchHeader("Accept", "application/json")
                .method(HttpMethod.GET)
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
        Response response = paymentsApi().request(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Pact(consumer = CONSUMER)
    public RequestResponsePact getAllPaymentsWhenNoneExistContract(PactDslWithProvider builder) {
        return builder
                .uponReceiving("get all payments when none exist")
                .path(PAYMENTS_BASE_PATH)
                .matchHeader("Accept", "application/json")
                .method(HttpMethod.GET)
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
        Response response = paymentsApi().request(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(200);
    }

}
