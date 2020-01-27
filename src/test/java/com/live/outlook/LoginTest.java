package com.live.outlook;

import io.github.bonigarcia.wdm.WebDriverManager;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import junitparams.mappers.CsvWithHeaderMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class LoginTest {

    RemoteWebDriver driver;

    @BeforeClass
    public static void beforeAll() {
        WebDriverManager.firefoxdriver().setup();
    }

    @Before
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @FileParameters(value = "classpath:emails.csv", mapper = CsvWithHeaderMapper.class)
    public void loginHappyPath(String email, String password){
        driver.get("https://outlook.live.com/owa/");
        driver.findElement(By.cssSelector(".auxiliary-actions .sign-in-link")).click();
        driver.findElement(By.id("i0116")).sendKeys(email);
        driver.findElement(By.id("idSIButton9")).click();
        //Here window may appear. I did not manage to open that form. I created xpath selectors based on given screenshot
        if(!driver.findElements(By.xpath("//*[contains(text(),'It looks like this email is used with more than one account')]")).isEmpty()){
        driver.findElement(By.xpath("//*[contains(text(),'Personal account')]")).click();
        };
        driver.findElement(By.id("i0118")).sendKeys(password);
        driver.findElement(By.id("idSIButton9")).click();
        //Here window may appear. I did not manage to open that form. I created xpath selectors based on given screenshot
        if(!driver.findElements(By.xpath("//*[contains(text(),'Break free from your passwords')]")).isEmpty()){
            driver.findElement(By.xpath("//*[contains(text(),'No thanks')]")).click();
        };
        //assert that im logged by checking if 'New message' button is displayed
        assertTrue(driver.findElementById("id__3").isDisplayed());
    }
}
