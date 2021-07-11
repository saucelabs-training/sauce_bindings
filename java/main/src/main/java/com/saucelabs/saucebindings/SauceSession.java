package com.saucelabs.saucebindings;

import com.saucelabs.saucebindings.options.BaseConfigurations;
import com.saucelabs.saucebindings.options.SauceOptions;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class SauceSession {
    @Getter @Setter private DataCenter dataCenter = DataCenter.US_WEST;
    @Getter private final SauceOptions sauceOptions;
    @Setter private URL sauceUrl;

    @Getter protected RemoteWebDriver driver;

    public SauceSession() {
        this(new SauceOptions());
    }

    /**
     * Ideally the end user calls build() on Configurations instance
     * this constructor is being accommodating in case they do not
     *
     * @param configs the instance of Configuration used to create the Options
     */
    public SauceSession(BaseConfigurations configs) {
        this(configs.build());
    }

    public SauceSession(SauceOptions options) {
        sauceOptions = options;
    }

    public RemoteWebDriver start() {
        this.driver = createRemoteWebDriver(getSauceUrl(), sauceOptions.toCapabilities());
        return driver;
	}

    public URL getSauceUrl() {
        try {
            if (sauceUrl != null) {
                return sauceUrl;
            } else {
                return new URL(getDataCenter().getValue());
            }
        } catch (MalformedURLException e) {
            throw new InvalidArgumentException("Invalid URL", e);
        }
    }

    protected RemoteWebDriver createRemoteWebDriver(URL url, MutableCapabilities capabilities) {
        return new RemoteWebDriver(url, capabilities);
    }

    public void stop(Boolean passed) {
        String result = passed ? "passed" : "failed";
        stop(result);
    }

    public void stop(String result) {
        if (this.driver != null) {
            updateResult(result);
            stop();
        }
    }

    private void updateResult(String result) {
        getDriver().executeScript("sauce:job-result=" + result);

        // Add output for the Sauce OnDemand Jenkins plugin
        // The first print statement will automatically populate links on Jenkins to Sauce
        // The second print statement will output the job link to logging/console
        String sauceReporter = String.format("SauceOnDemandSessionID=%s job-name=%s",
                this.driver.getSessionId(),
                this.sauceOptions.sauce().getName());
        String sauceTestLink = String.format("Test Job Link: https://app.saucelabs.com/tests/%s",
                this.driver.getSessionId());
        System.out.print(sauceReporter + "\n" + sauceTestLink + "\n");
    }

    protected void stop() {
        driver.quit();
    }
}
