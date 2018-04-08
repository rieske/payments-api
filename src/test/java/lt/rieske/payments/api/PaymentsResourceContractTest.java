package lt.rieske.payments.api;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.VerificationReports;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.rieske.payments.domain.Payment;
import lt.rieske.payments.domain.PaymentsRepository;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.Map;

@RunWith(SpringRestPactRunner.class)
@Provider("payments-api")
@PactFolder("build/pacts")
@VerificationReports({"console", "markdown"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
        "server.port=8888"
})
public class PaymentsResourceContractTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PaymentsRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @TestTarget
    public final Target target = new HttpTarget(8888);

    @After
    public void deleteTestData() {
        repository.deleteAll();
    }

    @State("payment exists")
    public void givenPaymentExists(Map<String, String> state) throws IOException {
        String paymentId = state.get("paymentId");
        Payment payment = validPayment(paymentId);
        repository.save(payment);
    }

    private Payment validPayment(String paymentId) throws IOException {
        Payment payment = mapper.readValue(getClass().getResourceAsStream("/fixtures/transaction.json"), Payment.class);
        payment.setId(paymentId);
        return payment;
    }
}