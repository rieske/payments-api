package lt.rieske.payments.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "lt.rieske.payments.infrastructure.OnlyIfAbsentIdGenerator")
    @Column(unique = true, nullable = false)
    private String id;
    private String type;
    private int version;
    private String organisationId;
    private PaymentAttributes attributes;

    @Embeddable
    @Data
    public static class PaymentAttributes {
        private BigDecimal amount;

        @OneToOne(cascade = ALL)
        private Beneficiary beneficiaryParty;

        @OneToOne(cascade = ALL)
        private Debtor debtorParty;

        @OneToOne(cascade = ALL)
        private Sponsor sponsorParty;

        private Charges chargesInformation;
        private Currency currency;
        private String endToEndReference;
        private Forex fx;
        private String numericReference;
        private String paymentId;
        private String paymentPurpose;
        private String paymentScheme;
        private String paymentType;
        private LocalDate processingDate;
        private String reference;
        private String schemePaymentType;
        private String schemePaymentSubType;

        @Entity
        @Data
        public static class Beneficiary {

            @Id
            @GeneratedValue(generator = "uuid")
            @GenericGenerator(name = "uuid", strategy = "uuid2")
            private String beneficiaryId;

            private String accountName;
            private String accountNumber;
            private String accountNumberCode;
            private int accountType;
            private String address;
            private String bankId;
            private String bankIdCode;
            private String name;
        }

        @Entity
        @Data
        public static class Debtor {

            @Id
            @GeneratedValue(generator = "uuid")
            @GenericGenerator(name = "uuid", strategy = "uuid2")
            private String debtorId;

            private String accountName;
            private String accountNumber;
            private String accountNumberCode;
            private String address;
            private String bankId;
            private String bankIdCode;
            private String name;
        }

        @Embeddable
        @Data
        public static class Charges {
            private String bearerCode;
            private BigDecimal receiverChargesAmount;
            private Currency receiverChargesCurrency;

            @OneToMany(cascade = ALL)
            private List<Charge> senderCharges = new ArrayList<>();

            @Entity
            @Data
            public static class Charge {

                @Id
                @GeneratedValue(generator = "uuid")
                @GenericGenerator(name = "uuid", strategy = "uuid2")
                private String chargeId;

                private BigDecimal amount;
                private Currency currency;
            }
        }

        @Embeddable
        @Data
        public static class Forex {
            private String contractReference;
            private BigDecimal exchangeRate;
            private BigDecimal originalAmount;
            private Currency originalCurrency;
        }

        @Entity
        @Data
        public static class Sponsor {

            @Id
            @GeneratedValue(generator = "uuid")
            @GenericGenerator(name = "uuid", strategy = "uuid2")
            private String sponsorId;

            private String accountNumber;
            private String bankId;
            private String bankIdCode;
        }
    }
}

