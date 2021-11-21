import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static  org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

@TestInstance(Lifecycle.PER_CLASS)
public class Task1 {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void Task3() {
        driver.get("https://search.maven.org/");
        wait.until(titleIs("Maven Central Repository Search"));
    }

    @AfterAll
    public void stop() {
        driver.quit();
        driver = null;
    }
}
