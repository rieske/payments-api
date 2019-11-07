package lt.rieske.payments.infrastructure.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.micrometer.core.instrument.MeterRegistry;
import lt.rieske.payments.domain.Payment;
import lt.rieske.payments.domain.PaymentBusinessRulesValidator;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Configuration
public class ApplicationConfiguration {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> commonTags(@Value("${spring.application.name}") String serviceName) {
        return r -> r.config().commonTags(
                serviceName,
                Optional.ofNullable(System.getenv("JMX_HOSTNAME"))
                        .map(host -> StringUtils.substringBefore(host, "."))
                        .orElse("localhost"));
    }

    @Bean
    Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder jacksonBuilder = new Jackson2ObjectMapperBuilder();
        jacksonBuilder.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        SimpleModule paymentsModule = new SimpleModule();
        paymentsModule.addSerializer(BigDecimal.class, new ToStringSerializer());
        jacksonBuilder.modules(new JavaTimeModule(), paymentsModule);
        jacksonBuilder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        jacksonBuilder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        return jacksonBuilder;
    }

    @Bean
    RepositoryRestConfigurer repositoryRestConfigurer() {

        return new RepositoryRestConfigurer() {

            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
                config.exposeIdsFor(Payment.class);
            }
        };
    }

    @Bean
    PaymentBusinessRulesValidator paymentEventHandler() {
        return new PaymentBusinessRulesValidator();
    }
}