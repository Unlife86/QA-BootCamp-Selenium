import litecart.test.AppInterface;
import litecart.test.Main;
import litecart.test.pages.CartPage;
import litecart.test.pages.IndexPage;
import litecart.test.pages.ProductPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.CacheLookup;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Task19 {

    AppInterface app;

    CartPage cart;

    public Task19() {
        app = new Main("https://litecart.stqa.ru/en/");
        //cart = new CartPage(app);

    }

    @RepeatedTest(value = 3, name= "{displayName}: {currentRepetition} of {totalRepetitions}")
    @DisplayName("Attempt to add a product")
    @Order(1)
    public void cartAddProduct(RepetitionInfo repetitionInfo) {
        ProductPage productPage = app.open().goToProductPage();
        assertTrue(app.getWait().until(textToBePresentInElement(
                productPage.getCart(),
                String.valueOf(repetitionInfo.getCurrentRepetition())
        )));

        cart = new CartPage(app);
    }

    @AfterAll
    public void stop() {
        app.stop();
    }

}
