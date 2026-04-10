package org.junit;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;

public class Tests1 {
	private WebDriver driver;
	private WebDriverWait wait;
	
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
	public void testSimple() {
		driver.get("https://www.google.com");
	}
	
	private void fillGiftCardFields(WebDriverWait wait) {
		WebElement giftCardOverview = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//*[@class = 'overview']")
		));
		
		WebElement recipientNameInput = giftCardOverview.findElement(
				By.xpath("descendant::input[contains(@class, 'recipient-name')]")
		);
		WebElement senderNameInput = giftCardOverview.findElement(
				By.xpath("descendant::input[contains(@class, 'sender-name')]")
		);
		
		recipientNameInput.sendKeys("Ernest");
		senderNameInput.sendKeys("Robas");
		
		WebElement quantityInput = giftCardOverview.findElement(
				By.xpath("descendant::input[contains(@class, 'qty-input')]")
		);
		WebElement addToCartButton = giftCardOverview.findElement(
				By.xpath("descendant::input[contains(@class, 'add-to-cart-button')]")
		);
		WebElement addToWishlistButton = giftCardOverview.findElement(
				By.xpath("descendant::input[contains(@class, 'add-to-wishlist-button')]")
		);
		
		quantityInput.clear();
		quantityInput.sendKeys("5000");
		
		addToCartButton.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[@id = 'bar-notification' and contains(@class, 'success')]")
		));
		
		addToWishlistButton.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[@id = 'bar-notification' and contains(@class, 'success')]")
		));
	}
	
	private void fillJewelryFields(WebDriverWait wait) {
		WebElement jewelryOverview = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//*[@class = 'overview']")
		));
		
		WebElement materialButton = jewelryOverview.findElement(
				By.xpath("descendant::select[@id = 'product_attribute_71_9_15']")
		);
		WebElement lengthInput = jewelryOverview.findElement(
				By.xpath("descendant::input[@id = 'product_attribute_71_10_16']")
		);
		WebElement starPendant = jewelryOverview.findElement(
				By.xpath("descendant::input[@id = 'product_attribute_71_11_17_50']")
		);
		WebElement quantityInput = jewelryOverview.findElement(
				By.xpath("descendant::input[contains(@class, 'qty-input')]")
		);
		WebElement addToCartButton = jewelryOverview.findElement(
				By.xpath("descendant::input[contains(@class, 'add-to-cart-button')]")
		);
		WebElement addToWishlistButton = jewelryOverview.findElement(
				By.xpath("descendant::input[contains(@class, 'add-to-wishlist-button')]")
		);
		
		Select materialSelection = new Select(materialButton);
		materialSelection.selectByContainsVisibleText("Silver (1 mm)");
		
		lengthInput.clear();
		lengthInput.sendKeys("80");
		
		starPendant.click();
		
		quantityInput.clear();
		quantityInput.sendKeys("26");
		
		addToCartButton.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[@id = 'bar-notification' and contains(@class, 'success')]")
		));
		
		addToWishlistButton.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[@id = 'bar-notification' and contains(@class, 'success')]")
		));
	}
	
	public void addWishlistedItemsToCart(WebDriverWait wait) {
		WebElement wishlistContent = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//*[contains(@class, 'wishlist-content')]")
		));
		
		List<WebElement> checkboxes = wishlistContent.findElements(
				By.xpath("descendant::*[contains(@class, 'add-to-cart')]/descendant::input")
		);
		checkboxes.forEach(WebElement::click);
		
		WebElement wishlistAddToCartButton = wishlistContent.findElement(
				By.xpath("descendant::input[contains(@class, 'wishlist-add-to-cart-button')]")
		);
		
		wishlistAddToCartButton.click();
	}
	
	@Test
	public void testSubtotal() {
		driver.get("https://demowebshop.tricentis.com/");
		
		WebElement giftCardButton = driver.findElement(
				By.xpath("//*[@href = '/gift-cards' and parent::*[contains(@class, 'inactive')]]") // //*[@href = '/gift-cards' and parent::*[count(@*)]]
		);
		giftCardButton.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[@href = '/gift-cards' and parent::*[contains(@class, 'active')]]")
		));
		
		
		WebElement giftCardPrices = driver.findElement(
				By.xpath("//*[contains(@class, 'actual-price') and number() > 99]/parent::*")
		);
		WebElement giftCardAddToCartButton = giftCardPrices.findElement(
				By.xpath("following-sibling::*/descendant::*[contains(@class, 'product-box-add-to-cart-button')]")
		);
		giftCardAddToCartButton.click();
		
		fillGiftCardFields(wait);
	
		
		WebElement jewelryButton = driver.findElement(
				By.xpath("//*[@href = '/jewelry' and parent::*[contains(@class, 'inactive')]]")
		);
		jewelryButton.click();
		
		WebElement createJewelryButton = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//*[@href = '/create-it-yourself-jewelry']")
		));
		createJewelryButton.click();
		
		fillJewelryFields(wait);
	
		WebElement wishlistButton = driver.findElement(
				By.xpath("//*[@href = '/wishlist' and contains(@class, 'ico-wishlist')]")
		);
		wishlistButton.click();
		
		addWishlistedItemsToCart(wait);
		
		
		WebElement cardTotalInfo = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//*[contains(@class, 'cart-total')]")
		));
		
		WebElement subTotal = cardTotalInfo.findElement(
				By.xpath("//*[contains(text(), 'Sub-Total')]/parent::*/following-sibling::*/descendant::*[contains(@class, 'product-price')]")
		);
		
		assertEquals("1002600.00", subTotal.getText());
	}
}
