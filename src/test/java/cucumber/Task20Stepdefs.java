package cucumber;

import io.cucumber.java8.En;
import io.cucumber.java8.Th;
import litecart.Main;
import litecart.interfaces.AppInterface;
import litecart.pages.CartPage;
import litecart.pages.IndexPage;
import litecart.pages.ProductPage;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Task20Stepdefs implements En {

    private static AppInterface app = new Main("https://litecart.stqa.ru/en/");

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {app.stop();}));
    }

    private ProductPage productPage;
    private IndexPage indexPage;
    private CartPage cartPage;

    public Task20Stepdefs() {
        Given("Open index page", () -> {
            indexPage = app.open();
        });
        When("Open random product page", () -> {
            productPage = indexPage.goToProductPage();
        });
        And("Add product to the cart",() -> {
            productPage.cartAddProduct();
        });
        Then("The number of products in the basket is {string}", (String count) -> {
            assertTrue(app.textToBePresentInElement(productPage.getCart(), count));
        });

        When("Open cart page", () -> {
            cartPage = indexPage.goToCartPage();
        });
        Then("Product count is {string}", (String count) -> {
            assertEquals(String.valueOf(cartPage.getCount()), count);
        });
        When("Delete one the product from the cart", () -> {
            cartPage.removeProduct();
        });
        Then("Table Located And Visible", () -> {
            assertTrue(cartPage.isTableLocatedAndVisible());
        });
        Then("Table Is Staleness Of", () -> {
            assertTrue(cartPage.tableIsStalenessOf());
        });
    }
}
