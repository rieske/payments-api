package lt.rieske.payments.domain;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import java.util.Currency;

@RepositoryEventHandler
public class PaymentBusinessRulesValidator {

    @HandleBeforeCreate
    @HandleBeforeSave
    public void validate(Payment p) {
        Payment.PaymentAttributes attributes = p.getAttributes();
        validateForexCurrency(attributes);
    }

    private void validateForexCurrency(Payment.PaymentAttributes attributes) {
        if (attributes == null) {
            return;
        }
        Payment.PaymentAttributes.Forex forex = attributes.getFx();
        Currency paymentCurrency = attributes.getCurrency();
        if (forex != null && paymentCurrency != null && paymentCurrency.equals(forex.getOriginalCurrency())) {
            throw new BadForexCurrencyException();
        }
    }

}
