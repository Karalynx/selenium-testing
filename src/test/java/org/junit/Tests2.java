package org.junit;

import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Tests2 {
	private WebDriver driver;
	private WebDriverWait wait;
	
	@Before
	public void setup() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		
		driver = new ChromeDriver(options);
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}
	
	@After
	public void cleanup() {
		if(null != driver) {
			driver.quit();
		}
	}

	@Test
	public void testProgressBar() {
		driver.get("https://demoqa.com/");

		WebElement widgetCard = driver.findElement(
				By.xpath("//*[contains(@class, 'card-body') and .//*[text() = 'Widgets']]/parent::*")
		);
		widgetCard.click();
		
		WebElement widgetMenu = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//*[contains(@class, 'group-header') and .//*[text() = 'Widgets']]")
		));
		WebElement progressBarButton = widgetMenu.findElement(
				By.xpath("//*[contains(@class, 'menu-list')]//*[text() = 'Progress Bar']/parent::*")
		);
		progressBarButton.click();
		
		WebElement progressBarContainer = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[@id = 'progressBarContainer']")
		));
		
		WebElement startButton = progressBarContainer.findElement(
				By.xpath("//button[@id = 'startStopButton']")
		);
		startButton.click();
		
		WebElement resetButton = wait.until(driver -> progressBarContainer.findElement(
				By.xpath("//button[@id = 'resetButton']")
		));
		resetButton.click();
		
		WebElement progressBarPercentage = progressBarContainer.findElement(
				By.xpath("//div[contains(@class, 'progress-bar')]")
		);
		
		String percentage = progressBarPercentage.getDomAttribute("aria-valuenow");
		assertEquals("0", percentage);
	}
	
	@Test
	public void testPagination() {
		driver.get("https://demoqa.com/");
		
		WebElement elementsCard = driver.findElement(
				By.xpath("//*[contains(@class, 'card-body') and .//*[text() = 'Elements']]/parent::*")
		);
		elementsCard.click();
		
		WebElement elementsMenu = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//*[contains(@class, 'group-header') and .//*[text() = 'Elements']]")
		));
		WebElement webTablesButton = elementsMenu.findElement(
				By.xpath("//*[contains(@class, 'menu-list')]//*[text() = 'Web Tables']/parent::*")
		);
		webTablesButton.click();
		
		WebElement tablesWrapper = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//*[contains(@class, 'web-tables-wrapper')]")
		));
		
		WebElement addButton = tablesWrapper.findElement(
				By.xpath("//button[@id = 'addNewRecordButton']")
		);
		
		WebElement pageBottom = tablesWrapper.findElement(
				By.xpath("//div[contains(@class, 'pagination-bottom')]")
		);
		
		WebElement currentPage = pageBottom.findElement(
				By.xpath("//*[contains(@class, 'pageJump')]//input")
		);
		
		WebElement totalPages = pageBottom.findElement(
				By.xpath("//*[contains(@class, 'totalPages')]")
		);
		//wait until custom expected condition
		wait.until(driver -> {
			if(totalPages.getText().trim().equals("2")) {
				return true;
			}
			else {
				addButton.click();
				
				WebElement userForm = wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//form[@id = 'userForm']")
				));
				
				WebElement firstNameInput = userForm.findElement(
						By.xpath("//input[@id = 'firstName']")
				);
				WebElement lastNameInput = userForm.findElement(
						By.xpath("//input[@id = 'lastName']")
				);
				WebElement emailInput = userForm.findElement(
						By.xpath("//input[@id = 'userEmail']")
				);
				WebElement ageInput = userForm.findElement(
						By.xpath("//input[@id = 'age']")
				);
				WebElement salaryInput = userForm.findElement(
						By.xpath("//input[@id = 'salary']")
				);
				WebElement departmentInput = userForm.findElement(
						By.xpath("//input[@id = 'department']")
				);
				
				WebElement submitButton = userForm.findElement(
						By.xpath("//button[@id = 'submit']")
				);
				
				firstNameInput.sendKeys("Ernestas");
				lastNameInput.sendKeys("Dubietis");
				emailInput.sendKeys("ernestasdubietis@gmail.com");
				ageInput.sendKeys("21");
				salaryInput.sendKeys("9000");
				departmentInput.sendKeys("IT");
				
				submitButton.click();
				
				wait.until(ExpectedConditions.invisibilityOfElementLocated(
						By.xpath("//form[@id = 'userForm']")
				));
				
				return false;
			}
		});
		
		WebElement nextButton = pageBottom.findElement(
				By.xpath("//*[contains(@class, 'next')]//button")
		);
		nextButton.click();
		
//		wait.until(ExpectedConditions.textToBePresentInElementValue(
//				currentPage, "2")
//		);
		
		WebElement trashButton = tablesWrapper.findElement(
				By.xpath("//*[contains(@class, 'action-buttons')]//*[@title = 'Delete']")
		);
		trashButton.click();
		
		assertEquals(currentPage.getText().trim(), "1");
		assertEquals(totalPages.getText().trim(), "1");
	}
}
