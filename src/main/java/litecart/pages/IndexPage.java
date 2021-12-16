package litecart.pages;

import litecart.interfaces.AppInterface;
import litecart.interfaces.PageInterface;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class IndexPage extends PageInterface {

    public IndexPage(AppInterface app) {
        super(app);
    }

    private By product = By.cssSelector(".product");

    private By productName = By.cssSelector(".product div.name");

    public ProductPage goToProductPage() {
        app.getElement(
                app.getRandomElementBy(app.getElements(product)),
                By.cssSelector("a.link")
        ).click();
        return new ProductPage(app);
    }
    public ProductPage goToProductPageByName(String name) {
        WebElement product = app.getRandomElementBy(app.getElements(this.product));
        for (WebElement element : app.getElements(productName)) {
            if (element.getText().equals(name)) product = element;
        }
        product.click();
        return new ProductPage(app);
    }

    public CartPage goToCartPage() {
        app.getElement(By.cssSelector("#cart .link")).click();
        return new CartPage(app);
    }

}
