package lt.rieske.payments.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

@Data
public class Payment {

    private final String id;
    private final String type;
    private final int version;
    private final String organisationId;
    private final PaymentAttributes attributes;

    @Data
    public static class PaymentAttributes {
        private final BigDecimal amount;
        private final Beneficiary beneficiaryParty;
        private final Charges chargesInformation;
        private final Currency currency;
        private final Debtor debtorParty;
        private final String endToEndReference;
        private final Forex fx;
        private final String numericReference;
        private final String paymentId;
        private final String paymentPurpose;
        private final String paymentScheme;
        private final String paymentType;
        private final LocalDate processingDate;
        private final String reference;
        private final String schemePaymentType;
        private final String schemePaymentSubType;
        private final Sponsor sponsorParty;

        @Data
        public static class Beneficiary {
            private final String accountName;
            private final String accountNumber;
            private final String accountNumberCode;
            private final int accountType;
            private final String address;
            private final String bankId;
            private final String bankIdCode;
            private final String name;
        }

        @Data
        public static class Debtor {
            private final String accountName;
            private final String accountNumber;
            private final String accountNumberCode;
            private final String address;
            private final String bankId;
            private final String bankIdCode;
            private final String name;
        }

        @Data
        public static class Charges {
            private final String bearerCode;
            private final BigDecimal receiverChargesAmount;
            private final Currency receiverChargesCurrency;
            private final List<Charge> senderCharges;

            @Data
            public static class Charge {
                private final BigDecimal amount;
                private final Currency currency;
            }
        }

        @Data
        public static class Forex {
            private final String contractReference;
            private final BigDecimal exchangeRate;
            private final BigDecimal originalAmount;
            private final Currency originalCurrency;
        }

        @Data
        public static class Sponsor {
            private final String accountNumber;
            private final String bankId;
            private final String bankIdCode;
        }
    }
}

