import helpers.DynamicTests;
import litecart.test.AppInterface;
import litecart.test.Main;
import litecart.test.pages.CartPage;
import litecart.test.pages.IndexPage;
import litecart.test.pages.ProductPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Task19 {

    AppInterface app;

    public Task19() {
        app = new Main();//"https://litecart.stqa.ru/en/");
    }

    @RepeatedTest(value = 3, name= "{displayName}: {currentRepetition} of {totalRepetitions}")
    @DisplayName("Attempt to add a product")
    @Order(1)
    public void cartAddProduct(RepetitionInfo repetitionInfo) {
        ProductPage productPage = app.open().goToProductPage();
        productPage.cartAddProduct();
        assertTrue(app.getWait().until(textToBePresentInElement(
                productPage.getCart(),
                String.valueOf(repetitionInfo.getCurrentRepetition())
        )));
    }

    @TestFactory
    @Order(2)
    public Collection<DynamicTest> cartDelProduct() {
        int i = 1;
        int count = 0;
        DynamicTests dynamicTests = new DynamicTests(app.getDriver());
        CartPage cartPage = app.open().goToCartPage();
        count = cartPage.getCount();
        while (cartPage.getCount() > 1) {
            cartPage.removeProduct();
            if (cartPage.tableIsStalenessOf()) {
                dynamicTests.dynamicTests(
                        String.format("Attempt to delete a product %s of %s",i,count),
                        () -> assertTrue(cartPage.isTableLocatedAndVisible())
                );
            } else {
                dynamicTests.dynamicTests(
                        String.format("Attempt to delete a product %s of %s",i,count),
                        () -> assertTrue(false)
                );
            }
            i++;
        }
        cartPage.removeProduct();
        dynamicTests.dynamicTests(
                String.format("Attempt to delete a product %s of %s",count,count),
                () -> assertTrue(cartPage.tableIsStalenessOf())
        );
        return dynamicTests.getDynamicTests();
    }

    @AfterAll
    public void stop() {
        app.stop();
    }

}
