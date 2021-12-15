package helpers;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class DynamicTests {

    private WebDriver driver;

    public DynamicTests(WebDriver driver) {
        this.driver = driver;
    }

    private List<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();

    public Collection<DynamicTest> getDynamicTests() {
        return dynamicTests;
    }

    public void dynamicTests(String displayName, Executable executable) {
        dynamicTests.add(
                _lambaDynamicTests(displayName,executable)
        );
    }

    private DynamicTest _lambaDynamicTests(String displayName, Executable executable) {
        return DynamicTest.dynamicTest(
                displayName,
                executable
        );
    }
}
