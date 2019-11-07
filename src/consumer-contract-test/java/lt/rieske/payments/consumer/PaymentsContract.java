package lt.rieske.payments.consumer;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import io.restassured.RestAssured;
import org.apache.commons.codec.Charsets;
import org.junit.Before;
import org.junit.Rule;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PaymentsContract {

    static final String CONSUMER = "some-external-consumer";

    static final String PAYMENTS_BASE_PATH = "/api/v1/payments/";

    static final String PAYMENT_ID = "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43";

    static final String TRANSACTION_JSON;

    static {
        try {
            TRANSACTION_JSON = Files.readString(Paths.get("src/consumer-contract-test/resources/transaction.json"), Charsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("payments-api", this);

    @Before
    public void init() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = "http://localhost:" + mockProvider.getPort() + PAYMENTS_BASE_PATH;
    }

    PactDslJsonBody paymentSchema(String paymentId) {
        return paymentSchema(paymentId, new PactDslJsonBody());
    }

    PactDslJsonBody paymentSchema(String paymentId, PactDslJsonBody dsl) {
        // @formatter:off
        return dsl
          .stringType("type", "Payment")
          .stringType("id", paymentId)
          .integerType("version", 0)
          .stringType("organisation_id", "743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb")
          .object("attributes")
            .stringType("amount", "42.00")
            .stringType("currency", "GBP")
            .stringType("end_to_end_reference", "Wil piano Jan")
            .stringType("numeric_reference", "1002001")
            .stringType("payment_id", "123456789012345678")
            .stringType("payment_purpose", "Paying for goods/services")
            .stringType("payment_scheme", "FPS")
            .stringType("payment_type", "Credit")
            .stringType("processing_date", "2017-01-18")
            .stringType("reference", "Payment for Em's piano lessons")
            .stringType("scheme_payment_sub_type", "InternetBanking")
            .stringType("scheme_payment_type", "ImmediatePayment")
            .object("beneficiary_party")
                .stringType("account_name", "W Owens")
                .stringType("account_number", "31926819")
                .stringType("account_number_code", "BBAN")
                .integerType("account_type", 0)
                .stringType("address", "1 The Beneficiary Localtown SE2")
                .stringType("bank_id", "403000")
                .stringType("bank_id_code", "GBDSC")
                .stringType("name", "Wilfred Jeremiah Owens")
            .closeObject()
            .object("charges_information")
              .stringType("bearer_code", "SHAR")
              .stringType("receiver_charges_amount", "1.00")
              .stringType("receiver_charges_currency", "USD")
              .array("sender_charges")
                .object()
                  .stringType("amount", "5.00")
                  .stringType("currency", "GBP")
                .closeObject()
                .object()
                  .stringType("amount", "10.00")
                  .stringType("currency", "USD")
                .closeObject()
              .closeArray()
            .closeObject()
            .object("debtor_party")
              .stringType("account_name", "EJ Brown Black")
              .stringType("account_number", "GB29XABC10161234567801")
              .stringType("account_number_code", "IBAN")
              .stringType("address", "10 Debtor Crescent Sourcetown NE1")
              .stringType("bank_id", "203301")
              .stringType("bank_id_code", "GBDSC")
              .stringType("name", "Emelia Jane Brown")
            .closeObject()
            .object("fx")
              .stringType("contract_reference", "FX123")
              .stringType("exchange_rate", "2.00000")
              .stringType("original_amount", "200.42")
              .stringType("original_currency", "USD")
            .closeObject()
            .object("sponsor_party")
              .stringType("account_number", "56781234")
              .stringType("bank_id", "123123")
              .stringType("bank_id_code", "GBDSC")
            .closeObject()
          .closeObject()
          .asBody();
        // @formatter:on
    }
}
