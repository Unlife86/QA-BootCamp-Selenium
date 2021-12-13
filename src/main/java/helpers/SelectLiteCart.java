package helpers;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class SelectLiteCart extends Select {

    public SelectLiteCart(WebElement element) {
        super(element);
    }

    private String js = "arguments[0].selectedIndex = %s; arguments[0].dispatchEvent(new Event('change'))";

    private int _getIndexOptionByText(List<WebElement> options, String text) {
        int i = 0;
        for (WebElement option : options) {
            if (text.equals(option.getText())) {
                return i;
            } else {
                i++;
            }
        }
        return -1;
    }

    public void _setOption(WebDriver driver, WebElement element, String optionText) {
        _setOption(driver, element,String.format(js,_getIndexOptionByText(this.getOptions(),optionText)));
    }

    public void _setOption(WebDriver driver, WebElement element) {
        _setOption(driver, element,Helper._getRandomNumber(1,this.getOptions().size()) - 1);
    }

    public void _setOption(WebDriver driver, WebElement element, Integer index) {
        try {
            this.selectByIndex(index);
        } catch (ElementNotInteractableException e) {
            ((JavascriptExecutor) driver).executeScript(
                    String.format(js,index),
                    this
            );
        }
    }
}
