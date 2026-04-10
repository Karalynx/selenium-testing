package org.junit;

import java.time.Duration;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Tests4 {

	private static WebDriver driver;
	private static WebDriverWait wait;
	private static User user = null;
	private static Integer totalVotes = null, firstAnswerVotes = null;
	
	private static void registerUser(WebDriver driver, WebDriverWait wait) {
		Tests4.user = new User();
		
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
	}
		
	@BeforeClass
	public static void getVoteCount() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--disable-gpu");
		options.addArguments("--window-size=1920,1080");
		
		WebDriver driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		
		driver.get("https://demowebshop.tricentis.com/");
		registerUser(driver, wait);
		
		 Optional<Tuple<Integer, Integer>> votes = castVote(driver, wait);
		 if(votes.isEmpty()) {
			 Assert.fail("Failed to parse vote counts");
		 }
		 
		 Tests4.totalVotes = votes.get().first;
		 Tests4.firstAnswerVotes = votes.get().second;
	}
	
	public static Optional<Tuple<Integer, Integer>> castVote(WebDriver driver, WebDriverWait wait) {
		WebElement voteBlock = driver.findElement(
				By.xpath("//div[@id = 'poll-block-1']")
		);
		
		WebElement firstAnswerButton = voteBlock.findElement(
				By.xpath(".//input[@id = 'pollanswers-1']")
		);
		
		WebElement voteButton = voteBlock.findElement(
				By.xpath(".//input[@id = 'vote-poll-1']")
		);
		
		firstAnswerButton.click();
		voteButton.click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(
				By.xpath(".//span[@id = 'poll-voting-progress-1']")
		));
		
		voteBlock = driver.findElement(
				By.xpath("//div[@id = 'poll-block-1']")
		);
		
		WebElement totalVotes = voteBlock.findElement(
				By.xpath(".//span[contains(@class, 'poll-total-votes')]")
		);
		
		WebElement firstAnswerVotes = voteBlock.findElement(
				By.xpath(".//ul[contains(@class, 'poll-results')]/li[1]")
		);
		
        Pattern pattern = Pattern.compile("\\b(\\d+) vote\\(s\\)");
        Matcher totalVoteMatcher = pattern.matcher(totalVotes.getText());
        Matcher firstAnswerVoteMatcher = pattern.matcher(firstAnswerVotes.getText());
        
        if(!totalVoteMatcher.find() || !firstAnswerVoteMatcher.find()) {
        	return Optional.empty();
        }
        
        return Optional.of(new Tuple<Integer, Integer>(
			Integer.parseInt(totalVoteMatcher.group(1)),
			Integer.parseInt(firstAnswerVoteMatcher.group(1))
		));
	}
	
	@Before
	public void setup() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		
		driver = new ChromeDriver(options);
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@After
	public void cleanup() {
		if(null != driver) {
			driver.quit();
		}
	}

	@Test
	public void testVoteCount() {
		driver.get("https://demowebshop.tricentis.com/");
		registerUser(Tests4.driver, Tests4.wait);
		
		 Optional<Tuple<Integer, Integer>> votes = castVote(Tests4.driver, Tests4.wait);
		 if(votes.isEmpty()) {
			 Assert.fail("Failed to parse vote counts");
		 }
		 
		 // System.out.printf("%d %d\n%d %d\n", Tests4.totalVotes, votes.get().first, Tests4.firstAnswerVotes, votes.get().second);
		 
		 Assert.assertTrue(votes.get().first > Tests4.totalVotes);
		 Assert.assertTrue(votes.get().second > Tests4.firstAnswerVotes);
	}
}
