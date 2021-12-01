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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Task13 extends Testt {
    public Task13() {
        baseURL = "http://localhost/litecart/en/";
    }

    private By table = By.cssSelector("table.dataTable") ;

    private int _getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void _goToProductPage() {
        _goToProductPage(_getRandomNumber(0,driver.findElements(By.cssSelector(".product")).size()));
    }

    private void _goToProductPage(int i) {
        _goToProductPage(driver.findElements(By.cssSelector(".product")).get(i));
    }

    private void _goToProductPage(WebElement product) {
        product.findElement(By.cssSelector("a.link")).click();
    }

    private void _cartAddProduct() {
        Select options_size;
        try {
            options_size = new Select(driver.findElement(By.name("options[Size]")));
            options_size.selectByIndex(_getRandomNumber(
                    1,
                    driver.findElement(By.name("options[Size]")).findElements(By.cssSelector("option:not(option[selected=selected])")).size()
            ));
        } catch (NoSuchElementException e) {
        } finally {
            driver.findElement(By.name("add_cart_product")).click();
            wait.until(alertIsPresent()).accept();
        }
    }

    @RepeatedTest(value = 3, name= "{displayName}: {currentRepetition} of {totalRepetitions}")
    @DisplayName("Attempt to add a product")
    @Order(1)
    public void cartAddProduct(RepetitionInfo repetitionInfo) {
        driver.get(baseURL);
        _goToProductPage();
        _cartAddProduct();
        assertTrue(wait.until(textToBePresentInElement(
                driver.findElement(By.cssSelector("#cart .content .quantity")),
                String.valueOf(repetitionInfo.getCurrentRepetition())
        )));
    }


    @TestFactory
    @Order(2)
    public Collection<DynamicTest> cartDelProduct() {
        List<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();
        driver.get(baseURL);
        int count = Integer.parseInt(driver.findElement(By.cssSelector("#cart .content .quantity")).getText());
        if (count == 0) {
            for (int i = 0; i < 3; i++) {
                _goToProductPage();
                _cartAddProduct();
                driver.navigate().back();
            }
            count = Integer.parseInt(driver.findElement(By.cssSelector("#cart .content .quantity")).getText());
        }
        driver.findElement(By.cssSelector("#cart .link")).click();

        int i = 1;
        while (count - i > 0) {
            WebElement table = driver.findElement(this.table);
            driver.findElement(By.name("remove_cart_item")).click();
            if (wait.until(stalenessOf(table))) {
                dynamicTests.add(DynamicTest.dynamicTest(
                        String.format("Attempt to delete a product %s of %s",i,count),
                        () -> assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(this.table)) instanceof WebElement)
                ));
            } else {
                dynamicTests.add(DynamicTest.dynamicTest(
                        String.format("Attempt to delete a product %s of %s",i,count),
                        () -> assertTrue(false)
                ));
            }
            i++;
        }
        driver.findElement(By.name("remove_cart_item")).click();
        dynamicTests.add(DynamicTest.dynamicTest(
                String.format("Attempt to delete a product %s of %s",count,count),
                () -> assertTrue(wait.until(stalenessOf(driver.findElement(this.table))))
        ));
        return dynamicTests;
    }
}
