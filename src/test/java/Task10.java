import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Task10: The product properties")
public class Task10 extends Task1 {

    public Task10() {
        baseURL = "https://litecart.stqa.ru/en/";//"http://localhost/litecart/en/";

        /*drivers.add(new ChromeDriver());
        drivers.add(new FirefoxDriver());*/
        drivers.add(new EdgeDriver());
    }

    @Override
    protected void _dynamicTests() {
        List<String> productPropIndex = Arrays.asList(new String[]{"name","regular-price", "campaign-price"});
        List<String> valPropProductIndex = new ArrayList<String>();

        for (int i = 0; i < 3; i++) {
            valPropProductIndex.add(driver.findElement(By.cssSelector("#box-campaigns .product:first-child ." + productPropIndex.get(i))).getText());
        }
        driver.findElement(By.cssSelector("#box-campaigns .product:first-child .link")).click();
        for (int i = 0; i < 3; i++) {
            dynamicTests.add(_lambaDynamicTests(valPropProductIndex.get(i),i));
        }
    }

    protected DynamicTest _lambaDynamicTests(final String index, int i) {
        List<String> prop = Arrays.asList(new String[]{"Name","Regular price", "Campaign price"});
        List<String> productProp = Arrays.asList(new String[]{"title","regular-price", "campaign-price"});
        return DynamicTest.dynamicTest(
                driver.getClass().getSimpleName()+": "+ prop.get(i),
                () -> assertEquals(
                    index,
                    driver.findElement(By.cssSelector("#box-product ." + productProp.get(i))).getText()
                )
        );
    }

    @Override
    @TestFactory
    @DisplayName("Name Test")
    public Collection<DynamicTest> Task() {
        return super.Task();
    }

    @Nested
    @DisplayName("Task10: Prices on Index page")
    class PriceIndex {
        protected List<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();
        protected DynamicTest _lambaDynamicTests(final int i, final WebElement element) {
            List<String> cssAttr = Arrays.asList(new String[]{"text-decoration-line", "font-weight"});
            List<String> cssAttrVal = Arrays.asList(new String[]{"line-through", "700"});
            return DynamicTest.dynamicTest(
                    driver.getClass().getSimpleName()+": Style text." + element.getAttribute("class"),
                    () -> assertEquals(
                        cssAttrVal.get(i),
                        element.getCssValue(cssAttr.get(i))
                )
            );
        }
        protected DynamicTest _lambaDynamicTests(final int i, final String val, final String text) {
            List<String> color = Arrays.asList(new String[]{
                    String.format(
                            "^%s,%s,%s",
                            val.split(",")[0],
                            val.split(",")[0],
                            val.split(",")[0]
                    ),
                    "^\\d{1,3},0,0"
            });
            Pattern pattern = Pattern.compile(color.get(i));
            Matcher matcher = pattern.matcher(val);
            System.out.println(color.get(i));
            System.out.println(val);
            return DynamicTest.dynamicTest(
                    driver.getClass().getSimpleName()+": Color text."+ text,
                    () -> assertTrue(matcher.find())
            );
        }

        public void _dynamicTests() {
            List<WebElement> prices = getPrices();
            for (int i = 0; i < 2; i++) {
                dynamicTests.add(_lambaDynamicTests(i, prices.get(i)));
                dynamicTests.add(_lambaDynamicTests(i,
                        prices.get(i).getCssValue("color").replaceAll("[^0-9,]", ""),
                        prices.get(i).getAttribute("class"))
                );
            }
        }

        @TestFactory
        public Collection<DynamicTest> Task() {
            for (int i = 0; i < drivers.size(); i++) {
                driver = threadsLocal.get(i).getDriver();
                _dynamicTests();
            }
            return dynamicTests;
        }

        protected DynamicTest _lambaDynamicTests(float regPriceTextSize, float campPriceTextSize) {
            return DynamicTest.dynamicTest(
                    driver.getClass().getSimpleName()+": Font-size of a price",
                    () -> assertTrue(regPriceTextSize < campPriceTextSize)
            );
        }

        @TestFactory
        public Collection<DynamicTest> regularAndCampaignTextSizePrice() {
            List<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();
            for (int i = 0; i < drivers.size(); i++) {
                driver = threadsLocal.get(i).getDriver();
                List<WebElement> prices = new ArrayList<WebElement>();
                prices = getPrices();
                dynamicTests.add(
                        _lambaDynamicTests(
                                Float.parseFloat(prices.get(0).getCssValue("font-size").replaceAll("[^0-9.,]", "")),
                                Float.parseFloat(prices.get(1).getCssValue("font-size").replaceAll("[^0-9.,]", ""))
                        )
                );
            }
            return dynamicTests;
        }

        protected List<WebElement> getPrices() {
            List<WebElement> prices = new ArrayList<WebElement>();
            if (driver.getCurrentUrl() != Task10.this.baseURL) {
                driver.get(Task10.this.baseURL);
            }
            prices.add(driver.findElement(By.cssSelector("#box-campaigns .product:first-child .regular-price")));
            prices.add(driver.findElement(By.cssSelector("#box-campaigns .product:first-child .campaign-price")));
            return prices;
        }
    }

    @Nested
    @DisplayName("Task10: Prices on Product page")
    class PriceProduct extends PriceIndex {
        @Override
        protected List<WebElement> getPrices() {
            List<WebElement> prices = new ArrayList<WebElement>();
            if (Task10.this.baseURL.equals(driver.getCurrentUrl())) {
                driver.findElement(By.cssSelector("#box-campaigns .product:first-child .link")).click();
            }
            prices.add(driver.findElement(By.cssSelector("#box-product .regular-price")));
            prices.add(driver.findElement(By.cssSelector("#box-product .campaign-price")));
            return prices;
        }
    }
}
