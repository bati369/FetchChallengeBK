package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

import java.util.List;

public class FetchChallengeMain {

    public FetchChallengeMain() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//button[@id='weigh']")
    public WebElement buttonWeigh;

    @FindBy(xpath = "//button[.='Reset']")
    public WebElement buttonReset;

    @FindBy(xpath = "//div[@class='result']//button")
    public WebElement buttonResult;

    @FindBy(xpath = "(//div[@class='board-row'])[1]//input")
    public List<WebElement> firstRowLeft;

    @FindBy(xpath = "(//div[@class='board-row'])[4]//input")
    public List<WebElement> firstRowRight;

    @FindBy(xpath = "//div[@class='game-info']//li")
    public List<WebElement> listOfWeighing;

    @FindBy(xpath = "//div[@class='coins']//button")
    public List<WebElement> coins;

    @FindBy(xpath = "//*[.='Weighings']/..//li")
    public List <WebElement> listOfWeighing2;



    public WebElement getCoinByFakeNumber(String value) {
        String newValue=Integer.parseInt(value)+1+"";

        return Driver.getDriver().findElement(By.xpath("(//div[@class='coins']//button)["+newValue+"]"));

    }
}
