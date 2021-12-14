package litecart.test;

import org.openqa.selenium.WebDriver;

public abstract class PageInterface {

    protected AppInterface app;

    public PageInterface(AppInterface app) {
        this.app = app;
    }

}
