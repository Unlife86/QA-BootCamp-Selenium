import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Task12 extends Testt {
    private Map<String,Map<String,Map<String,String>>> tabsFields = new HashMap<String,Map<String,Map<String,String>>>();

    public Task12() throws FileNotFoundException, IOException {
        baseURL = "http://localhost/litecart/admin/?app=catalog&doc=catalog";
        //_gson("General", "src/test/resources/addNewPoduct.json");
        for (String tab : Arrays.asList(new String[]{"General","Information", "Prices"})) {
            tabsFields.put(tab,_gson(tab, "src/test/resources/addNewPoduct.json"));
        }
    }

    @Override
    @BeforeAll
    public void start() {
        //super.start();
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(baseURL);
        if (driver.getCurrentUrl().contains("http://localhost/litecart/admin/login.php?")) {
            driver.findElement(By.name("username")).sendKeys("admin");
            driver.findElement(By.name("password")).sendKeys("admin");
            driver.findElement(By.name("login")).click();
        }
    }

    @Test
    public void canGoAddProductPage() {
        for (WebElement a : driver.findElement(By.id("content")).findElement(By.cssSelector("div[style='float: right;']")).findElements(By.tagName("a"))) {
            if (a.getText().equals("Add New Product")) {
                a.click();
                break;
            }
        }
        assertTrue(wait.until(titleContains("Add New Product | My Store")));
        _fillFields();
        driver.findElement(By.name("save")).click();
    }
    /*String nameOfClass = stringClassName;
    Class classForName = Class.forName(nameOfClass);
    TargetClass targetClassObject = (TargetClass) classForName.newInstance();*/

    private void _fillFields() {
        WebElement a;
        WebElement tab;
        WebElement root = driver.findElement(By.cssSelector("div.tabs"));
        root = root.findElement(By.cssSelector("ul.index"));
        for (Map.Entry<String, Map<String,Map<String,String>>> entry : tabsFields.entrySet()) {
            a = root.findElement(By.linkText(entry.getKey()));
            a.click();
            wait.until(ExpectedConditions.attributeToBe(
                    By.id( a.getAttribute("href").substring(a.getAttribute("href").indexOf("#") + 1)),
                    "display",
                    "block"
            ));
            if (entry.getKey().equals("Prices")) {
                tab = driver.findElement(By.id("tab-prices"));
                tab.findElement(By.name("prices[USD]")).sendKeys("17.0000");
                tab.findElement(By.name("prices[EUR]")).sendKeys("6.0000");
            }
            if (entry.getValue() != null) {
                for (WebElement strong : driver.findElements(By.xpath( entry.getValue().get("root").get("xpath") ))) {
                    _actionElement(strong,entry.getValue().get(strong.getText()));
                }
            }
        }
    }

    protected void _actionElement(WebElement root, Map<String,String> map) {
        By by = By.xpath(map.get("xpath"));
        switch (root.getText()) {
            case "Status":
                for (WebElement label : root.findElements(by)) {
                    if (label.getText().equals(map.get("value"))) {
                        label.findElement(By.tagName("input")).click();
                        break;
                    }
                }
                break;
            case "Categories":
                root.findElement(By.xpath("..//input[@data-name='Root']")).click();
                root.findElement(by).click();
                break;
            case "Product Groups":
                List<WebElement> genders = root.findElements(by);
                for (WebElement gender : genders) {
                    if (gender.getAttribute("value").equals("1-"+ _getRandomNumber(1,genders.size()))) {
                        gender.click();
                    }
                }
                break;
            default:
                for (WebElement element : root.findElements(by)) {
                    if (element.getTagName().equals("select")) {
                        _setOption(element);
                    } else if (element.getTagName().equals("input")) {
                        switch (element.getAttribute("type")) {
                            case "file":
                                element.sendKeys((new File(map.get("value"))).getAbsolutePath());
                                break;
                            case "date":
                                ((JavascriptExecutor) driver).executeScript(String.format(
                                        map.get("js"),
                                        LocalDate.now().plusDays(Integer.parseInt(map.get("value"))).toString()
                                ));
                                break;
                            default:
                                element.sendKeys(map.get("value"));
                                break;
                        }
                    }

                }
        }

    }






}
