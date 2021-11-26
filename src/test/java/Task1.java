import org.junit.jupiter.api.Test;

import static  org.openqa.selenium.support.ui.ExpectedConditions.titleIs;


public class Task1 extends Testt {
    public Task1() {
        baseURL = "https://search.maven.org/";
    }

    @Test
    public void Task3() {

        driver.get(baseURL);
        wait.until(titleIs("Maven Central Repository Search"));
    }
}
