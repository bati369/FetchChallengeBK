package utilities;

import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.FetchChallengeMain;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class FetchChallengeUtility {

    private int[] firstGroup = new int[0];
    private int[] secondGroup = new int[0];
    private int[] thirdGroup = new int[0];
    FetchChallengeMain fcm=new FetchChallengeMain();

    public void divideBarsToGroups(int[] goldBars) {

        int groupSize = goldBars.length / 3;

        this.firstGroup = new int[groupSize];
        this.secondGroup = new int[groupSize];
        this.thirdGroup = new int[goldBars.length - 2 * groupSize];

        for (int i = 0; i < goldBars.length; i++) {
            if (i < groupSize) {
                firstGroup[i] = goldBars[i];
            } else if (i < 2 * groupSize) {
                secondGroup[i - groupSize] = goldBars[i];
            } else {
                thirdGroup[i - 2 * groupSize] = goldBars[i];

            }
        }
//        System.out.println(Arrays.toString(firstPart));
//        System.out.println(Arrays.toString(secondPart));
//        System.out.println(Arrays.toString(thirdPart));
    }

    public int[] getFirstPart() {
        return firstGroup;
    }

    public int[] getSecondPart() {
        return secondGroup;
    }

    public int[] getThirdPart() {
        return thirdGroup;
    }

    public boolean resultOfWeigh(String resultOfWeigh) {

        return true;
    }
    public void weighBars(int[] leftBars, int[] rightBars) {
        List<WebElement> firstRowLeft = fcm.firstRowLeft;
        List<WebElement> firstRowRight = fcm.firstRowRight;

        for (int i = 0; i < firstRowLeft.size(); i++) {
            firstRowLeft.get(i).sendKeys(leftBars[i] + "", Keys.ENTER);
            firstRowRight.get(i).sendKeys(rightBars[i] + "", Keys.ENTER);
        }

        fcm.buttonWeigh.click();
        Flow.wait(3000);
    }

    private void determineFakeBar(int[] bars) {
        List<WebElement> firstRowLeft = fcm.firstRowLeft;
        List<WebElement> firstRowRight = fcm.firstRowRight;

        firstRowLeft.get(0).sendKeys(bars[0] + "", Keys.ENTER);
        firstRowRight.get(0).sendKeys(bars[1] + "", Keys.ENTER);
        fcm.buttonWeigh.click();
        Flow.wait(3000);

        String weighResult = fcm.buttonResult.getText();
        System.out.println(weighResult);

        int fakeBarNumber;
        switch (weighResult) {
            case "=":
                fakeBarNumber = bars[2];
                break;
            case ">":
                fakeBarNumber = bars[1];
                break;
            default:
                fakeBarNumber = bars[0];
                break;
        }

        System.out.println(fakeBarNumber);

        List<WebElement> coins = fcm.coins;
        System.out.println(coins.size() + " size here");
        System.out.println(fakeBarNumber);
        WebElement fakeCoin = fcm.getCoinByFakeNumber(fakeBarNumber + "");
        fakeCoin.click();
        try {
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(2));
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = Driver.getDriver().switchTo().alert();
            String actualMessage = Driver.getDriver().switchTo().alert().getText();
            alert.accept();
            String expectedMessage = "Yay! You find it!";
            Assert.assertEquals(actualMessage, expectedMessage);

        } catch (
                UnhandledAlertException uae) {
            System.out.println("Unhandled Alert detected: " + uae.getMessage());
            handleUnexpectedAlert();
        } catch (Exception e) {
            System.out.println("General error occurred: " + e.getMessage());
        }
    }

    private void handleUnexpectedAlert() {
        try {
            Alert alert = Driver.getDriver().switchTo().alert();
            System.out.println("Alert text: " + alert.getText());
            alert.accept();
        } catch (Exception ex) {
            System.out.println("Error while handling the alert: " + ex.getMessage());
        }
    }




}