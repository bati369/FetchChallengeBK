package tests;

import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.FetchChallengeMain;
import utilities.Config;
import utilities.Driver;
import utilities.FetchChallengeUtility;
import utilities.Flow;

import java.time.Duration;
import java.util.List;

public class TestFetchChallenge {
    FetchChallengeMain fcm = new FetchChallengeMain();
    FetchChallengeUtility fcu=new FetchChallengeUtility();
    int[] goldBars = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    int[] fakeTriada = new int[3];
    int fakeBarNumber = 0;

    @Test
    public void FetchTest(){

        Driver.getDriver().get(Config.getValue("challengeURL"));

         /*
         for solve this task by using minimum attempts of weighing, was chosen approach (algorithm) to dividing gold bars to 3 groups.
        It's achieved by using helper methods from utility class.
         */
        fcu.divideBarsToGroups(goldBars);
        int[] first3 = fcu.getFirstPart();
        int[] second3 = fcu.getSecondPart();
        int[] third3 = fcu.getThirdPart();

        //starting of group's  weighing process
        List<WebElement> firstRowLeft = fcm.firstRowLeft;
        for (int i = 0; i < firstRowLeft.size(); i++) {
            firstRowLeft.get(i).sendKeys(first3[i] + "", Keys.ENTER);
        }

        List<WebElement> firstRowRight = fcm.firstRowRight;
        for (int i = 0; i < firstRowRight.size(); i++) {
            firstRowRight.get(i).sendKeys(second3[i] + "", Keys.ENTER);
        }

        fcm.buttonWeigh.click();

        //To avoid synch. issues provided wait methods from utilities
        Flow.wait(3000);
        String weighResult = fcm.buttonResult.getText();
        fcm.buttonReset.click();

        //process of selecting triada or group with fake bar
        int [] selectedTriada;
        if ("=".equals(weighResult)) {
            selectedTriada = third3;
        } else if (">".equals(weighResult)) {
            selectedTriada = second3;
        } else {
            selectedTriada = first3;
        }

        firstRowLeft.get(0).sendKeys(selectedTriada[0] + "", Keys.ENTER);
        firstRowRight.get(0).sendKeys(selectedTriada[1] + "", Keys.ENTER);
        fcm.buttonWeigh.click();
        Flow.wait(">".equals(weighResult) ? 3000 : 5000); // Conditional wait based on `weighResult`
        String weighResult2 = fcm.buttonResult.getText();

        if ("=".equals(weighResult2)) {
            fakeBarNumber = selectedTriada[2];
        } else if (">".equals(weighResult2)) {
            fakeBarNumber = selectedTriada[1];
        } else {
            fakeBarNumber = selectedTriada[0];
        }
        System.out.println("Fake gold bar's number: "+fakeBarNumber);

        //process of counting all attempts and getting them as list

        List<WebElement> listOfWeighing=fcm.listOfWeighing;
        System.out.println("Number of attempts "+listOfWeighing.size());
        System.out.println("Results of weighing");
        for(WebElement weighing: listOfWeighing){
            System.out.println(weighing.getText());
        }

        //fake bar is selected. Process of clicking certain fake coin on the bottom of web page
        WebElement fakeCoin = fcm.getCoinByFakeNumber(fakeBarNumber+"");
        fakeCoin.click();
        try {
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(2));
            wait.until(ExpectedConditions.alertIsPresent());

            //step of switching to the Alert
            Alert alert = Driver.getDriver().switchTo().alert();
            String actualMessage = Driver.getDriver().switchTo().alert().getText();
            System.out.println("Actual message from alert is: "+actualMessage);
            alert.accept();
            String expectedMessage =Config.getValue("expectedPositiveMessage");

            //verification part of code by using TestNG Assertions
            Assert.assertEquals(actualMessage, expectedMessage);

        } catch (UnhandledAlertException uae) {
            System.out.println("Unhandled Alert detected: " + uae.getMessage());
            handleUnexpectedAlert();
        } catch (Exception e) {
            System.out.println("General error occurred: " + e.getMessage());
        }
    }

    //process of try-catch and handle "UnhandledAlertException"
    private void handleUnexpectedAlert() {
        try {
            Alert alert = Driver.getDriver().switchTo().alert();
            System.out.println("Alert text: " + alert.getText());
            alert.accept();
        } catch (Exception ex) {
            System.out.println("Error while handling the alert: " + ex.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        Driver.getDriver().quit();
    }
}
