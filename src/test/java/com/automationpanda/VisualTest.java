package com.automationpanda;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class VisualTest {

    private static boolean headless;
    private static Configuration config;
    private static VisualGridRunner runner;

    private WebDriver driver;
    private Eyes eyes;

    @BeforeAll
    public static void setUpConfigAndRunner() {

        // Determine if Chrome should be headless
        headless = Boolean.parseBoolean(System.getenv().getOrDefault("HEADLESS", "false"));

        // Create the runner for the Ultrafast Grid
        // Warning: If you have a free account, then concurrency will be limited to 1
        runner = new VisualGridRunner(new RunnerOptions().testConcurrency(5));

        // Create a configuration for Applitools Eyes
        config = new Configuration();

        // Set the Applitools API key so test results are uploaded to your account
        config.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        // Create a new batch
        config.setBatch(new BatchInfo("A Visual Testing Revolution"));

        // Add 5 desktop browsers with different viewports to test in the Ultrafast Grid.
        config.addBrowser(800, 600, BrowserType.CHROME);
        config.addBrowser(700, 500, BrowserType.FIREFOX);
        config.addBrowser(1600, 1200, BrowserType.IE_11);
        config.addBrowser(1024, 768, BrowserType.EDGE_CHROMIUM);
        config.addBrowser(800, 600, BrowserType.SAFARI);

        // Add 5 mobile emulation devices with different orientations to test in the Ultrafast Grid
        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Pixel_2, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Galaxy_S5, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Nexus_10, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.iPad_Pro, ScreenOrientation.LANDSCAPE);
    }

    @BeforeEach
    public void setUpVisualAI(TestInfo testInfo) {

        // Initialize Selenium WebDriver
        driver = new ChromeDriver(new ChromeOptions().setHeadless(headless));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // Initialize Eyes
        eyes = new Eyes(runner);
        eyes.setConfiguration(config);

        // Open Eyes to start visual testing
        eyes.open(driver, "Applitools Demo App", testInfo.getDisplayName());
    }

    @Test
    public void login() {
        loadLoginPage();
        verifyLoginPage();
        performLogin();
        verifyMainPage();
    }

    @AfterEach
    public void cleanUpTest() {

        // Quit the WebDriver instance
        driver.quit();

        // Close Eyes to tell the server it should display the results
        eyes.close();
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
