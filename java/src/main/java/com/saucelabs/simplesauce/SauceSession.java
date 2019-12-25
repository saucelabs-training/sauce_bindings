package com.saucelabs.simplesauce;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;

public class SauceSession {
    @Getter private final String sauceDataCenter = DataCenter.US_WEST.getEndpoint();
    @Getter private final SauceOptions sauceOptions;
    @Getter private final SauceTimeout timeouts = new SauceTimeout();
    @Getter @Setter private String sauceUserName = System.getenv("SAUCE_USERNAME");
    @Getter @Setter private String sauceAccessKey = System.getenv("SAUCE_ACCESS_KEY");
    private final String sauceOptionsTag = "sauce:options";

    //TODO 2 same variables being used differently
    private MutableCapabilities mutableCapabilities;
    @Getter private MutableCapabilities currentSessionCapabilities;
    @Getter private final SauceRemoteDriver sauceDriver;
    @Getter private WebDriver webDriver;

    public MutableCapabilities getSauceOptionsCapability(){
        return ((MutableCapabilities) currentSessionCapabilities.getCapability(sauceOptionsTag));
    }

    public SauceSession() {
        currentSessionCapabilities = new MutableCapabilities();
        sauceDriver = new SauceDriverImpl();
        sauceOptions = new SauceOptions();
    }

    public SauceSession(SauceRemoteDriver remoteManager) {
        sauceDriver = remoteManager;
        currentSessionCapabilities = new MutableCapabilities();
        sauceOptions = new SauceOptions();
    }

    public SauceSession(SauceOptions options) {
        sauceOptions = options;
        currentSessionCapabilities = new MutableCapabilities();
        sauceDriver = new SauceDriverImpl();
    }

    public SauceSession(SauceOptions options, SauceRemoteDriver remoteManager) {
        sauceOptions = options;
        sauceDriver = remoteManager;
        currentSessionCapabilities = new MutableCapabilities();
    }

    public WebDriver start() {
        if (sauceUserName == null) {
            throw new SauceEnvironmentVariablesNotSetException("Sauce Username was not provided");
        } else if (sauceAccessKey == null) {
            throw new SauceEnvironmentVariablesNotSetException("Sauce Access Key was not provided");
        } else {
            mutableCapabilities = appendSauceCapabilities();
            setBrowserSpecificCapabilities(sauceOptions.getBrowserName());
            currentSessionCapabilities = setRemoteDriverCapabilities(mutableCapabilities);
            tryToCreateRemoteWebDriver(sauceDataCenter);
            return webDriver;
        }
	}

    private MutableCapabilities appendSauceCapabilities() {
        mutableCapabilities = new MutableCapabilities();
        mutableCapabilities.setCapability("username", getSauceUserName());
        mutableCapabilities.setCapability("accessKey", getSauceAccessKey());
        if (timeouts.getCommandTimeout() != 0) {
            mutableCapabilities.setCapability("commandTimeout", timeouts.getCommandTimeout());
        }
        if (timeouts.getIdleTimeout() != 0) {
            mutableCapabilities.setCapability("idleTimeout", timeouts.getIdleTimeout());
        }
        if (timeouts.getMaxTestDurationTimeout() != 0) {
            mutableCapabilities.setCapability("maxDuration", timeouts.getMaxTestDurationTimeout());
        }
        return mutableCapabilities;
    }

    private void setBrowserSpecificCapabilities(String browserName) {
        if (browserName.equalsIgnoreCase("Chrome")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            currentSessionCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        }
        else if (browserName.equalsIgnoreCase("Firefox")) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            currentSessionCapabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
        }
        else if(browserName.equalsIgnoreCase("Safari")) {
            SafariOptions safariOptions = new SafariOptions();
            currentSessionCapabilities.setCapability(SafariOptions.CAPABILITY, safariOptions);
        }
        else if(browserName.equalsIgnoreCase("Edge")) {
            EdgeOptions edgeOptions = new EdgeOptions();
            currentSessionCapabilities.setCapability("Edge", edgeOptions);
        }
        else if(browserName.equalsIgnoreCase("IE")) {
            InternetExplorerOptions ieOptions = new InternetExplorerOptions();
            currentSessionCapabilities.setCapability("se:ieOptions", ieOptions);
        }
        else {
            throw new IllegalArgumentException("The browserName=>" + browserName + " that you passed in is not a valid option.");
        }
    }

    private MutableCapabilities setRemoteDriverCapabilities(MutableCapabilities sauceOptions) {
        currentSessionCapabilities.setCapability(sauceOptionsTag, sauceOptions);
        currentSessionCapabilities.setCapability(CapabilityType.BROWSER_NAME, this.sauceOptions.getBrowserName());
        currentSessionCapabilities.setCapability(CapabilityType.PLATFORM_NAME, this.sauceOptions.getPlatformName());
        currentSessionCapabilities.setCapability(CapabilityType.BROWSER_VERSION, this.sauceOptions.getBrowserVersion());
        return currentSessionCapabilities;
    }

    private void tryToCreateRemoteWebDriver(String sauceLabsUrl) {
        try {
            webDriver = this.sauceDriver.createRemoteWebDriver(sauceLabsUrl, currentSessionCapabilities);
        }
        catch (MalformedURLException e) {
            throw new InvalidArgumentException("Invalid URL");
        }
    }

    public void stop() {
        if(webDriver !=null)
            webDriver.quit();
    }
}
