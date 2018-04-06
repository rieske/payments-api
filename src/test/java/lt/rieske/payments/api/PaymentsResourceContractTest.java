package lt.rieske.payments.api;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.RestPactRunner;
import au.com.dius.pact.provider.junit.VerificationReports;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.target.MockMvcTarget;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

@RunWith(RestPactRunner.class)
@Provider("payments-api")
@PactFolder("build/pacts")
@VerificationReports({"console", "markdown"})
public class PaymentsResourceContractTest {

    @InjectMocks
    private PaymentsResource resource;

    @Before
    public void setUpResource() {
        MockitoAnnotations.initMocks(this);

        target.setControllers(resource);
    }

    @TestTarget
    public final MockMvcTarget target = new MockMvcTarget();
}
