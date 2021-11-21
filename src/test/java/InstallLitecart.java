import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class InstallLitecart {
    private WebDriver driver;

    @BeforeAll
    public void start() {
        driver = new ChromeDriver();
    }

    @Test
    public void InstallLitecart() {
        WebElement installation_form;
        Select db_collation;
        Select country_code;
        Select store_time_zone;

        driver.get("http://localhost/litecart/install/");
        installation_form = driver.findElement(By.name("installation_form"));
        installation_form.findElement(By.name("db_database")).sendKeys("litecart");

        db_collation = new Select(installation_form.findElement(By.name("db_collation")));
        db_collation.selectByVisibleText("utf8_general_ci");

        installation_form.findElement(By.name("db_username")).sendKeys("root");
        installation_form.findElement(By.name("db_password")).sendKeys("");

        country_code = new Select(installation_form.findElement(By.name("country_code")));
        country_code.selectByVisibleText("Russian Federation");

        store_time_zone = new Select(installation_form.findElement(By.name("store_time_zone")));
        store_time_zone.selectByVisibleText("Asia/Novosibirsk");

        installation_form.findElement(By.name("admin_folder")).sendKeys("admin");
        installation_form.findElement(By.name("username")).sendKeys("admin");
        installation_form.findElement(By.name("password")).sendKeys("admin");

        installation_form.findElement(By.name("install")).click();
    }
}
