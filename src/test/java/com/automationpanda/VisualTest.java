package com.automationpanda;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class VisualTest {

    private WebDriver driver;
    private VisualGridRunner runner;
    private Eyes eyes;

    @BeforeEach
    public void setUpVisualAI() {

        // Determine if Chrome should be headless
        boolean headless = System.getenv().getOrDefault("HEADLESS", "false")
                .equalsIgnoreCase("true");

        // Prepare Eyes and Ultrafast Grid for Selenium WebDriver
        driver = new ChromeDriver(new ChromeOptions().setHeadless(headless));
        runner = new VisualGridRunner(new RunnerOptions().testConcurrency(5));
        eyes = new Eyes(runner);

        // Initialize Eyes Configuration
        Configuration config = eyes.getConfiguration();

        // You can get your API key from the Applitools dashboard
        config.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        // Create a new batch
        config.setBatch(new BatchInfo("A Visual Testing Revolution"));

        // Add browsers with different viewports
        config.addBrowser(800, 600, BrowserType.CHROME);
        config.addBrowser(700, 500, BrowserType.FIREFOX);
        config.addBrowser(1600, 1200, BrowserType.IE_11);
        config.addBrowser(1024, 768, BrowserType.EDGE_CHROMIUM);
        config.addBrowser(800, 600, BrowserType.SAFARI);

        // Add mobile emulation devices in Portrait mode
        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Pixel_2, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Galaxy_S5, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Nexus_10, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.iPad_Pro, ScreenOrientation.LANDSCAPE);

        // Set the configuration object to Eyes
        eyes.setConfiguration(config);
    }

    @AfterEach
    public void cleanUpTest() {
        // Quit the WebDriver instance
        driver.quit();

        // Report visual differences
        TestResultsSummary allTestResults = runner.getAllTestResults(true);
        System.out.println(allTestResults);
    }

    @Test
    public void login() {
        try {
            // Open Eyes to start visual testing
            eyes.open(
                    driver,
                    "Applitools Demo App",
                    "A visual login test");

            // Run the test steps, but with visual checks
            loadLoginPage();
            verifyLoginPage();
            performLogin();
            verifyMainPage();

            // Close Eyes to tell the server it should display the results
            eyes.closeAsync();
        }
        finally {
            // Notify the server if the test aborts
            eyes.abortAsync();
        }
    }

    private void loadLoginPage() {
        String site = System.getenv().getOrDefault("DEMO_SITE", "original");

        if (site.equals("original"))
            driver.get("https://demo.applitools.com");
        else
            driver.get("https://demo.applitools.com/index_v2.html");
    }

    private void verifyLoginPage() {
        eyes.check(Target.window().fully().withName("Login page"));
    }

    private void performLogin() {
        driver.findElement(By.id("username")).sendKeys("andy");
        driver.findElement(By.id("password")).sendKeys("i<3pandas");
        driver.findElement(By.id("log-in")).click();
    }

    private void verifyMainPage() {
        // This snapshot uses LAYOUT match level to avoid differences in
        // "Your nearest branch closes in: ..." times
        eyes.check(Target.window().fully().withName("Main page").layout());
    }
}
