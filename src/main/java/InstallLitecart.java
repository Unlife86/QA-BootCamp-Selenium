import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.FileOutputStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class InstallLitecart {
    public static void main(String[] args) throws IOException {
        InstallLitecart install = new InstallLitecart();
        File destDir = new File("C:\\litecart");
        if (install.deleteDirectory(destDir)) {
            install.unzipFile("C:\\Users\\Admin\\Downloads\\litecart-1.3.7.zip",destDir);
        }
        install.install();

    }

    public boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public void unzipFile(String fileZip,File destDir) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // write file content
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }
        }
        zis.closeEntry();
        zis.close();
    }

    public File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public void install() {
        WebDriver driver = new ChromeDriver();
        WebElement installation_form;
        Select db_collation;
        Select country_code;
        Select store_time_zone;

        driver.get("http://localhost/phpmyadmin/");
        driver.findElement(By.cssSelector("li.first.new_database.italics a.hover_show_full")).click();
        driver.findElement(By.name("new_db")).sendKeys("litecart");
        (new Select(driver.findElement(By.name("db_collation")))).selectByVisibleText("utf8_general_ci");
        driver.findElement(By.id("buttonGo")).click();

        driver.get("http://localhost/litecart/install/");
        installation_form = driver.findElement(By.name("installation_form"));

        installation_form.findElement(By.name("db_database")).clear();
        installation_form.findElement(By.name("db_database")).sendKeys("litecart");

        db_collation = new Select(installation_form.findElement(By.name("db_collation")));
        db_collation.selectByVisibleText("utf8_general_ci");
        installation_form.findElement(By.name("demo_data")).click();

        installation_form.findElement(By.name("db_username")).clear();
        installation_form.findElement(By.name("db_password")).clear();
        installation_form.findElement(By.name("db_username")).sendKeys("root");
        installation_form.findElement(By.name("db_password")).sendKeys("");

        country_code = new Select(installation_form.findElement(By.name("country_code")));
        country_code.selectByVisibleText("Russian Federation");
        store_time_zone = new Select(installation_form.findElement(By.name("store_time_zone")));
        store_time_zone.selectByVisibleText("Asia/Novosibirsk");



        installation_form.findElement(By.name("admin_folder")).clear();
        installation_form.findElement(By.name("username")).clear();
        installation_form.findElement(By.name("password")).clear();
        installation_form.findElement(By.name("admin_folder")).sendKeys("admin");
        installation_form.findElement(By.name("username")).sendKeys("admin");
        installation_form.findElement(By.name("password")).sendKeys("admin");

        installation_form.findElement(By.name("install")).click();
    }
}
