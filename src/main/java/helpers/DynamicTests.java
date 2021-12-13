package helpers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class DynamicTests {

    private WebDriver driver;

    public DynamicTests(WebDriver driver) {
        this.driver = driver;
    }

    private List<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();

    protected void _dynamicTests() {
        dynamicTests.add(
                _lambaDynamicTests();
        );
    }

    protected DynamicTest _lambaDynamicTests(String displayName, Executable executable) {
        return DynamicTest.dynamicTest(
                driver.getClass().getSimpleName(),
                executable
        );
    }
}
