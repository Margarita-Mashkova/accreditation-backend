package ru.ulstu.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AuthPage {
    public WebDriver driver;
    @FindBy(xpath = "//*[@id=\"app\"]/div/form/div[1]/input")
    private WebElement inputLogin;
    @FindBy(xpath = "//*[@id=\"app\"]/div/form/div[2]/input")
    private WebElement inputPassword;
    @FindBy(xpath = "//*[@id=\"app\"]/div/form/div[3]/button")
    private WebElement btnLogin;
    @FindBy(xpath = "//*[@id=\"app\"]/div/form/div[1]/label")
    private WebElement labelError;

    public AuthPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterLogin(String login) {
        inputLogin.sendKeys(login);
    }
    public void enterPassword(String passwd) {
        inputPassword.sendKeys(passwd);
    }
    public void clickBtnLogin() {
        btnLogin.click();
    }
    public String getLabelError(){
        return labelError.getText();
    }
}
