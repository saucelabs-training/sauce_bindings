package com.saucelabs.saucebindings.examples;

import com.saucelabs.saucebindings.SaucePlatform;
import com.saucelabs.saucebindings.VisualResults;
import com.saucelabs.saucebindings.VisualSession;
import com.saucelabs.saucebindings.options.SauceOptions;
import com.saucelabs.saucebindings.options.VisualOptions;
import com.saucelabs.saucebindings.pixels.Google;
import com.saucelabs.saucebindings.pixels.ViewportSize;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

public class VisualTest {

	@Test
	public void startSession() {
		// 1. Create Visual Options instance
		VisualOptions visualOptions = new VisualOptions();
		visualOptions.setProjectName("My Project")
				.setViewportSize(ViewportSize.google(Google.PIXEL_3));

		// 2. Create Sauce Options.
		//    Use setVisualOptions() to add the visual options
		//    Make sure setName() has a value
		SauceOptions sauceOptions = SauceOptions.chrome()
				.setName("My Test Name")
				.setPlatformName(SaucePlatform.MAC_HIGH_SIERRA)
				.setBrowserVersion("85")
				.setVisualOptions(visualOptions)
				.build();

		// 3. Start the Session with SauceOptions
		VisualSession session = new VisualSession(sauceOptions);

		// 4. Start Session to get the Driver
		RemoteWebDriver driver = session.start();

		// 5. Use the driver in your tests just like normal
		driver.get("https://www.saucedemo.com/");

		// 6. Use the session to take a snapshot
		session.takeSnapshot("Sauce Demo Page");

		// 7. Use the session to get visual results if desired
		VisualResults results = session.getVisualResults();

		// 8. Stop the Session with whether the test passed or failed
		session.stop(true);
	}
}
