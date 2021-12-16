package litecart.interfaces;

import helpers.Helper;
import litecart.pages.IndexPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement;

public abstract class AppInterface {

    protected LoginInterface login;
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseURL;



    public abstract void login(String user, String pass);

    public abstract void logout();

    public abstract IndexPage open();

    public WebDriver getDriver() {return this.driver;}

    public WebDriverWait getWait() {return this.wait;}

    public List<WebElement> getElements(By by) {
        return driver.findElements(by);
    }

    public WebElement getElement(By by) {
        return driver.findElement(by);
    }

    public WebElement getElement(WebElement root, By by) {
        return root.findElement(by);
    }

    public WebElement getRandomElementBy(List<WebElement> elements) {
        return elements.get(Helper._getRandomNumber(0,elements.size()));
    }

    public Boolean visibilityOfElementLocated(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by)) != null;
    }

    public void alertIsPresentAccept() {
        wait.until(alertIsPresent()).accept();
    }

    public Boolean stalenessOf(WebElement element) {
        return wait.until(ExpectedConditions.stalenessOf(element));
    }

    public Boolean textToBePresentInElement (WebElement element, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public void stop() {
        driver.quit();
        driver = null;
    }
}
