import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(Lifecycle.PER_CLASS)
public class LitecartTesting {
    private WebDriver driver;
    private WebDriverWait wait;
    private String litecart;

    private void _login() {
        driver.get(litecart);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    private boolean _isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @BeforeAll
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        litecart = "http://localhost/litecart/admin/";
    }

    @Test
    public void Task3() {
        String text = " You are now logged in as admin";
        Boolean result = false;
        _login();
        if (_isElementPresent(By.cssSelector("div.notice.success"))) {
            if (text.equals(driver.findElement(By.cssSelector("div.notice.success")).getText())) {
                result = true;
            }
        } else {
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("Menu")
    class Menu {
        int menuCount;
        WebElement root;

        @Test
        @DisplayName("Count item")
        public void MenuSize() {
            root = driver.findElement(By.id("box-apps-menu"));
            menuCount = root.findElements(By.id("app-")).size();
            assertTrue(menuCount == 17);
        }

        @RepeatedTest(value = 17, name = "{displayName} {currentRepetition} of {totalRepetitions}")
        @DisplayName("Link action")
        public void Links(RepetitionInfo repetitionInfo) {
            root = driver.findElement(By.id("box-apps-menu"));
            try {
                root.findElement(By.cssSelector("li#app-:nth-child(" + repetitionInfo.getCurrentRepetition() + ") > a")).click();
            } catch (NoSuchElementException e){
                System.out.println("root.getTagName()");
            }

        }

    }

    @AfterAll
    public void stop() {
        driver.quit();
        driver = null;
    }
}

