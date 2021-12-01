import com.google.gson.Gson;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Task11 extends Testt {
    private Map<String,String> values;
    private MockNeat mock = MockNeat.old();
    public Task11() {
        baseURL = "http://localhost/litecart/en/";
        values = (new Gson()).fromJson(
                String.format(
                        "{'company':'%s','firstname':'%s','lastname':'%s','address1':'%s','address2':'%s','postcode':'%s','city':'%s','email':'%s','phone':'%s','password':'%s','confirmed_password':'%s'}",
                        "My Company", //company
                        mock.names().first().get(),//firstname
                        mock.names().last().get(),//lastname
                        "Address1",//address1
                        "Address2",//address2
                        String.valueOf(mock.ints().range(10000,99999).get()),//postcode
                        "Rockville",//city
                        mock.emails().get(),//email
                        "+19876543214",//phone
                        "12345",//password
                        "12345"//confirmed_password


                ),
                Map.class
        );

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
    @DisplayName("User registration scenario")
    public void createNewCustomer() {
        Select country_code;
        Select zone_code;

        driver.get(baseURL);
        driver.findElement(By.id("box-account-login")).findElement(By.linkText("New customers click here")).click();
        _setOption(driver.findElement(By.name("customer_form")).findElement(By.name("country_code")),"United States");

        for (WebElement input : driver.findElement(By.name("customer_form")).findElements(By.cssSelector("input:not([type=hidden]):not([type=checkbox]):not([name=tax_id])"))) {
            input.clear();
            input.sendKeys(values.get(input.getAttribute("name")));
        }
        driver.findElement(By.name("newsletter")).click();

        if ("true".equals(driver.findElement(By.name("customer_form")).findElement(By.cssSelector("select[name=zone_code]")).getAttribute("disabled"))) {
            driver.findElement(By.cssSelector("button[name=create_account]")).click();
            _setOption(driver.findElement(By.name("customer_form")).findElement(By.cssSelector("select[name=zone_code]")),"District of Columbia");
            driver.findElement(By.name("customer_form")).findElement(By.name("password")).sendKeys(values.get("password"));
            driver.findElement(By.name("customer_form")).findElement(By.name("confirmed_password")).sendKeys(values.get("confirmed_password"));

        } else {
            _setOption(driver.findElement(By.name("customer_form")).findElement(By.cssSelector("select[name=zone_code]")),"District of Columbia");
        }
        driver.findElement(By.cssSelector("button[name=create_account]")).click();

        driver.findElement(By.id("box-account")).findElement(By.linkText("Logout")).click();
        driver.findElement(By.id("box-account-login")).findElement(By.name("email")).sendKeys(values.get("email"));
        driver.findElement(By.id("box-account-login")).findElement(By.name("password")).sendKeys(values.get("password"));
        driver.findElement(By.id("box-account-login")).findElement(By.cssSelector("button[name=login]")).click();
        driver.findElement(By.id("box-account")).findElement(By.linkText("Logout")).click();

    }
}
