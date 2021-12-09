import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Testt {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseURL = "http://localhost/litecart/admin/";

    @BeforeAll
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterAll
    public void stop() {
        driver.quit();
        driver = null;
    }

    protected void _login() {
        driver.get(baseURL);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    protected Map<String,Map<String,String>> _gson(String key, String fileName) throws FileNotFoundException, IOException {
        Path path = Paths.get(fileName);
        JsonElement tree = null;
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonParser parser = new JsonParser();
            tree = parser.parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (new Gson()).fromJson(((JsonObject) tree).get(key),Map.class);
    }

    protected void _setOption(WebElement element, String optionText) {
        Select select;
        select = new Select(element);
        String js = "arguments[0].selectedIndex = " + _getIndexOptionByText(select.getOptions(),optionText) + "; arguments[0].dispatchEvent(new Event('change'))";
        try {
            select.selectByVisibleText(optionText);
        } catch (ElementNotInteractableException e) {
            ((JavascriptExecutor) driver).executeScript(js,select);
        }
    }

    protected void _setOption(WebElement element) {
        Select select;
        select = new Select(element);
        Integer index = _getRandomNumber(1,select.getOptions().size()) - 1;
        String js = "arguments[0].selectedIndex = " + index + "; arguments[0].dispatchEvent(new Event('change'))";
        try {
            select.selectByIndex(index);
        } catch (ElementNotInteractableException e) {
            ((JavascriptExecutor) driver).executeScript(js,select);
        }
    }

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

    protected int _getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
