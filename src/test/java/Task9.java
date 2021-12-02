import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class Task9 extends Testt {

    public Task9() {

    }

    private List<String> hrefsZone = new ArrayList<String>();
    private boolean subTaskCountriesIsPassed;

    private void _go() {
        driver.get(baseURL);
        if (driver.getCurrentUrl().contains("http://localhost/litecart/admin/login.php?")) {
            driver.findElement(By.name("username")).sendKeys("admin");
            driver.findElement(By.name("password")).sendKeys("admin");
            driver.findElement(By.name("login")).click();
        }
    }
    private boolean _isSorted(List<String> list) {
        List<String> sortedList = ((List) ((ArrayList) list).clone());
        sortedList.sort(Comparator.naturalOrder());
        return list.equals(sortedList);
    }
    private void _countries(String href, Boolean condition) {
        href = String.format("//a[contains(@href,'%s') and not(@title='Edit')]",href);
        List<String> countries = new ArrayList<String>();
        for (WebElement link : driver.findElements(By.xpath(href))) {
            countries.add(link.getText());
            if (!condition) {
                condition = Integer.parseInt(link.findElement(By.xpath("./../following-sibling::*[1]/self::td")).getText()) > 0;
            }
            if (condition) {
                hrefsZone.add(link.getAttribute("href"));
                condition = false;
            }
        }
        subTaskCountriesIsPassed = _isSorted(countries);
    }
    private Collection<DynamicTest> _zones(String path) {
        List<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();
        List<String> zones = new ArrayList<String>();
        if (this.hrefsZone.size() == 0) {
            dynamicTests.add(DynamicTest.dynamicTest(
                    "The list of countries has not been received",
                    () -> assertTrue(this.subTaskCountriesIsPassed)
            ));
            return dynamicTests;
        }
        for (String href : this.hrefsZone) {
            zones.clear();
            driver.get(href);
            for (WebElement link : driver.findElements(
                    By.xpath(path)
            )) {
                zones.add(link.getText());
            }
            dynamicTests.add(DynamicTest.dynamicTest(
                    driver.findElement(By.name("name")).getAttribute("value"),
                    () -> assertTrue(_isSorted(zones))
            ));
        }

        return dynamicTests;
    }
    //Проверяем, что страны расположенные в алфавитном порядке
    @DisplayName("Checking the list of countries")
    @Test
    @Order(1)
    public void subTaskCountries() {
        baseURL = "http://localhost/litecart/admin/?app=countries&doc=countries";
        _go();
        _countries("?app=countries&doc=edit_country&country_code=",false);
        assertTrue(subTaskCountriesIsPassed);
    }

    @Nested
    @Order(2)
    @DisplayName("Task9: Checking list of zones on Country page")
    class SubTask1OfTask9 {
        @BeforeEach
        private void start() {
            if (Task9.this.hrefsZone.size() == 0) {
                Task9.this.hrefsZone.add("http://localhost/litecart/admin/?app=countries&doc=edit_country&country_code=CA");
                Task9.this.hrefsZone.add("http://localhost/litecart/admin/?app=countries&doc=edit_country&country_code=US");
            }
            _go();
        }
        //Проверяем, что зоны страны расположены в алфовитном порядке
        @TestFactory
        public Collection<DynamicTest> subTaskZones() {
            return Task9.this._zones("//input[contains(@name,'][name]') and @type='hidden']/..");
        }
    }

    @Nested
    @Order(3)
    @DisplayName("SubTask2: Checking list of zones on Country page")
    class SubTask2OfTask9 {
        @BeforeEach
        private void start() {
            Task9.this.hrefsZone.clear();
            baseURL = "http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones";
            Task9.this._go();
        }
        @TestFactory
        public Collection<DynamicTest> subTaskZones() {
            Task9.this._countries(
                    "?app=geo_zones&doc=edit_geo_zone&page=1&geo_zone_id=",
                    true
            );
            return Task9.this._zones("//select[contains(@name,'zone_code')]//option[@selected='selected']");
        }
    }

}
