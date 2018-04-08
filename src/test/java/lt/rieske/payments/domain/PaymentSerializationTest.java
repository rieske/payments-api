package lt.rieske.payments.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.rieske.payments.infrastructure.config.ApplicationConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfiguration.class)
public class PaymentSerializationTest {

    @Autowired
    private Jackson2ObjectMapperBuilder jacksonBuilder;

    @Test
    public void deserializesExamplePayments() throws IOException {
        ObjectMapper mapper = jacksonBuilder.build();

        List<Payment> payments = mapper.readValue(getClass().getResourceAsStream("/fixtures/transactions.json"),
                new TypeReference<List<Payment>>() {});

        assertThat(payments).hasSize(14);
    }
}
