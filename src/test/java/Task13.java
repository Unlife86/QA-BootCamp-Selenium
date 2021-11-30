import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class Task13 extends Testt {
    public Task13() {
        baseURL = "http://localhost/litecart/en/";
    }
    private int _getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @RepeatedTest(value = 3, name= "{displayName}: {currentRepetition} of {totalRepetitions}")
    @DisplayName("Attempt to add a product")
    public void cartAddProduct(RepetitionInfo repetitionInfo) {
        Select options_size;
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
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
            driver.navigate().refresh();
            assertEquals(repetitionInfo.getCurrentRepetition(),Integer.parseInt(driver.findElement(By.cssSelector("#cart .content .quantity")).getText()));
        }


    }
    @RepeatedTest(value = 3, name= "{displayName}: {currentRepetition} of {totalRepetitions}")
    @DisplayName("Attempt to delete a product")
    public void cartDelProduct() {
        driver.get(baseURL);
        if (Integer.parseInt(driver.findElement(By.cssSelector("#cart .content .quantity")).getText()) == 3) {
            assertTrue(false);
        }
        driver.findElement(By.cssSelector("#cart .link")).click();

    }
}
