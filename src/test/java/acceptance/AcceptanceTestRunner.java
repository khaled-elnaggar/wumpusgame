package acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:shoot.feature", "classpath:move.feature"},
        plugin = {"pretty"},
        snippets = CucumberOptions.SnippetType.CAMELCASE)
public class AcceptanceTestRunner {
}


