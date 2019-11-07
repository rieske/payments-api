package lt.rieske.payments.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.rieske.payments.infrastructure.config.ApplicationConfiguration;
import org.apache.commons.codec.Charsets;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfiguration.class)
public class ForexSerializationTest {

    @Autowired
    private Jackson2ObjectMapperBuilder jacksonBuilder;

    @Test
    public void preservesForexRateScale() throws IOException, JSONException {
        ObjectMapper mapper = jacksonBuilder.build();

        String forexJson = Files.readString(Paths.get("src/test/resources/fixtures/forex.json"), Charsets.UTF_8);

        Payment.PaymentAttributes.Forex forex = mapper.readValue(forexJson, Payment.PaymentAttributes.Forex.class);

        String serializedForex = mapper.writeValueAsString(forex);
        JSONAssert.assertEquals(forexJson, serializedForex, true);
    }
}