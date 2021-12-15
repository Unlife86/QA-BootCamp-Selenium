package litecart.interfaces;

import org.openqa.selenium.By;

public interface LoginInterface {

    public By getUsernameField();

    public By getPasswordField();

    public By getLoginBtn();

    public By getLogoutBtn();

    public Boolean isLogined();

}
