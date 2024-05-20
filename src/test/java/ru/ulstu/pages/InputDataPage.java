package ru.ulstu.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class InputDataPage {
    public WebDriver driver;
    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[1]/select")
    private WebElement inputOpop;
    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[1]/select/option")
    private WebElement firstOpop;
    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[2]/input")
    private WebElement inputDate;
    @FindBy(xpath = "//*[@id=\"app\"]/div[6]/div[1]/input[2]")
    private WebElement inputFirstVariable;
    @FindBy(xpath = "//*[@id=\"app\"]/div[6]/div[2]/input[2]")
    private WebElement inputSecondVariable;
    @FindBy(xpath = "//*[@id=\"app\"]/div[6]/div[3]/input[2]")
    private WebElement inputThirdVariable;
    @FindBy(xpath = "//*[@id=\"app\"]/div[7]/button")
    private WebElement btnSave;

    public InputDataPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickUnwrapSelectOpop() {
        inputOpop.click();
    }

    public void clickChooseFirstOpop() {
        firstOpop.click();
    }

    public void enterDate(String date) {
        inputDate.sendKeys(date);
    }

    public void enterFirstVariable(String value) {
        inputFirstVariable.clear();
        inputFirstVariable.sendKeys(value);
    }

    public void enterSecondVariable(String value) {
        inputSecondVariable.clear();
        inputSecondVariable.sendKeys(value);
    }

    public void enterThirdVariable(String value) {
        inputThirdVariable.clear();
        inputThirdVariable.sendKeys(value);
    }

    public void clickBtnSave(){
        btnSave.click();
    }
}
