package lt.rieske.payments.api;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.RestPactRunner;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.VerificationReports;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.target.MockMvcTarget;
import lt.rieske.payments.domain.PaymentsRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(RestPactRunner.class)
@Provider("payments-api")
@PactFolder("build/pacts")
@VerificationReports({"console", "markdown"})
public class PaymentsResourceContractTest {

    @Mock
    private PaymentsRepository paymentsRepository;

    @InjectMocks
    private PaymentsResource resource;

    @Before
    public void setUpResource() {
        MockitoAnnotations.initMocks(this);

        when(paymentsRepository.save(any())).thenReturn(UUID.randomUUID().toString());

        target.setControllers(resource);
    }

    @TestTarget
    public final MockMvcTarget target = new MockMvcTarget();

    @State("payment exists")
    public void givenPaymentExists(Map<String, String> state) {
        String paymentId = state.get("paymentId");

        when(paymentsRepository.findPayment(paymentId)).thenReturn(Optional.of("{\n" +
          "  \"type\": \"Payment\",\n" +
          "  \"id\": \"4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43\",\n" +
          "  \"version\": 0,\n" +
          "  \"organisation_id\": \"743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb\",\n" +
          "  \"attributes\": {\n" +
          "    \"amount\": \"100.21\",\n" +
          "    \"beneficiary_party\": {\n" +
          "      \"account_name\": \"W Owens\",\n" +
          "      \"account_number\": \"31926819\",\n" +
          "      \"account_number_code\": \"BBAN\",\n" +
          "      \"account_type\": 0,\n" +
          "      \"address\": \"1 The Beneficiary Localtown SE2\",\n" +
          "      \"bank_id\": \"403000\",\n" +
          "      \"bank_id_code\": \"GBDSC\",\n" +
          "      \"name\": \"Wilfred Jeremiah Owens\"\n" +
          "    },\n" +
          "    \"charges_information\": {\n" +
          "      \"bearer_code\": \"SHAR\",\n" +
          "      \"sender_charges\": [\n" +
          "        {\n" +
          "          \"amount\": \"5.00\",\n" +
          "          \"currency\": \"GBP\"\n" +
          "        },\n" +
          "        {\n" +
          "          \"amount\": \"10.00\",\n" +
          "          \"currency\": \"USD\"\n" +
          "        }\n" +
          "      ],\n" +
          "      \"receiver_charges_amount\": \"1.00\",\n" +
          "      \"receiver_charges_currency\": \"USD\"\n" +
          "    },\n" +
          "    \"currency\": \"GBP\",\n" +
          "    \"debtor_party\": {\n" +
          "      \"account_name\": \"EJ Brown Black\",\n" +
          "      \"account_number\": \"GB29XABC10161234567801\",\n" +
          "      \"account_number_code\": \"IBAN\",\n" +
          "      \"address\": \"10 Debtor Crescent Sourcetown NE1\",\n" +
          "      \"bank_id\": \"203301\",\n" +
          "      \"bank_id_code\": \"GBDSC\",\n" +
          "      \"name\": \"Emelia Jane Brown\"\n" +
          "    },\n" +
          "    \"end_to_end_reference\": \"Wil piano Jan\",\n" +
          "    \"fx\": {\n" +
          "      \"contract_reference\": \"FX123\",\n" +
          "      \"exchange_rate\": \"2.00000\",\n" +
          "      \"original_amount\": \"200.42\",\n" +
          "      \"original_currency\": \"USD\"\n" +
          "    },\n" +
          "    \"numeric_reference\": \"1002001\",\n" +
          "    \"payment_id\": \"123456789012345678\",\n" +
          "    \"payment_purpose\": \"Paying for goods/services\",\n" +
          "    \"payment_scheme\": \"FPS\",\n" +
          "    \"payment_type\": \"Credit\",\n" +
          "    \"processing_date\": \"2017-01-18\",\n" +
          "    \"reference\": \"Payment for Em's piano lessons\",\n" +
          "    \"scheme_payment_sub_type\": \"InternetBanking\",\n" +
          "    \"scheme_payment_type\": \"ImmediatePayment\",\n" +
          "    \"sponsor_party\": {\n" +
          "      \"account_number\": \"56781234\",\n" +
          "      \"bank_id\": \"12345\",\n" +
          "      \"bank_id_code\": \"GBDSC\"\n" +
          "    }\n" +
          "  }\n" +
          "}"));
    }
}
