package com.saucelabs.saucebindings.integration;

import com.saucelabs.saucebindings.SystemManager;
import com.saucelabs.saucebindings.VisualResults;
import com.saucelabs.saucebindings.VisualSession;
import com.saucelabs.saucebindings.VisualSnapshot;
import com.saucelabs.saucebindings.options.InvalidSauceOptionsArgumentException;
import com.saucelabs.saucebindings.options.SauceOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

import static org.junit.Assert.*;

public class VisualTest {
    private VisualSession session;

    @Before
    public void setup() {
        System.setProperty("BUILD_NAME", "Sauce Bindings");
    }

    @After
    public void cleanUp() {
        if (session != null) {
            session.stop(true);
        }
    }

    @Test
    public void usesVisualWhenOptionSet() {
        SauceOptions options = SauceOptions.chrome().setName("Visual uses this name").build();
        session = new VisualSession(options);
        RemoteWebDriver driver = session.start();
        driver.get("https://www.saucedemo.com");
        session.takeSnapshot("Snapshot necessary to for Visual test to pass");
        assertEquals("Snapshot necessary to for Visual test to pass",
                session.getVisualResults().getSnapshots().get(0).getName());
    }

    @Test
    public void takesSnapshot() {
        SauceOptions options = SauceOptions.chrome().setName("Takes Sauce Snapshot").build();
        session = new VisualSession(options);
        RemoteWebDriver driver = session.start();
        driver.get("https://www.saucedemo.com");
        session.takeSnapshot("Name of Snapshot");
        assertEquals("Name of Snapshot", session.getVisualResults().getSnapshots().get(0).getName());
    }

    @Test
    public void providesTestResults() {
        SauceOptions options = SauceOptions.chrome().setName("Provides Test Results").build();
        session = new VisualSession(options);
        RemoteWebDriver driver = session.start();
        driver.get("https://www.saucedemo.com");
        session.takeSnapshot("Name-of-Snapshot");
        VisualResults results = session.getVisualResults();
        assertTrue(results.getPassed());
        assertEquals("success", results.getStatus());
        assertNull(results.getMessage());

        assertEquals(Long.valueOf(1), results.getTotal());
        assertEquals(Long.valueOf(1), results.getTotalAccepted());
        assertEquals(Long.valueOf(0), results.getTotalNew());
        assertEquals(Long.valueOf(0), results.getTotalChanged());
        assertEquals(Long.valueOf(0), results.getTotalRejected());

        List<VisualSnapshot> states = results.getSnapshots();
        assertEquals(1, states.size());

        VisualSnapshot snapshot = results.getSnapshots().get(0);
        assertEquals("Name-of-Snapshot", snapshot.getName());
        assertEquals("Provides Test Results", snapshot.getGroupName());
        assertEquals("accepted", snapshot.getStatus());
        String url = (String) snapshot.getUrl();
        assertTrue(url.contains(SystemManager.getCurrentGitBranch()));
        assertTrue(url.contains("Name-of-Snapshot"));
        assertTrue(url.contains("Windows%2010"));
    }

    @Test
    public void errorsWithoutTestName() {
        try {
            new VisualSession(new SauceOptions());
            fail("Expected InvalidSauceOptionsArgumentException exception");
        } catch (InvalidSauceOptionsArgumentException exception) {
            String msg = "Visual Tests Require setting a name in options: SauceOptions#setName(Name)";
            assertEquals(msg, exception.getMessage());
        }
    }
}
