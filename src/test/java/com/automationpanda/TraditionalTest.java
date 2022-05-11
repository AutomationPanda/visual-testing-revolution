package com.automationpanda;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TraditionalTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void startWebDriver() throws Exception {

        String browserName = System.getenv().getOrDefault("BROWSER", "chrome");

        if (browserName.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        }
        else if (browserName.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        }
        else if (browserName.equalsIgnoreCase("safari")) {
            driver = new SafariDriver();
        }
        else if (browserName.equalsIgnoreCase("edge")) {
            String edgeDriverPath = System.getenv().get("EDGE_DRIVER_PATH");
            System.setProperty("webdriver.edge.driver", edgeDriverPath);
            driver = new EdgeDriver();
        }
        else {
            throw new Exception("Unsupported browser type: " + browserName);
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    public void quitWebDriver() {
        driver.quit();
    }

    @Test
    public void login() {
        loadLoginPage();
        verifyLoginPage();
        performLogin();
        verifyMainPage();
    }

    private void waitForAppearance(By locator) {
        wait.until(d -> d.findElements(locator).size() > 0);
    }

    private void loadLoginPage() {
        String site = System.getenv().getOrDefault("DEMO_SITE", "original");

        if (site.equals("original"))
            driver.get("https://demo.applitools.com");
        else
            driver.get("https://demo.applitools.com/index_v2.html");
    }

    private void verifyLoginPage() {
        waitForAppearance(By.cssSelector("div.logo-w"));
        waitForAppearance(By.id("username"));
        waitForAppearance(By.id("password"));
        waitForAppearance(By.id("log-in"));
        waitForAppearance(By.cssSelector("input.form-check-input"));
    }

    private void performLogin() {
        driver.findElement(By.id("username")).sendKeys("andy");
        driver.findElement(By.id("password")).sendKeys("i<3pandas");
        driver.findElement(By.id("log-in")).click();
    }

    private void verifyMainPage() {
        // Check various page elements
        waitForAppearance(By.cssSelector("div.logo-w"));
        waitForAppearance(By.cssSelector("div.element-search.autosuggest-search-activator > input"));
        waitForAppearance(By.cssSelector("div.avatar-w img"));
        waitForAppearance(By.cssSelector("ul.main-menu"));
        waitForAppearance(By.xpath("//a/span[.='Add Account']"));
        waitForAppearance(By.xpath("//a/span[.='Make Payment']"));
        waitForAppearance(By.xpath("//a/span[.='View Statement']"));
        waitForAppearance(By.xpath("//a/span[.='Request Increase']"));
        waitForAppearance(By.xpath("//a/span[.='Pay Now']"));

        // Check time message
        assertTrue(Pattern.matches(
                "Your nearest branch closes in:( \\d+[hms])+",
                driver.findElement(By.id("time")).getText()));

        // Check menu element names
        var menuElements = driver.findElements(By.cssSelector("ul.main-menu li span"));
        var menuItems = menuElements.stream().map(i -> i.getText().toLowerCase()).toList();
        var expected = Arrays.asList("card types", "credit cards", "debit cards", "lending", "loans", "mortgages");
        assertEquals(expected, menuItems);

        // Check transaction statuses
        var statusElements = driver.findElements(By.xpath("//td[./span[contains(@class, 'status-pill')]]/span[2]"));
        var statusNames = statusElements.stream().map(n -> n.getText().toLowerCase()).toList();
        var acceptableNames = Arrays.asList("complete", "pending", "declined");
        assertTrue(acceptableNames.containsAll(statusNames));
    }
}
