import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;

public class Task17 extends Testt {
    private String url = "http://localhost/litecart/admin/?app=catalog&doc=catalog&category_id=1";

    private Boolean isLogined() {
        try {
            wait.until((WebDriver d) -> driver.findElement(By.id("box-login")));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Test
    @DisplayName("Login")
    public void canLogin() {
        _login();
        assertEquals(0,driver.manage().logs().get("browser").getAll().size());
    }

    @Test
    @DisplayName("The category page is open")
    public void goToCategory() {
        driver.get(url);
        if (isLogined()) {
            driver.findElement(By.name("username")).sendKeys("admin");
            driver.findElement(By.name("password")).sendKeys("admin");
            driver.findElement(By.name("login")).click();
        }
        assertEquals(0,driver.manage().logs().get("browser").getAll().size());
    }

    private DynamicTest _noLogActionProduct(WebElement product) {
        String text = product.getText();
        product.click();
        url = driver.getCurrentUrl();
        return DynamicTest.dynamicTest(
                text + ": No Logs",
                () -> assertEquals(0,driver.manage().logs().get("browser").getAll().size())
        );
    }

    @TestFactory
    public Collection<DynamicTest> clickOnProducts() {
        List<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();
        String xpath = "[not(self::node()[@title='Edit'])]/../../following-sibling::*[1]/self::tr//a[contains(@href,'edit_product')][not(self::node()[@title='Edit'])]";
        WebElement product;
        List<WebElement> products;
        if (!driver.getCurrentUrl().equals(url)) {
            driver.get(url);
        }
        if (isLogined()) {
            driver.findElement(By.name("username")).sendKeys("admin");
            driver.findElement(By.name("password")).sendKeys("admin");
            driver.findElement(By.name("login")).click();
        }
        products = driver.findElement(By.className("dataTable")).findElements(By.cssSelector("a[href*=edit_product]:not([title=Edit])"));
        if (products.size() > 0) {
            product = products.get(0);
            dynamicTests.add(_noLogActionProduct(product));
        } else {
            return dynamicTests;
        }
        while (product != null) {
            driver.navigate().back();
            try {
                product = driver.findElement(By.className("dataTable")).findElement(By.xpath(
                        String.format(".//a[@href='%s']%s",url,xpath)
                ));
                dynamicTests.add(_noLogActionProduct(product));
            } catch (NoSuchElementException e) {
                return dynamicTests;
            }
        }
        return dynamicTests;
    }
}
