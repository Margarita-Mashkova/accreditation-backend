package ru.ulstu.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AnalysisPage {
    public WebDriver driver;
    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[1]/select")
    private WebElement inputOpop;
    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[1]/select/option")
    private WebElement firstOpop;
    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[2]/input")
    private WebElement inputDateStart;
    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[3]/input")
    private WebElement inputDateEnd;
    @FindBy(xpath = "//*[@id=\"app\"]/div[4]/div[1]/button")
    private WebElement btnMakeReport;
    @FindBy(xpath = "//*[@id=\"app\"]/div[6]/h4")
    private WebElement labelNoData;
    @FindBy(xpath = "//*[@id=\"app\"]/div[11]/div[1]/h4")
    private WebElement labelRecommendations;
    @FindBy(xpath = "//*[@id=\"app\"]/div[7]/div/label")
    private WebElement labelGraphics;

    public AnalysisPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickUnwrapSelectOpop() {
        inputOpop.click();
    }

    public void clickChooseFirstOpop() {
        firstOpop.click();
    }

    public void enterDateStart(String date) {
        inputDateStart.sendKeys(date);
    }

    public void enterDateEnd(String date) {
        inputDateEnd.sendKeys(date);
    }

    public void clickBtnMakeReport() {
        btnMakeReport.click();
    }

    public String getLabelNoData() {
        return labelNoData.getText();
    }

    public String getLabelRecommendations(){
        return labelRecommendations.getText();
    }

    public String getLabelGraphics(){
        return labelGraphics.getText();
    }
}
