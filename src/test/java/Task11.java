import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Arrays;
import java.util.List;

public class Task11 extends Testt {
    public Task11() {
        baseURL = "http://localhost/litecart/en/";
    }

    @Test
    public void createNewCustomer() {
        Select country_code;
        Select zone_code;
        int i = 0;
        List<String> values = Arrays.asList(new String[]{
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

        driver.get(baseURL);
        driver.findElement(By.id("box-account-login")).findElement(By.linkText("New customers click here")).click();
        country_code = new Select(driver.findElement(By.name("customer_form")).findElement(By.name("country_code")));
        country_code.selectByVisibleText("United States");

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
            zone_code = new Select(driver.findElement(By.name("customer_form")).findElement(By.cssSelector("select[name=zone_code]")));
            zone_code.selectByVisibleText("District of Columbia");
            driver.findElement(By.name("customer_form")).findElement(By.name("password")).sendKeys(values.get(10));
            driver.findElement(By.name("customer_form")).findElement(By.name("confirmed_password")).sendKeys(values.get(10));
            driver.findElement(By.cssSelector("button[name=create_account]")).click();
        } else {
            zone_code = new Select(driver.findElement(By.name("customer_form")).findElement(By.cssSelector("select[name=zone_code]")));
            zone_code.selectByVisibleText("District of Columbia");
            driver.findElement(By.cssSelector("button[name=create_account]")).click();
        }

        driver.findElement(By.id("box-account")).findElement(By.linkText("Logout")).click();
        driver.findElement(By.id("box-account-login")).findElement(By.name("email")).sendKeys(values.get(8));
        driver.findElement(By.id("box-account-login")).findElement(By.name("password")).sendKeys(values.get(10));
        driver.findElement(By.id("box-account-login")).findElement(By.cssSelector("button[name=login]")).click();
        driver.findElement(By.id("box-account")).findElement(By.linkText("Logout")).click();

    }

    @Override
    public void stop() {
    }
}
