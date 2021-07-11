package com.saucelabs.saucebindings.examples;

import com.saucelabs.saucebindings.*;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DataCenterTest {

    @Test
    public void changeDataCenter() {
        // 1. Create Session object with the defaults
        SauceSession session = new SauceSession();

        // 2. Set Data Center
        session.setDataCenter(DataCenter.EU_CENTRAL);

        // 3. Start Session to get the Driver
        RemoteWebDriver driver = session.start();

        // 4. Use the driver in your tests just like normal
        driver.get("https://www.saucedemo.com/");

        // 5. Stop the Session with whether the test passed or failed
        session.stop(true);
    }
}
