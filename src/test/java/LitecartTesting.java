import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private DynamicTest _factory(final String str) {
        String h1 = driver.findElement(By.cssSelector("#content h1")).getText();
        return DynamicTest.dynamicTest(str, () -> assertEquals(str, h1));
    }

    @BeforeAll
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        litecart = "http://localhost/litecart/admin/";
    }

    @Test
    @DisplayName("Task3: Sign on")
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
    @DisplayName("Task7: Sections of the administrative panel")
    class Task7 {
        int menuCount;
        WebElement root;

        @Test
        @DisplayName("Count item")
        public void MenuSize() {
            root = driver.findElement(By.id("box-apps-menu"));
            menuCount = root.findElements(By.id("app-")).size();
            assertTrue(menuCount == 17);
        }

        @TestFactory
        @DisplayName("Links")
        public Collection<DynamicTest> Links() {
            int countI; int countJ;
            String text;
            List<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();
            countI = driver.findElements(By.cssSelector("#box-apps-menu li#app- > a")).size();
            if (countI > 0) {
                for (int i = 1; i <= countI; i++) {
                    driver.findElement(By.cssSelector("#box-apps-menu li#app-:nth-child(" + i + ") > a")).click();
                    countJ = driver.findElements(By.cssSelector("#box-apps-menu li#app-:nth-child(" + i + ") > ul.docs li[id*=doc-]:not(.selected) > a")).size();
                    if (countJ > 0) {
                        dynamicTests.add(_factory(driver.findElement(By.cssSelector("#box-apps-menu li#app-:nth-child(" + i + ") > ul.docs > li[id*=doc-].selected > a")).getText()));
                        for (int j = 2; j <= countJ + 1; j++) {
                            driver.findElement(By.cssSelector("#box-apps-menu li#app-:nth-child(" + i + ") > ul.docs li[id*=doc-]:not(.selected):nth-child(" + j + ") > a")).click();
                            dynamicTests.add(_factory(driver.findElement(By.cssSelector("#box-apps-menu li#app-:nth-child(" + i + ") > ul.docs li[id*=doc-].selected > a")).getText()));
                        }
                    } else {
                        text = driver.findElement(By.cssSelector("#box-apps-menu li#app-:nth-child(" + i + ") > a")).getText();
                        dynamicTests.add(_factory(text));
                    }

                }
                return dynamicTests;
            }
            dynamicTests.add(DynamicTest.dynamicTest("No such element", () -> assertTrue(false)));
            return dynamicTests;
        }

    }

    @RepeatedTest(value = 11, name = "{displayName} {currentRepetition} of {totalRepetitions} has sticker") //"#main > .middle > .content div[id^=box-].box ul.listing-wrapper.products > li.product.column.shadow.hover-light div.sticker.new"
    @DisplayName("Product")
    public void Task8(RepetitionInfo repetitionInfo) {
        int i = repetitionInfo.getCurrentRepetition();
        litecart = "http://localhost/litecart/en/";
        driver.get(litecart);
        List<WebElement> items = driver.findElements(By.cssSelector("#main > .middle > .content div[id^=box-].box  li.product.column.shadow.hover-light"));
        assertEquals(1, items.get(i-1).findElements(By.className("sticker")).size());
    }

    @AfterAll
    public void stop() {
        driver.quit();
        driver = null;
    }
}

