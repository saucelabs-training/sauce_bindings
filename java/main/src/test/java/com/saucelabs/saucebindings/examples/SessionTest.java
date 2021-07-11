package com.saucelabs.saucebindings.examples;

import com.saucelabs.saucebindings.*;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SessionTest {

    @Test
    public void startSession() {
        // 1. Create Session object with the defaults
        SauceSession session = new SauceSession();

        // 2. Start Session to get the Driver
        RemoteWebDriver driver = session.start();

        // 3. Use the driver in your tests just like normal
        driver.get("https://www.saucedemo.com/");

        // 4. Stop the Session with whether the test passed or failed
        session.stop(true);
    }
}
