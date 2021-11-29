import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;


public class Task9 extends Testt {

    public Task9() {
        baseURL = "http://localhost/litecart/admin/?app=countries&doc=countries";
    }

    private List<String> hrefsZone = new ArrayList<String>();
    private boolean subTaskCountriesIsPassed;

    private boolean _isSorted(List<String> list) {
        List<String> sortedList = list.subList(0,list.size());
        sortedList.sort(Comparator.naturalOrder());
        return list.equals(sortedList);
    }
    //Проверяем, что страны расположенные в алфавитном порядке
    @DisplayName("Checking the list of countries")
    @Test
    public void subTaskCountries() {
        List<String> countries = new ArrayList<String>();
        driver.get(baseURL);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();

        for (WebElement link : driver.findElements(By.xpath("//a[contains(@href,'?app=countries&doc=edit_country&country_code=') and not(@title='Edit')]"))) {
            countries.add(link.getText());
            if (Integer.parseInt(link.findElement(By.xpath("./../following-sibling::*[1]/self::td")).getText()) > 0) {
                hrefsZone.add(link.getAttribute("href"));
            }
        }
        subTaskCountriesIsPassed = _isSorted(countries);
        assertTrue(subTaskCountriesIsPassed);
    }

    @Nested
    @DisplayName("Task9: Checking list of zones on Country page")
    class Zones {

        //Проверяем, что зоны страны расположены в алфовитном порядке
        @TestFactory
        public Collection<DynamicTest> subTaskZones() {
            List<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();
            List<String> zones = new ArrayList<String>();
            if (Task9.this.hrefsZone.size() == 0) {
                dynamicTests.add(DynamicTest.dynamicTest(
                        "The list of countries has not been received",
                        () -> assertTrue(Task9.this.subTaskCountriesIsPassed)
                ));
                return dynamicTests;
            }
            for (String href : Task9.this.hrefsZone) {
                driver.get(href);
                for (WebElement link : driver.findElements(By.xpath("//input[contains(@name,'][name]') and @type='hidden']"))) {
                    zones.add(link.findElement(By.xpath("./..")).getText());
                }
                dynamicTests.add(DynamicTest.dynamicTest(
                        driver.findElement(By.name("name")).getAttribute("value"),
                        () -> assertTrue(_isSorted(zones))
                ));
            }
            return dynamicTests;
        }
    }

}
