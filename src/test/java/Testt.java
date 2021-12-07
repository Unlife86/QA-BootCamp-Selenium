import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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

    protected void _actionElement(WebElement root, Map<String,String> map) {
        By by = By.xpath(map.get("xpath"));
        List<WebElement> elements = root.findElements(by);
        if (elements.size() == 1) {
            switch (elements.get(0).getAttribute("type")) {
                case "input":
                    _actionElement(elements.get(0),map.get("value"));
                    break;
                case "checkbox":
                case "radio":
                    _actionElement(elements.get(0));
                    break;
            }
        } else if (elements.size() > 1) {
            for (WebElement element : elements) {
                element.getText();
            }
        }
    }
    protected void _actionElement(WebElement element) {
        if (element.getAttribute("checked").equals("false")) element.click();
    }
    protected void _actionElement(WebElement element, String string) {
        element.sendKeys(string);
    }

}
