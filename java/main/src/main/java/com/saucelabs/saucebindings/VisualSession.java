package com.saucelabs.saucebindings;

import com.saucelabs.saucebindings.options.InvalidSauceOptionsArgumentException;
import com.saucelabs.saucebindings.options.SauceOptions;
import com.saucelabs.saucebindings.options.VisualOptions;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class VisualSession extends SauceSession {
    public VisualSession(SauceOptions options) {
        super(options);
        if (options.sauce().getName() == null) {
            String msg = "Visual Tests Require setting a name in options: SauceOptions#setName(Name)";
            throw new InvalidSauceOptionsArgumentException(msg);
        }
        if (options.visual() == null) {
            options.setVisualOptions(new VisualOptions());
        }
    }

    @Override
    public URL getSauceUrl() {
        try {
            return new URL("https://hub.screener.io/wd/hub");
        } catch (MalformedURLException e) {
            throw new InvalidArgumentException("Invalid URL", e);
        }
    }

    public RemoteWebDriver start() {
        super.start();
        init(getSauceOptions().sauce().getName());
        return driver;
    }

    @Override
    public void stop() {
        VisualResults results = getVisualResults();
        System.out.println(results.resultSummary());
        super.stop();
    }

    public void init(String name) {
        driver.executeScript("/*@visual.init*/", name);
    }

    public VisualResults getVisualResults() {
        Map results = (Map) driver.executeScript("/*@visual.end*/");
        return new VisualResults(results);
    }

    public void takeSnapshot(String name) {
        driver.executeScript("/*@visual.snapshot*/", name);
    }
}
