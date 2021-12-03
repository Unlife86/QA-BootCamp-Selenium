import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Task12 extends Testt {
    private Map<String,Map<String,String>> tabsFields;
    /*private MockNeat mock = MockNeat.old();*/

    public Task12() {
        baseURL = "http://localhost/litecart/admin/?app=catalog&doc=catalog";
        for (String tab : Arrays.asList(new String[]{"General","Information", "Prices"})) {
            tabsFields.put(
                    tab,
                    (new Gson()).fromJson(
                            String.format(
                                    "{'Status':'%s','Name':'../span/input','Code':'%s','Categories':'%s','Default Category':'%s','Product Groups':'%s','Quantity':'%s','Quantity Unit':'%s','Delivery Status':'%s','Sold Out Status':'%s','Upload Images':'%s'}",
                                    "../label/input",
                                    "../input",
                                    "..//input[@data-name='Rubber Ducks']",
                                    "../select[@name='default_category_id']",
                                    "..//table//input[contains(@name,'product_groups')]",
                                    "../../following-sibling::*[1]/self::tr//input[contains(@name,'quantity')]",
                                    "../../following-sibling::*[1]/self::tr//select[contains(@name,'quantity')]",
                                    "../../following-sibling::*[1]/self::tr//select[@name='delivery_status_id']",
                                    "../../following-sibling::*[1]/self::tr//select[@name='sold_out_status_id']",
                                    "..//input[contains(@name,'new_images')]"
                            ),
                            Map.class
                    )
            );
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
    }

    private void _fillFields() {

    }

    private void gson() throws FileNotFoundException, IOException {
        Map<String,String> map = new HashMap<>();

        String fileName = "src/test/resources/addNewPoduct.json";
        Path path = Paths.get(fileName);

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonParser parser = new JsonParser();
            JsonElement tree = parser.parse(reader);
            (new Gson()).fromJson(tree,Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void canAddProduct() {

    }






}
