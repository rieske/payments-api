package lt.rieske.payments.functional;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java8.En;

public class StepDefinitions extends MockMvcSteps implements En {

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    public StepDefinitions() {

        Given("^payment ([^\\s]+) exists$", super::paymentExists);
        Given("^payment ([^\\s]+) exists with id ([^\\s]+)$", super::paymentWithIdExists);
        Given("^no payments exist$", super::assertNoPaymentsExist);
        Given("^payment with id ([^\\s]+) does not exist$", super::assertPaymentDoesNotExist);
        Given("^payments ([^\\s]+) exist$", super::paymentsListExists);

        When("^the client issues a GET ([^\\s]+) accepting ([^\\s]+)$", super::get);
        When("^the client issues a POST to ([^\\s]+) with payload ([^\\s]+)$", super::post);
        When("^the client issues a POST to ([^\\s]+) accepting ([^\\s]+) with payload ([^\\s]+)$", super::postAccepting);
        When("^the client issues a POST to ([^\\s]+) accepting ([^\\s]+) with payload ([^\\s]+) with field ([^\\s]+) having \"(.*)\"$",
          super::postAcceptingWithModifiedContent);
        When("^the client issues a DELETE ([^\\s]+)$", super::delete);
        When("^the client issues a PATCH ([^\\s]+) with body ([^\\s]+)$", super::patch);
        When("^the client issues a PATCH ([^\\s]+) accepting ([^\\s]+) with body ([^\\s]+)$", super::patchAccepting);
        When("^the client issues a PATCH ([^\\s]+) accepting ([^\\s]+) with body:$", super::patchAcceptingWithBody);
        When("^the client issues a PATCH to ([^\\s]+) accepting ([^\\s]+) with payload ([^\\s]+) with field ([^\\s]+) having \"(.*)\"$",
          super::patchAcceptingWithModifiedContent);

        Then("^the client receives status code of (\\d+)$", super::assertHttpStatus);

        And("^response contains ([^\\s]+) header with value ([^\\s]+)$", super::assertHeader);
        And("^new resource location in Location header$", super::assertLocationHeaderPointsToNewResource);
        And("^the response body is empty$", super::assertResponseBodyIsEmpty);
        And("^the response body contains resource matching ([^\\s]+)$", super::assertResponseBodyMatchesFixture);
        And("^the response body contains jsonpaths matching$", super::assertResponseBodyContainsJsonpathMatching);
        And("^the response body contains an empty list of payments$", super::assertResponseBodyContainsAnEmptyListOfPayments);
        And("^the response body contains a list of payments matching ([^\\s]+)$", super::assertResponseBodyContainsPayments);
        And("^the response body contains bad request description$", super::assertResponseBodyContainsBadRequestDescription);
        And("^the response body contains validation error description$", super::assertResponseBodyContainsValidationErrorDescription);

        And("^the interaction is documented as ([^\\s]+)$", super::documentInteraction);
    }

}
