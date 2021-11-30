import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Task14 extends Testt {
    String handle;
    private void _go() {
        driver.get(baseURL);
        if (driver.getCurrentUrl().contains("http://localhost/litecart/admin/login.php?")) {
            driver.findElement(By.name("username")).sendKeys("admin");
            driver.findElement(By.name("password")).sendKeys("admin");
            driver.findElement(By.name("login")).click();
        }
    }

    private ExpectedCondition<String> _getCreatedHandle(Set<String> handles) {
        return  new ExpectedCondition<String>() {
            @Override
            public String apply(WebDriver driver) {
                Set<String> hhandles = driver.getWindowHandles();
                hhandles.removeAll(handles);
                return hhandles.size() > 0 ? hhandles.iterator().next() : null;
            }
        };
    }

    private String _getDisplay(WebElement element) {
        if ("Code".equals(element.findElement(By.xpath("./..")).findElement(By.tagName("strong")).getText())) {
            return element.findElement(By.xpath("./..")).getText();
        }
        return element.findElement(By.xpath("./..")).findElement(By.tagName("strong")).getText();
    }

    @TestFactory
    public Collection<DynamicTest> openLinkNewWindow() {
        List<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();
        _go();
        driver.findElement(By.xpath(".//ul[@id='box-apps-menu']")).findElement(By.linkText("Countries")).click();
        driver.findElement(By.name("countries_form")).findElement(By.cssSelector("a[title=Edit]:first-child")).click();
        handle = driver.getWindowHandle();
        for (WebElement blank : driver.findElements(By.xpath("//form//table//a[@target='_blank']"))) {
            dynamicTests.add(DynamicTest.dynamicTest(
                    _getDisplay(blank),
                    () -> {
                        Set<String> handles = driver.getWindowHandles();
                        blank.click();
                        String newHandle = wait.until(_getCreatedHandle(handles));
                        driver.switchTo().window(newHandle);
                        assertEquals(handles.size()+1, driver.getWindowHandles().size());
                        if (driver.getWindowHandles().size() > 1) {
                            driver.close();
                            driver.switchTo().window(handle);
                        } else {
                            driver.navigate().back();
                        }

                    }
            ));
        }
        return  dynamicTests;

    }


}
