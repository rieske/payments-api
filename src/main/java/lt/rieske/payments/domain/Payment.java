package lt.rieske.payments.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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

    @NotNull
    @Pattern(regexp = "Payment")
    private String type;

    @NotNull
    private Integer version;

    @Length(min = 1, max = 255)
    @NotNull
    private String organisationId;

    @NotNull
    @Valid
    private PaymentAttributes attributes;

    @Embeddable
    @Data
    public static class PaymentAttributes {

        @Positive
        @NotNull
        private BigDecimal amount;

        @NotNull
        private Currency currency;

        @NotNull
        @Valid
        @OneToOne(cascade = ALL)
        private Beneficiary beneficiaryParty;

        @NotNull
        @Valid
        @OneToOne(cascade = ALL)
        private Debtor debtorParty;

        @NotNull
        @Valid
        @OneToOne(cascade = ALL)
        private Sponsor sponsorParty;

        @NotNull
        @Valid
        private Charges chargesInformation;

        @Valid
        private Forex fx;

        @Length(min = 1, max = 255)
        @NotNull
        private String endToEndReference;

        @Length(min = 1, max = 255)
        @NotNull
        private String numericReference;

        @Length(min = 1, max = 255)
        @NotNull
        private String paymentId;

        @Length(min = 1, max = 255)
        @NotNull
        private String paymentPurpose;

        @Length(min = 1, max = 255)
        @NotNull
        private String paymentScheme;

        @Length(min = 1, max = 255)
        @NotNull
        private String paymentType;

        @NotNull
        private LocalDate processingDate;

        @Length(min = 1, max = 255)
        @NotNull
        private String reference;

        @Length(min = 1, max = 255)
        @NotNull
        private String schemePaymentType;

        @Length(min = 1, max = 255)
        @NotNull
        private String schemePaymentSubType;

        @Data
        @Entity
        @Table(name = "beneficiary")
        public static class Beneficiary {

            @Id
            @GeneratedValue(generator = "uuid")
            @GenericGenerator(name = "uuid", strategy = "uuid2")
            private UUID beneficiaryId;

            @Length(min = 1, max = 255)
            @NotNull
            private String accountName;

            @Length(min = 1, max = 255)
            @NotNull
            private String accountNumber;

            @Length(min = 1, max = 255)
            @NotNull
            private String accountNumberCode;

            @NotNull
            private Integer accountType;

            @Length(min = 1, max = 255)
            @NotNull
            private String address;

            @Length(min = 1, max = 255)
            @NotNull
            private String bankId;

            @Length(min = 1, max = 255)
            @NotNull
            private String bankIdCode;

            @Length(min = 1, max = 255)
            @NotNull
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

            @Length(min = 1, max = 255)
            @NotNull
            private String accountName;

            @Length(min = 1, max = 255)
            @NotNull
            private String accountNumber;

            @Length(min = 1, max = 255)
            @NotNull
            private String accountNumberCode;

            @Length(min = 1, max = 255)
            @NotNull
            private String address;

            @Length(min = 1, max = 255)
            @NotNull
            private String bankId;

            @Length(min = 1, max = 255)
            @NotNull
            private String bankIdCode;

            @Length(min = 1, max = 255)
            @NotNull
            private String name;
        }

        @Embeddable
        @Data
        public static class Charges {

            @Length(min = 1, max = 255)
            @NotNull
            private String bearerCode;

            @NotNull
            @PositiveOrZero
            private BigDecimal receiverChargesAmount;

            @NotNull
            private Currency receiverChargesCurrency;

            @Valid
            @OneToMany(cascade = ALL, fetch = FetchType.EAGER)
            private List<Charge> senderCharges = new ArrayList<>();

            @Entity
            @Data
            @Table(name = "charge")
            public static class Charge {

                @Id
                @GeneratedValue(generator = "uuid")
                @GenericGenerator(name = "uuid", strategy = "uuid2")
                private UUID chargeId;

                @NotNull
                @Positive
                private BigDecimal amount;

                @NotNull
                private Currency currency;
            }
        }

        @Embeddable
        @Data
        public static class Forex {

            @Length(min = 1, max = 255)
            @NotNull
            private String contractReference;

            @NotNull
            @Positive
            @Column(precision = 20, scale = 5)
            private BigDecimal exchangeRate;

            @NotNull
            @Positive
            private BigDecimal originalAmount;

            @NotNull
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

            @Length(min = 1, max = 255)
            @NotNull
            private String accountNumber;

            @Length(min = 1, max = 255)
            @NotNull
            private String bankId;

            @Length(min = 1, max = 255)
            @NotNull
            private String bankIdCode;
        }
    }
}

