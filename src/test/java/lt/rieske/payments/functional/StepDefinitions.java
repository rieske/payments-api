package lt.rieske.payments.functional;

import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java8.En;


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

        Given("^payment \"([^\"]*)\" exists$", super::paymentExists);
        Given("^payment \"([^\"]*)\" exists with id \"([^\"]*)\"$", super::paymentWithIdExists);
        Given("^no payments exist$", super::assertNoPaymentsExist);
        Given("^payment with id \"([^\"]*)\" does not exist$", super::assertPaymentDoesNotExist);
        Given("^payments \"([^\"]*)\" exist$", super::paymentsListExists);

        When("^the client issues a GET \"([^\"]*)\" requesting \"([^\"]*)\"$", super::get);
        When("^the client issues a POST to \"([^\"]*)\" with payload \"([^\"]*)\"$", super::post);
        When("^the client issues a POST to \"([^\"]*)\" with payload \"([^\"]*)\" requesting \"([^\"]*)\"$", super::postAccepting);

        Then("^the client receives status code of (\\d+)$", super::assertHttpStatus);

        And("^response contains \"([^\"]*)\" header with value \"([^\"]*)\"$", super::assertHeader);
        And("^new resource location in Location header$", super::assertLocationHeaderPointsToNewResource);
        And("^the response body is empty$", super::assertResponseBodyIsEmpty);
        And("^the response body contains resource matching \"([^\"]*)\"$", super::assertResponseBodyMatchesFixture);
        And("^the response body contains an empty list of payments$", super::assertResponseBodyContainsAnEmptyListOfPayments);
        And("^the response body contains a list of payments matching \"([^\"]*)\"$", super::assertResponseBodyContainsPayments);

    }

}
