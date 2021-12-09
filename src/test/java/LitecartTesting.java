import org.junit.jupiter.api.*;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LitecartTesting extends Testt {

    private boolean _isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private DynamicTest _factory(final String str) {
        String h1 = driver.findElement(By.cssSelector("#content h1")).getText();
        return DynamicTest.dynamicTest(str, () -> assertEquals(str, h1));
    }

    @Test
    @DisplayName("Task3: Sign on")
    public void Task3() {
        String text = " You are now logged in as admin";
        Boolean result = false;
        _login();
        if (_isElementPresent(By.cssSelector("div.notice.success"))) {
            if (text.equals(driver.findElement(By.cssSelector("div.notice.success")).getText())) {
                result = true;
            }
        } else {
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("Task7: Sections of the administrative panel")
    class Task7 {
        int menuCount;
        WebElement root;

        @Test
        @DisplayName("Count item")
        public void MenuSize() {
            root = driver.findElement(By.id("box-apps-menu"));
            menuCount = root.findElements(By.id("app-")).size();
            assertTrue(menuCount == 17);
        }

        @TestFactory
        @DisplayName("Links")
        public Collection<DynamicTest> Links() {
            int countI; int countJ;
            String text;
            List<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();
            countI = driver.findElements(By.cssSelector("#box-apps-menu li#app- > a")).size();
            if (countI > 0) {
                for (int i = 1; i <= countI; i++) {
                    driver.findElement(By.cssSelector("#box-apps-menu li#app-:nth-child(" + i + ") > a")).click();
                    countJ = driver.findElements(By.cssSelector("#box-apps-menu li#app-:nth-child(" + i + ") > ul.docs li[id*=doc-]:not(.selected) > a")).size();
                    if (countJ > 0) {
                        dynamicTests.add(_factory(driver.findElement(By.cssSelector("#box-apps-menu li#app-:nth-child(" + i + ") > ul.docs > li[id*=doc-].selected > a")).getText()));
                        for (int j = 2; j <= countJ + 1; j++) {
                            driver.findElement(By.cssSelector("#box-apps-menu li#app-:nth-child(" + i + ") > ul.docs li[id*=doc-]:not(.selected):nth-child(" + j + ") > a")).click();
                            dynamicTests.add(_factory(driver.findElement(By.cssSelector("#box-apps-menu li#app-:nth-child(" + i + ") > ul.docs li[id*=doc-].selected > a")).getText()));
                        }
                    } else {
                        text = driver.findElement(By.cssSelector("#box-apps-menu li#app-:nth-child(" + i + ") > a")).getText();
                        dynamicTests.add(_factory(text));
                    }

                }
                return dynamicTests;
            }
            dynamicTests.add(DynamicTest.dynamicTest("No such element", () -> assertTrue(false)));
            return dynamicTests;
        }

    }

    @Nested
    @DisplayName("Task8: Products")
    class Task8 {
        private DynamicTest _stickers(final WebElement root, final String text) {
            return DynamicTest.dynamicTest(
                    text + ": " + root.findElement(By.className("link")).getAttribute("title") + "has only one sticker",
                    () -> assertEquals(1, root.findElements(By.className("sticker")).size())
            );
        }
        @TestFactory
        public Collection<DynamicTest> Stickers() {
            List<WebElement> items;
            List<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();
            driver.get(LitecartTesting.this.baseURL);
            List<WebElement> categories = driver.findElements(By.cssSelector("#main > .middle > .content div[id^=box-].box "));
            for (WebElement category : categories) {
                items = category.findElements(By.cssSelector("li.product.column.shadow.hover-light"));
                for (WebElement item : items) {
                    dynamicTests.add(_stickers(item,category.findElement(By.tagName("h3")).getText()));
                }
            }
            return dynamicTests;
        }

    }
}

