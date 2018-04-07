package lt.rieske.payments.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.rieske.payments.infrastructure.PaymentsObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentSerializationTest {

    @Test
    public void deserializesExamplePayments() throws IOException {
        ObjectMapper mapper = new PaymentsObjectMapper();

        List<Payment> payments = mapper.readValue(getClass().getResourceAsStream("/fixtures/transactions.json"),
                new TypeReference<List<Payment>>() {});

        assertThat(payments).hasSize(14);
    }
}
