import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static  org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class Task1 {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void Task3() {
        driver.get("https://search.maven.org/");
        wait.until(titleIs("Maven Central Repository Search"));
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
