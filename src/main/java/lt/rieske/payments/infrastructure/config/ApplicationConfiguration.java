package lt.rieske.payments.infrastructure.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Optional;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> commonTags(@Value("${spring.application.name}") String serviceName) {
        return r -> r.config().commonTags(
          serviceName,
          Optional.ofNullable(System.getenv("JMX_HOSTNAME"))
            .map(host -> StringUtils.substringBefore(host, "."))
            .orElse("localhost"));
    }

    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .paths(PathSelectors.regex("/api.*"))
          .build();
    }

}
