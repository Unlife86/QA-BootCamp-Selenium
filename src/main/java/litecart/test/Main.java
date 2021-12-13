package litecart.test;

import helpers.Helper;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Main {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseURL;

    public Main(String baseURL) {
        this.baseURL = baseURL;
    }

    public Main(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public Main() {
        baseURL = "http://localhost/litecart/";
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void goToPage(String url) {
        driver.get(url);
    }

    public void goToIndex() {
        goToPage(baseURL);
    }

    public List<WebElement> getElements(By by) {
        return driver.findElements(by);
    }

    public WebElement getRandomElementBy(List<WebElement> elements) {
        Integer bottom = elements.size();
        return elements.get(Helper._getRandomNumber(0,bottom));
    }







}
