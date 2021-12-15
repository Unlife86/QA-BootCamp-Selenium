package litecart.interfaces;

public abstract class PageInterface {

    protected AppInterface app;

    public PageInterface(AppInterface app) {
        this.app = app;
    }

}
