package com.roman.tests;

import com.roman.util.Config;
import com.roman.util.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.net.MalformedURLException;
import java.net.URL;

import static com.roman.util.Constants.GRID_ENABLED;

public abstract class AbstractTest {

    public static final Logger log = LoggerFactory.getLogger(AbstractTest.class);

    protected WebDriver driver;

    @BeforeSuite
    public void setupConfig() {
        Config.initialize();
    }

    @BeforeTest
//    @Parameters({"browser"})
    public void setDriver() throws MalformedURLException {
        this.driver = Boolean.parseBoolean(Config.get(GRID_ENABLED)) ? getRemoteDriver() : getLocalDriver();
    }

    private WebDriver getLocalDriver() {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver();
    }

    private WebDriver getRemoteDriver() throws MalformedURLException {
        Capabilities capabilities = getCapabilitiesForBrowser(Config.get(Constants.BROWSER));
        String urlFormat = Constants.GRID_URL_FORMAT;
        String hubHost = Config.get(Constants.GRID__HUB_HOST);
        String url = String.format(urlFormat, hubHost);
        log.info("grid URL: {}", url);
        return new RemoteWebDriver(new URL(url), capabilities);
    }

    private static Capabilities getCapabilitiesForBrowser(String browser) {
        return switch (browser) {
            case Constants.FIREFOX -> new FirefoxOptions();
            case Constants.SAFARI -> new SafariOptions();
            default -> new ChromeOptions();
        };
    }

    @AfterTest
    public void quitDriver(){
        this.driver.quit();
    }

//    @AfterMethod
//    public void sleep() {
//        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(10));
//    }

}
