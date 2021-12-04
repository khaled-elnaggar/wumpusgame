package acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:features"},
        snippets = CucumberOptions.SnippetType.CAMELCASE)
public class AcceptanceTestRunner {
}


