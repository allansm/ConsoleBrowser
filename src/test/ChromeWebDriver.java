package test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeWebDriver {
	public ChromeWebDriver() {
		System.setProperty("webdriver.chrome.driver", "chromedriver\\chromedriver.exe");
	}
	public WebDriver getWebDriver() {
		ChromeOptions op = new ChromeOptions();
		op.setHeadless(true);
		WebDriver driver = new ChromeDriver(op);
		return driver;
	}
}
