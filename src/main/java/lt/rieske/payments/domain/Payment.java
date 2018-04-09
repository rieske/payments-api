package lt.rieske.payments.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;

@Data
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "lt.rieske.payments.infrastructure.OnlyIfAbsentIdGenerator")
    @Column(unique = true, nullable = false)
    private UUID id;
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

        @Data
        @Entity
        @Table(name = "beneficiary")
        public static class Beneficiary {

            @Id
            @GeneratedValue(generator = "uuid")
            @GenericGenerator(name = "uuid", strategy = "uuid2")
            private UUID beneficiaryId;

            private String accountName;
            private String accountNumber;
            private String accountNumberCode;
            private int accountType;
            private String address;
            private String bankId;
            private String bankIdCode;
            private String name;
        }

        @Data
        @Entity
        @Table(name = "debtor")
        public static class Debtor {

            @Id
            @GeneratedValue(generator = "uuid")
            @GenericGenerator(name = "uuid", strategy = "uuid2")
            private UUID debtorId;

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

            @OneToMany(cascade = ALL, fetch = FetchType.EAGER, orphanRemoval = true)
            private List<Charge> senderCharges = new ArrayList<>();

            @Entity
            @Data
            @Table(name = "charge")
            public static class Charge {

                @Id
                @GeneratedValue(generator = "uuid")
                @GenericGenerator(name = "uuid", strategy = "uuid2")
                private UUID chargeId;

                private BigDecimal amount;
                private Currency currency;
            }
        }

        @Embeddable
        @Data
        public static class Forex {
            private String contractReference;
            @Column(precision = 20, scale = 5)
            private BigDecimal exchangeRate;
            private BigDecimal originalAmount;
            private Currency originalCurrency;
        }

        @Entity
        @Data
        @Table(name = "sponsor")
        public static class Sponsor {

            @Id
            @GeneratedValue(generator = "uuid")
            @GenericGenerator(name = "uuid", strategy = "uuid2")
            private UUID sponsorId;

            private String accountNumber;
            private String bankId;
            private String bankIdCode;
        }
    }
}

