package com.saucelabs.saucebindings.integration;

import com.saucelabs.saucebindings.DataCenter;
import com.saucelabs.saucebindings.SauceOptions;
import com.saucelabs.saucebindings.SaucePlatform;
import com.saucelabs.saucebindings.SauceSession;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DesktopBrowserTest {
    private SauceSession session = new SauceSession();
    private RemoteWebDriver webDriver;

    @After
    public void cleanUp() {
        session.stop(true);
    }

    @Test
    public void defaultsToUSWest() {
        webDriver = session.start();
        assertNotNull(webDriver);
        assertTrue(session.getSauceUrl().toString().contains("us-west-"));
    }

    @Test
    public void runsUSEast() {
        SauceOptions options = new SauceOptions();
        options.setPlatformName(SaucePlatform.LINUX);
        session = new SauceSession(options);
        session.setDataCenter(DataCenter.US_EAST);
        webDriver = session.start();

        assertNotNull(webDriver);
        assertTrue(session.getSauceUrl().toString().contains("us-east-1"));
    }

    @Test
    public void runsEUCentral() {
        session.setDataCenter(DataCenter.EU_CENTRAL);
        webDriver = session.start();

        assertNotNull(webDriver);
        assertTrue(session.getSauceUrl().toString().contains("eu-central-1"));
    }
}
