package lt.rieske.payments.functional;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", strict = true, plugin = {"pretty", "html:build/cucumber"})
public class FunctionalTest {
}