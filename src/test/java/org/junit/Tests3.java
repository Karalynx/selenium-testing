package org.junit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Tests3 {

	private static WebDriver driver;
	private static WebDriverWait wait;
	private static User user = new User();
	
	@Before
	public void setup() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		
		driver = new ChromeDriver(options);
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
	public List<String> readProductNames(String fileName) throws IOException {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while (null != (line = reader.readLine())) {
                lines.add(line);
            }
        }
        
        return lines;
	}
	
	@After
	public void cleanup() {
		if(null != driver) {
			driver.quit();
		}
	}
	
	@BeforeClass
	public static void registerUser() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--disable-gpu");
		options.addArguments("--window-size=1920,1080");
		
		WebDriver driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		driver.get("https://demowebshop.tricentis.com/");
		
		WebElement loginLink = driver.findElement(
				By.xpath("//div[contains(@class, 'header-links')]//*[@href = '/login']")
		);
		loginLink.click();
		
		WebElement registerLink = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[contains(@class, 'register-block')]//input[contains(@class, 'register-button')]")
		));
		registerLink.click();
		
		WebElement registerPage = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[contains(@class, 'registration-page')]")
		));
		WebElement genderMaleCheckbox = registerPage.findElement(
				By.xpath("//input[@id = 'gender-male']")
		);
		WebElement firstNameInput = registerPage.findElement(
				By.xpath("//input[@id = 'FirstName']")
		);
		WebElement lastNameInput = registerPage.findElement(
				By.xpath("//input[@id = 'LastName']")
		);
		WebElement emailInput = registerPage.findElement(
				By.xpath("//input[@id = 'Email']")
		);
		
		WebElement passwordInput = registerPage.findElement(
				By.xpath("//input[@id = 'Password']")
		);
		WebElement passwordConfirmInput = registerPage.findElement(
				By.xpath("//input[@id = 'ConfirmPassword']")
		);
		
		WebElement registerButton = registerPage.findElement(
				By.xpath("//input[@id = 'register-button']")
		);
		
		genderMaleCheckbox.click();
		firstNameInput.sendKeys(user.name);
		lastNameInput.sendKeys(user.surname);
		emailInput.sendKeys(user.email);
		passwordInput.sendKeys(user.password);
		passwordConfirmInput.sendKeys(user.password);
		
		registerButton.click();
		
		WebElement continueButton = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//input[contains(@class, 'register-continue-button')]")
		));
		continueButton.click();
		
		driver.quit();
	}
	
	private WebElement navigatetoProductPage() {
		WebElement loginLink = driver.findElement(
				By.xpath("//div[contains(@class, 'header-links')]"
						+ "//*[@href = '/login']")
		);
		loginLink.click();
		
		WebElement loginPage = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[contains(@class, 'returning-wrapper')]")
		));
		WebElement emailInput = loginPage.findElement(
				By.xpath("//input[@id = 'Email']")
		);
		WebElement passwordInput = loginPage.findElement(
				By.xpath("//input[@id = 'Password']")
		);
		
		emailInput.sendKeys(user.email);
		passwordInput.sendKeys(user.password);
		
		WebElement loginButton = loginPage.findElement(
				By.xpath("//input[contains(@class, 'login-button')]")
		);
		loginButton.click();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[contains(@class, 'header-links')]"
						+ "//*[contains(@class, 'account')]")
		));
		
		WebElement digitalDownloadsLink = driver.findElement(
				By.xpath("//*[@href = '/digital-downloads']")
		);
		digitalDownloadsLink.click();
		
		return wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[contains(@class, 'product-grid')]")
		));
	}
	
	private void addProducts(String productFileName, WebElement productGrid) throws IOException {
		List<String> productNames = readProductNames(productFileName);
		for(String productName : productNames) {
//			WebElement productDetails = productGrid.findElement(
//				By.xpath("//div[contains(@class, 'details') and .//*[contains(text(), 'Music 2')]]")
//			);
//			
//			WebElement addToCartButton = productDetails.findElement(
//					By.xpath(".//input[contains(@class, 'product-box-add-to-cart-button')]")
//			);
			
			WebElement addToCartButton = productGrid.findElement(
				By.xpath("//div[contains(@class, 'details') and .//*[contains(text(), '" + productName + "')]]"
						+ "//input[contains(@class, 'product-box-add-to-cart-button')]")
			);
			addToCartButton.click();
			
			wait.until(ExpectedConditions.invisibilityOfElementLocated(
					By.xpath("//div[contains(@class, 'loading-image')]")
			));
		}
	}
		
	private void completePurchase() {
		WebElement shoppingCartLink = driver.findElement(
				By.xpath("//div[contains(@class, 'header-links')]"
						+ "//*[@id = 'topcartlink']")
		);
		shoppingCartLink.click();
		
		WebElement totals = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[contains(@class, 'totals')]")
		));
		WebElement termsCheckbox = totals.findElement(
				By.xpath("//input[@id = 'termsofservice']")
		);
		WebElement checkoutButton = totals.findElement(
				By.xpath("//button[@id = 'checkout']")
		);
		
		termsCheckbox.click();
		checkoutButton.click();
					
		WebElement checkout = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[contains(@class, 'checkout-page')]")
		));

		WebElement billingPage = checkout.findElement(
				By.xpath("//div[@id = 'checkout-step-billing']")
		);
		WebElement newAddressForm = billingPage.findElement(
				By.xpath("//div[@id = 'billing-new-address-form']")
		);
		
		if(newAddressForm.isDisplayed()) {
			WebElement countryBox = billingPage.findElement(
					By.xpath("//select[@id = 'BillingNewAddress_CountryId']")
			);
			WebElement cityInput = billingPage.findElement(
					By.xpath("//input[@id = 'BillingNewAddress_City']")
			);
			WebElement address1Input = billingPage.findElement(
					By.xpath("//input[@id = 'BillingNewAddress_Address1']")
			);
			WebElement postalCodeInput = billingPage.findElement(
					By.xpath("//input[@id = 'BillingNewAddress_ZipPostalCode']")
			);
			WebElement phoneNumberInput = billingPage.findElement(
					By.xpath("//input[@id = 'BillingNewAddress_PhoneNumber']")
			);
			
			Select countryDropdown = new Select(countryBox);
			countryDropdown.selectByVisibleText("Lithuania");
			
			cityInput.sendKeys("Vilnius");
			address1Input.sendKeys("Didlaukio g. 47");
			postalCodeInput.sendKeys("08303");
			phoneNumberInput.sendKeys("+37052195001");
		}
		
		String[] continueButtonClasses = {
				"new-address-next-step-button",
				"payment-method-next-step-button",
				"payment-info-next-step-button",
				"confirm-order-next-step-button"
		};
		for(String buttonClass : continueButtonClasses) {
			WebElement continueButton = checkout.findElement(
					By.xpath("//input[contains(@class, '" + buttonClass + "')]")
			);
			wait.until(ExpectedConditions.elementToBeClickable(continueButton));
			continueButton.click();
		}	
	}
	
	@Test
	public void testOrderPlacement1() {
		driver.get("https://demowebshop.tricentis.com/");
		
		WebElement productGrid = navigatetoProductPage();
		try {
			addProducts("data1.txt", productGrid);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		completePurchase();
		wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[contains(@class, 'order-completed')]"))
		);
	}
	
	@Test
	public void testOrderPlacement2() {
		driver.get("https://demowebshop.tricentis.com/");
		
		WebElement productGrid = navigatetoProductPage();
		try {
			addProducts("data2.txt", productGrid);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		completePurchase();
		wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[contains(@class, 'order-completed')]"))
		);
	}
}
