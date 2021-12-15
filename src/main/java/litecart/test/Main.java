package litecart.test;

import helpers.Helper;
import litecart.test.pages.IndexPage;
import litecart.test.pages.LoginPage;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Main extends AppInterface {

    public Main(String baseURL) {
        this.baseURL = baseURL;
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        login = new LoginPage("user", "pass", "login", "logout");
    }

    public Main(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public Main(LoginInterface login, WebDriver driver, WebDriverWait wait, String baseURL) {
        this.login = login;
        this.driver = driver;
        this.wait = wait;
        this.baseURL = baseURL;
    }

    public Main() {
        baseURL = "http://localhost/litecart/";
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        login = new LoginPage("user", "pass", "login", "logout");
    }

    public void login(String user, String pass) {
        driver.get(baseURL);
        driver.findElement(login.getUsernameField()).sendKeys(user);
        driver.findElement(login.getPasswordField()).sendKeys(pass);
        driver.findElement(login.getLoginBtn()).click();
    }

    public void logout() {
        if (!login.isLogined()) { driver.findElement(login.getLogoutBtn()).click();}
    }

    public IndexPage open() {
        driver.get(baseURL);
        return new IndexPage(this);
    }




}
