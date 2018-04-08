package lt.rieske.payments.functional;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", strict = true, plugin = {"pretty", "html:build/cucumber"})
public class FunctionalTest {
}