package ru.ulstu;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ulstu.pages.AnalysisPage;
import ru.ulstu.pages.AuthPage;
import ru.ulstu.pages.CalculationPage;
import ru.ulstu.pages.HomePage;
import ru.ulstu.pages.InputDataPage;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccreditationApplicationTests {
	public static WebDriver driver;
	public static AuthPage authPage;
	public static AnalysisPage analysisPage;
	public static InputDataPage inputDataPage;
	public static CalculationPage calculationPage;
	public static HomePage homePage;

	/**
	 * Осуществление первоначальной настройки
	 */
	@BeforeAll
	public static void setup() {
		//определение пути до драйвера и его настройка
		System.setProperty("webdriver.chrome.driver", ConfProperties.getProperty("chromedriver"));
		//создание экземпляра драйвера
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		driver = new ChromeDriver(options);
		authPage = new AuthPage(driver);
		homePage = new HomePage(driver);
		inputDataPage = new InputDataPage(driver);
		calculationPage = new CalculationPage(driver);
		analysisPage = new AnalysisPage(driver);
		//окно разворачивается на полный экран
		driver.manage().window().maximize();
		//задержка на выполнение теста = 10 сек.
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}


	@Test
	@Order(1)
	void contextLoads() {
	}

	@Test
	@Order(2)
	public void authWithWrongData() {
		driver.get(ConfProperties.getProperty("loginPage"));
		authPage.enterLogin(ConfProperties.getProperty("loginDean"));
		authPage.enterPassword(ConfProperties.getProperty("passwordDeanWrong"));
		authPage.clickBtnLogin();
		Assertions.assertEquals("Неверный логин и/или пароль", authPage.getLabelError());
	}

	@Test
	@Order(3)
	public void authWithValidData() throws InterruptedException {
		driver.get(ConfProperties.getProperty("loginPage"));
		authPage.enterLogin(ConfProperties.getProperty("loginDean"));
		authPage.enterPassword(ConfProperties.getProperty("passwordDean"));
		authPage.clickBtnLogin();
		Thread.sleep(1000);
		String homeWelcome = homePage.getLabelWelcome();
		Assertions.assertTrue(homeWelcome.contains("Здравствуйте"));
	}

	@Test
	@Order(4)
	public void inputDataForCalculation() throws InterruptedException {
		driver.get(ConfProperties.getProperty("inputDataPage"));
		inputDataPage.clickUnwrapSelectOpop();
		inputDataPage.clickChooseFirstOpop();
		inputDataPage.enterDate("01.01.2024");
		inputDataPage.enterFirstVariable("50");
		inputDataPage.enterSecondVariable("15");
		inputDataPage.enterThirdVariable("30");
		inputDataPage.clickBtnSave();
		Thread.sleep(2000);
		String alertMessage = driver.switchTo().alert().getText();
		driver.switchTo().alert().accept();
		Assertions.assertEquals("Данные добавлены", alertMessage);
	}

	@Test
	@Order(5)
	public void calculateIndicators(){
		driver.get(ConfProperties.getProperty("calculationPage"));
		calculationPage.clickUnwrapSelectOpop();
		calculationPage.clickChooseFirstOpop();
		calculationPage.clickUnwrapSelectDate();
		calculationPage.clickChooseFirstDate();
		calculationPage.clickBtnMakeReport();
		calculationPage.clickUnwrapResultTable();
		String accreditationStatus = calculationPage.getAccreditationStatus().toLowerCase();
		Assertions.assertTrue(accreditationStatus.contains("аккредитована"));
	}

	@Test
	@Order(6)
	public void analysisIndicatorsWithWrongPeriod() throws InterruptedException {
		driver.get(ConfProperties.getProperty("analysisPage"));
		analysisPage.clickUnwrapSelectOpop();
		analysisPage.clickChooseFirstOpop();
		analysisPage.enterDateStart("01.01.2029");
		analysisPage.enterDateEnd("01.01.2032");
		analysisPage.clickBtnMakeReport();
		Thread.sleep(1000);
		String labelNoData = analysisPage.getLabelNoData();
		Assertions.assertEquals("Нет данных для указанного периода", labelNoData);
	}

	@Test
	@Order(7)
	public void analysisIndicatorsWithValidPeriod() throws InterruptedException {
		driver.get(ConfProperties.getProperty("analysisPage"));
		analysisPage.clickUnwrapSelectOpop();
		analysisPage.clickChooseFirstOpop();
		analysisPage.enterDateStart("01.01.2022");
		analysisPage.enterDateEnd("01.01.2024");
		analysisPage.clickBtnMakeReport();
		Thread.sleep(1000);
		String labelRecommendations = analysisPage.getLabelRecommendations();
		String labelGraphics = analysisPage.getLabelGraphics();
		Assertions.assertEquals("Рекомендации", labelRecommendations);
		Assertions.assertEquals("Тип графиков", labelGraphics);
	}

	@Test
	@Order(8)
	public void tearDown() {
		//Закрытие окна браузера
		driver.quit();
	}
}
