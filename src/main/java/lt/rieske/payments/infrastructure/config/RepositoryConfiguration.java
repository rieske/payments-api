package lt.rieske.payments.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.validation.Validator;

@Configuration
@RequiredArgsConstructor
public class RepositoryConfiguration implements RepositoryRestConfigurer {

    private final Validator validator;

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener v) {
        v.addValidator("beforeCreate", validator);
        v.addValidator("beforeSave", validator);
    }

}