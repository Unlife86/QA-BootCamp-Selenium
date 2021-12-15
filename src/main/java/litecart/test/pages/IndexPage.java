package litecart.test.pages;

import litecart.test.AppInterface;
import litecart.test.PageInterface;
import org.openqa.selenium.By;

public class IndexPage extends PageInterface {

    public IndexPage(AppInterface app) {
        super(app);
    }

    public ProductPage goToProductPage() {
        app.getElement(
                app.getRandomElementBy(app.getElements(By.cssSelector(".product"))),
                By.cssSelector("a.link")
        ).click();
        return new ProductPage(app);
    }

    public CartPage goToCartPage() {
        app.getElement(By.cssSelector("#cart .link")).click();
        return new CartPage(app);
    }

}
