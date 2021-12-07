import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Task12 extends Testt {
    private Map<String,Map<String,Map<String,String>>> tabsFields = new HashMap<String,Map<String,Map<String,String>>>();

    public Task12() throws FileNotFoundException, IOException {
        baseURL = "http://localhost/litecart/admin/?app=catalog&doc=catalog";
        //_gson("General", "src/test/resources/addNewPoduct.json");
        for (String tab : Arrays.asList(new String[]{"General"/*,"Information", "Prices"*/})) {
            tabsFields.put(tab,_gson(tab, "src/test/resources/addNewPoduct.json"));
        }
    }

    @Override
    @BeforeAll
    public void start() {
        super.start();
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
    }

    private void _fillFields() {
        WebElement field;
        WebElement root = driver.findElement(By.cssSelector("div.tabs"));
        root = root.findElement(By.cssSelector("ul.index"));
        for (Map.Entry<String, Map<String,Map<String,String>>> entry : tabsFields.entrySet()) {
            root.findElement(By.linkText(entry.getKey())).click();
            if (entry.getValue() != null) {
                for (WebElement strong : driver.findElements(By.xpath( entry.getValue().get("root").get("xpath") ))) {
                    _actionElement(strong,entry.getValue().get(strong.getText()));
                }
            }
        }
    }






}
