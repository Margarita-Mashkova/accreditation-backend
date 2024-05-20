package ru.ulstu.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    public WebDriver driver;
    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/h2")
    private WebElement labelWelcome;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getLabelWelcome(){
        return labelWelcome.getText();
    }
}
