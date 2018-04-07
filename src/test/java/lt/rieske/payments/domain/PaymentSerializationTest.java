package lt.rieske.payments.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.rieske.payments.infrastructure.PaymentsObjectMapper;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentSerializationTest {

    @Test
    public void deserializesAndSerializesExamplePayments() throws IOException, JSONException {
        ObjectMapper mapper = new PaymentsObjectMapper();
        String paymentsJson = IOUtils.resourceToString("/fixtures/transactions.json", Charsets.UTF_8);

        List<Payment> payments = mapper.readValue(paymentsJson, new TypeReference<List<Payment>>() {});

        assertThat(payments).hasSize(14);
        JSONAssert.assertEquals(paymentsJson, mapper.writeValueAsString(payments), true);
    }
}
