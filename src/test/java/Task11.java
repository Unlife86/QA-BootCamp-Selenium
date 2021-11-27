import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class Task11 extends Testt {
    private List<String> values = Arrays.asList(new String[]{
            "TaxID",
            "My Company",
            "MyFirstName",
            "MyLastName",
            "My Address1",
            "My Address2",
            "12345",
            "My City",
            "test@test.com",
            "+19876543214",
            "12345"
    });
    public Task11() {
        baseURL = "http://localhost/litecart/en/";
    }

    private int _getIndexOptionByText(List<WebElement> options, String text) {
        int i = 0;
        for (WebElement option : options) {
            if (text.equals(option.getText())) {
                return i;
            } else {
                i++;
            }
        }
        return -1;
    }

    private void _setOption(WebElement element, String optionText) {
        Select select;
        select = new Select(element);
        String js = "arguments[0].selectedIndex = " + _getIndexOptionByText(select.getOptions(),optionText) + "; arguments[0].dispatchEvent(new Event('change'))";
        try {
            select.selectByVisibleText(optionText);
        } catch (ElementNotInteractableException e) {
            ((JavascriptExecutor) driver).executeScript(js,select);
        }
    }

    @Override
    @BeforeAll
    public void start() {
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void createNewCustomer() {
        Select country_code;
        Select zone_code;
        int i = 0;

        driver.get(baseURL);
        driver.findElement(By.id("box-account-login")).findElement(By.linkText("New customers click here")).click();
        _setOption(driver.findElement(By.name("customer_form")).findElement(By.name("country_code")),"United States");

        for (WebElement input : driver.findElement(By.name("customer_form")).findElements(By.cssSelector("input:not([type=hidden])"))) {
            if (!"checkbox".equals(input.getAttribute("type"))) {
                input.clear();
            }
            switch (input.getAttribute("type")) {
                case "text":
                    input.sendKeys(values.get(i));
                    i++;
                    break;
                case "email":
                    input.sendKeys(values.get(8));
                    break;
                case "tel":
                    input.sendKeys(values.get(9));
                    break;
                case "password":
                    input.sendKeys(values.get(10));
                    break;
                case "checkbox":
                    input.click();
                    break;
                default:
                    break;
            }
        }

        if ("true".equals(driver.findElement(By.name("customer_form")).findElement(By.cssSelector("select[name=zone_code]")).getAttribute("disabled"))) {
            driver.findElement(By.cssSelector("button[name=create_account]")).click();
            _setOption(driver.findElement(By.name("customer_form")).findElement(By.cssSelector("select[name=zone_code]")),"District of Columbia");
            driver.findElement(By.name("customer_form")).findElement(By.name("password")).sendKeys(values.get(10));
            driver.findElement(By.name("customer_form")).findElement(By.name("confirmed_password")).sendKeys(values.get(10));

        } else {
            _setOption(driver.findElement(By.name("customer_form")).findElement(By.cssSelector("select[name=zone_code]")),"District of Columbia");
        }
        driver.findElement(By.cssSelector("button[name=create_account]")).click();

        driver.findElement(By.id("box-account")).findElement(By.linkText("Logout")).click();
        driver.findElement(By.id("box-account-login")).findElement(By.name("email")).sendKeys(values.get(8));
        driver.findElement(By.id("box-account-login")).findElement(By.name("password")).sendKeys(values.get(10));
        driver.findElement(By.id("box-account-login")).findElement(By.cssSelector("button[name=login]")).click();
        driver.findElement(By.id("box-account")).findElement(By.linkText("Logout")).click();

    }

    @Override
    @AfterAll
    public void stop() {
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        driver.findElement(By.id("box-apps-menu")).findElement(By.linkText("Customers")).click();
        driver.findElement(By.name("customers_form")).findElement(By.linkText(values.get(2) + " " + values.get(3))).click();
        driver.findElement(By.name("customer_form")).findElement(By.name("delete")).click();
        driver.switchTo().alert().accept();
        super.stop();
    }
}
