package lt.rieske.payments.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;

public class PaymentsObjectMapper extends ObjectMapper {
    public PaymentsObjectMapper() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(BigDecimal.class, new ToStringSerializer());
        registerModule(module);
        setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }
}
