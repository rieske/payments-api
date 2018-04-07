package lt.rieske.payments.api;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.RestPactRunner;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.VerificationReports;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.target.MockMvcTarget;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.rieske.payments.domain.Payment;
import lt.rieske.payments.domain.PaymentsRepository;
import lt.rieske.payments.infrastructure.PaymentsObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.Collections;
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

    private final ObjectMapper mapper = new PaymentsObjectMapper();

    @Mock
    private PaymentsRepository paymentsRepository;

    @InjectMocks
    private PaymentsResource resource;

    @Before
    public void setUpResource() {
        MockitoAnnotations.initMocks(this);

        when(paymentsRepository.save(any())).thenReturn(UUID.randomUUID().toString());

        target.setControllers(resource);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);
        target.setMessageConverters(Collections.singletonList(converter));
    }

    @TestTarget
    public final MockMvcTarget target = new MockMvcTarget();

    @State("payment exists")
    public void givenPaymentExists(Map<String, String> state) throws IOException {
        String paymentId = state.get("paymentId");
        Payment payment = validPayment(paymentId);

        when(paymentsRepository.findAllPayments()).thenReturn(Collections.singletonList(payment));
        when(paymentsRepository.findPayment(paymentId)).thenReturn(Optional.of(payment));
    }

    private Payment validPayment(String paymentId) throws IOException {
        return mapper.readValue(getClass().getResourceAsStream("/fixtures/transaction.json"), Payment.class);
    }
}