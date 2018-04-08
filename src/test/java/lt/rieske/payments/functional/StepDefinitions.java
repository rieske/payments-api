package lt.rieske.payments.functional;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java8.En;

import static org.assertj.core.api.Assertions.assertThat;


public class StepDefinitions extends SpringBootSteps implements En {

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    public StepDefinitions() {

        Given("^payment \"([^\"]*)\" exists with id \"([^\"]*)\"$", this::paymentWithIdExists);

        Given("^payment with id \"([^\"]*)\" does not exist$", (String paymentId) -> {
            assertThat(paymentsRepository.findById(paymentId)).isEmpty();
        });

        When("^the client issues a GET \"([^\"]*)\" requesting \"([^\"]*)\"$", this::get);
        When("^the client issues a POST to \"([^\"]*)\" with payload \"([^\"]*)\"$", this::post);
        When("^the client issues a POST to \"([^\"]*)\" with payload \"([^\"]*)\" requesting \"([^\"]*)\"$", this::postAccepting);

        Then("^the client receives status code of (\\d+)$", this::assertHttpStatus);

        And("^response contains \"([^\"]*)\" header with value \"([^\"]*)\"$", this::assertHeader);
        And("^new resource location in Location header$", this::assertLocationHeaderPointsToNewResource);
        And("^the response body is empty$", this::assertResponseBodyIsEmpty);
        And("^the response body contains resource matching \"([^\"]*)\"$", this::assertResponseBodyMatchesFixture);


    }

}
