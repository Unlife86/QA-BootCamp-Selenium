package litecart.pages;

import litecart.AppInterface;
import litecart.PageInterface;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends PageInterface {

    public CartPage(AppInterface app) {
        super(app);
    }

    private By table = By.cssSelector("table.dataTable");

    private By removeCartItem = By.cssSelector("[name='remove_cart_item']");

    private By quantity = By.cssSelector(".dataTable tr:not(.header):not(.footer) > td[style='text-align: center;']");

    public By getTable() {
        return table;
    }

    public void setTable(By table) {
        this.table = table;
    }

    public Integer getCount() {
        Integer count = 0;
        //List<WebElement> tds = app.getElements(quantity);
        for (WebElement td : app.getElements(quantity)) {
            count += Integer.parseInt(td.getText());
        }
        return count;
    }

    public By getRemoveCartItemBtn() {
        return removeCartItem;
    }

    public void setRemoveCartItemBtn(By removeCartItem) {
        this.removeCartItem = removeCartItem;
    }

    public void removeProduct() {
        app.getWait().until(ExpectedConditions.elementToBeClickable(app.getElement(getRemoveCartItemBtn()))).click();
    }

    public Boolean isTableLocatedAndVisible() {
        return app.visibilityOfElementLocated(getTable());
    }

    public Boolean tableIsStalenessOf() {
        return app.stalenessOf(app.getElement(getTable()));
    }


}
