package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class Driver {

     private static WebDriver driver;
    private static String browser = Config.getValue("browser");

    public static WebDriver getDriver(){
        WebDriverManager.chromedriver().setup();
        if (driver== null){

            if (browser.equalsIgnoreCase("chrome")){


                ChromeOptions co = new ChromeOptions();
                co.addArguments("--remote-allow-origins=*");
                co.addArguments(Config.getValue("chromeOptions"));

                driver = new ChromeDriver(co);
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                return driver;
            } else if (browser.equalsIgnoreCase("firefox")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                return driver;
            }
            else{
                System.out.println("Invalid Browser Type. Launching Default Browser");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                return driver;
            }

        }else {
            return driver;
        }
    }

    public static void quitBrowser(){
        if (driver != null){
            driver.quit();
            driver = null;
        }
    }
    public static void closeBrowser(){
        if(driver!=null){
            driver.close();

        }
    }

}
