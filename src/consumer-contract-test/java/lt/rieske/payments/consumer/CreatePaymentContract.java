package lt.rieske.payments.consumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CreatePaymentContract extends PaymentsContract {

    private final String paymentId = "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43";

    @Pact(consumer = CONSUMER)
    public RequestResponsePact createNewPaymentContract(PactDslWithProvider builder) {
        return builder
          .uponReceiving("create new payment")
          .path(PAYMENTS_BASE_PATH)
          .matchHeader("Content-Type", "application/json")
          .method("POST")
          .body(paymentSchema(paymentId))
          .willRespondWith()
          .status(201)
          .matchHeader("Location", PAYMENTS_BASE_PATH + ".*", PAYMENTS_BASE_PATH + paymentId)
          .toPact();
    }

    @Test
    @PactVerification(fragment = "createNewPaymentContract")
    public void createsNewPayment() throws IOException {
        String transactionJson = IOUtils.resourceToString("/transaction.json", Charsets.UTF_8);

        Response response = paymentsApi().request()
          .post(Entity.json(transactionJson));

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getHeaderString("Location")).isEqualTo(PAYMENTS_BASE_PATH + paymentId);
    }

    @Pact(consumer = CONSUMER)
    public RequestResponsePact conflictWhenCreatingPaymentContract(PactDslWithProvider builder) {
        return builder
          .given("payment exists", "paymentId", paymentId)
          .uponReceiving("create new payment given one with same id already exists")
          .path(PAYMENTS_BASE_PATH)
          .matchHeader("Content-Type", "application/json")
          .method("POST")
          .body(paymentSchema(paymentId))
          .willRespondWith()
          .status(409)
          .toPact();
    }

    @Test
    @PactVerification(fragment = "conflictWhenCreatingPaymentContract")
    public void conflictsGivenPaymentWithSameIdExists() throws IOException {
        String transactionJson = IOUtils.resourceToString("/transaction.json", Charsets.UTF_8);

        Response response = paymentsApi().request()
          .post(Entity.json(transactionJson));

        assertThat(response.getStatus()).isEqualTo(409);
        assertThat(response.readEntity(String.class)).isEmpty();
    }
}
