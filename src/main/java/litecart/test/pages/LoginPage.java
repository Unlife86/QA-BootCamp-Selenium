package litecart.test.pages;

import litecart.test.LoginInterface;
import litecart.test.PageInterface;
import org.openqa.selenium.By;

public class LoginPage implements LoginInterface {

    private String username;
    private String password;
    private String login;
    private String logout;


    public LoginPage(String username, String password, String login, String logout) {
        this.username = username;
        this.password = password;
        this.login = login;
        this.logout = logout;
    }

    public By getUsernameField() { return By.cssSelector(username); }

    public By getPasswordField() { return By.cssSelector(password); }

    public By getLoginBtn() { return By.cssSelector(login); }

    public By getLogoutBtn() { return By.cssSelector(logout); }

    public Boolean isLogined() {return true; }

}
