package lt.rieske.payments.domain;

public class BadForexCurrencyException extends RuntimeException {

    BadForexCurrencyException() {
        super("Forex currency can not be the same as payment currency");
    }
}
