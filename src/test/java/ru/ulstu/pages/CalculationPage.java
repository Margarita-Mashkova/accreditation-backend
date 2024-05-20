package ru.ulstu.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CalculationPage {
    public WebDriver driver;
    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[1]/select")
    private WebElement inputOpop;
    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[1]/select/option")
    private WebElement firstOpop;
    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[2]/select")
    private WebElement inputDate;
    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[2]/select/option")
    private WebElement firstDate;
    @FindBy(xpath = "//*[@id=\"app\"]/div[4]/div[1]/button")
    private WebElement btnMakeReport;
    @FindBy(xpath = "//*[@id=\"app\"]/div[6]/a[4]")
    private WebElement unwrapResultTable;
    @FindBy(xpath = "//*[@id=\"accreditation-status\"]")
    private WebElement accreditationStatus;

    public CalculationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickUnwrapSelectOpop() {
        inputOpop.click();
    }

    public void clickChooseFirstOpop() {
        firstOpop.click();
    }

    public void clickUnwrapSelectDate() {
        inputDate.click();
    }

    public void clickChooseFirstDate() {
        firstDate.click();
    }

    public void clickBtnMakeReport(){
        btnMakeReport.click();
    }

    public void clickUnwrapResultTable(){
        unwrapResultTable.click();
    }

    public String getAccreditationStatus(){
        return accreditationStatus.getText();
    }
}
