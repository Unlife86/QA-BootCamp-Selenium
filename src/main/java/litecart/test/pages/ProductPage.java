package litecart.test.pages;

import helpers.SelectLiteCart;
import litecart.test.AppInterface;
import litecart.test.PageInterface;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class ProductPage extends PageInterface {

    public ProductPage(AppInterface app) {
        super(app);
        hasOptions = app.getElements(select).size() > 0;
    }

    private Boolean hasOptions;

    private By select = By.name("options[Size]");

    private By cart = By.cssSelector("#cart .content .quantity");

    private void _setOption() {
        if (hasOptions) {
            (new SelectLiteCart(app.getElement(select)))._setOption(app.getDriver());
        }
    }

    public void cartAddProduct() {
        try {
            _setOption();
        } catch (NoSuchElementException e) {
        } finally {
            app.getElement(By.name("add_cart_product")).click();
            app.alertIsPresentAccept();
            app.getDriver().navigate().refresh();
        }
    }

    public WebElement getCart() {
        return app.getElement(cart);
    }

    public Integer getCountInCart() {
        return Integer.parseInt(getCart().getText());
    }


}
