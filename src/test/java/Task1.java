import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static  org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Task1 /*extends Testt implements Runnable*/ {
    public Task1() {
        baseURL = "https://search.maven.org/";
        drivers.add(new ChromeDriver());
        drivers.add(new FirefoxDriver());
    }
    /*private Thread chrome;
    private Thread firefox;*/
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected List<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();
    protected List<WebDriver> drivers = new ArrayList<>();
    protected List<ThreadLocalDriver> threadsLocal = new ArrayList<ThreadLocalDriver>();
    protected List<Thread> threads = new ArrayList<Thread>();
    protected String baseURL;

    public static ThreadLocal<WebDriver> threadLocal = new ThreadLocal<WebDriver>();

    class ThreadLocalDriver implements Runnable {
        private volatile WebDriver driver;

        public ThreadLocalDriver(WebDriver driver) {
            this.driver = driver;
        }

        @Override
        public void run() {
            threadLocal.set(driver);
        }

        public WebDriver getDriver() {
            return driver;
        }
    }

    @BeforeAll
    public void start() throws InterruptedException {
        for (WebDriver driver : drivers) {
            threadsLocal.add(new ThreadLocalDriver(driver));
        }
        for (ThreadLocalDriver threadLocal : threadsLocal) {
            threads.add(new Thread(threadLocal));
        }
        for (Thread thread : threads) {
            thread.start();
            thread.join();
        }
    }

    @AfterAll
    public void stop() {
        for (WebDriver driver : drivers) {
            driver.quit();
            driver = null;
        }
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    protected void  _dynamicTests() {
        dynamicTests.add(
                _lambaDynamicTests()
        );
    }

    protected DynamicTest _lambaDynamicTests() {
        return DynamicTest.dynamicTest(
                driver.getClass().getSimpleName(),
                () -> Assertions.assertTrue(wait.until(titleIs("Maven Central Repository Search")))
        );
    }

    @TestFactory
    public Collection<DynamicTest> Task() {
        for (int i = 0; i < drivers.size(); i++) {
            driver = threadsLocal.get(i).getDriver();
            driver.get(baseURL);
            wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            _dynamicTests();
        }
        return dynamicTests;
    }
}
