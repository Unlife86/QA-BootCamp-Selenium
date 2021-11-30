import org.junit.jupiter.api.*;
import org.junit.jupiter.api.ClassOrderer.OrderAnnotation;
import org.junit.jupiter.api.RepetitionInfo;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Task13 extends Testt {
    public Task13() {
        baseURL = "http://localhost/litecart/en/";
    }
    private int _getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @RepeatedTest(value = 3, name= "{displayName}: {currentRepetition} of {totalRepetitions}")
    @DisplayName("Attempt to add a product")
    @Order(1)
    public void cartAddProduct(RepetitionInfo repetitionInfo) {
        Select options_size;
        Alert alert;
        driver.get(baseURL);
        List<WebElement> products = driver.findElements(By.cssSelector(".product"));
        products.get(_getRandomNumber(0,products.size())).findElement(By.cssSelector("a.link")).click();
        try {
            options_size = new Select(driver.findElement(By.name("options[Size]")));
            options_size.selectByIndex(_getRandomNumber(
                    1,
                    driver.findElement(By.name("options[Size]")).findElements(By.cssSelector("option:not(option[selected=selected])")).size()
            ));
        } catch (NoSuchElementException e) {
        } finally {
            driver.findElement(By.name("add_cart_product")).click();
            alert = wait.until(alertIsPresent());
            if (alert != null) {
                alert.accept();
                driver.navigate().refresh();
                assertEquals(repetitionInfo.getCurrentRepetition(),Integer.parseInt(driver.findElement(By.cssSelector("#cart .content .quantity")).getText()));
            } else {
                assertTrue(wait.until(textToBePresentInElement(
                        driver.findElement(By.cssSelector("#cart .content .quantity")),
                        String.valueOf(repetitionInfo.getCurrentRepetition())
                )));
            }

        }


    }

    @RepeatedTest(value = 3, name= "{displayName}: {currentRepetition} of {totalRepetitions}")
    @DisplayName("Attempt to delete a product")
    @Order(2)
    public void cartDelProduct(RepetitionInfo repetitionInfo) {
        WebElement table;
        if (repetitionInfo.getCurrentRepetition() == 1) {
            driver.get(baseURL);
            if (Integer.parseInt(driver.findElement(By.cssSelector("#cart .content .quantity")).getText()) == 0) {
                assertTrue(false);
            }
            driver.findElement(By.cssSelector("#cart .link")).click();
        }
        table = driver.findElement(By.cssSelector("table.dataTable"));
        driver.findElement(By.name("remove_cart_item")).click();
        if (wait.until(stalenessOf(table))) {
            table = null;
            table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.dataTable")));
            if (table == null) {
                table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath(".//*[@id='checkout-cart-wrapper']//em")
                ));
            }
            assertTrue(table instanceof WebElement);
        } else {
            assertTrue(false);
        }
    }
}
