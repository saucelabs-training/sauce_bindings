package com.saucelabs.saucebindings.options;

import com.saucelabs.saucebindings.SaucePlatform;
import com.saucelabs.saucebindings.SystemManager;
import com.saucelabs.saucebindings.pixels.Apple;
import com.saucelabs.saucebindings.pixels.Samsung;
import com.saucelabs.saucebindings.pixels.ViewportSize;
import org.junit.Test;
import org.openqa.selenium.MutableCapabilities;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class VisualOptionsTest {
    @Test
    public void defaultsNoVisualOptions() {
        SauceOptions sauceOptions = SauceOptions.chrome().build();
        assertNull(sauceOptions.visual());
    }

    @Test
    public void acceptsVisualOptions() {
        Map<String, Object> diffOptions = new HashMap<>();
        diffOptions.put("structure", true);
        diffOptions.put("layout", true);
        diffOptions.put("style", true);
        diffOptions.put("content", true);
        diffOptions.put("minLayoutPosition", 4);
        diffOptions.put("minLayoutDimension", 10);

        Map<String, Object> iframesOptions = new HashMap<>();
        iframesOptions.put("maxFrames", "Infinity");

        VisualOptions visualOptions = new VisualOptions();
        visualOptions
                .setProjectName("My Project")
                .setViewportSize(ViewportSize.apple(Apple.IPAD_9_7))
                .setBranch("branch")
                .setBaseBranch("baseBranch")
                .setDiffOptions(diffOptions)
                .setIgnore("#some-id, .some-selector")
                .setFailOnNewStates(true)
                .setAlwaysAcceptBaseBranch(true)
                .setDisableBranchBaseline(true)
                .setScrollAndStitchScreenshots(true)
                .setDisableCORS(true)
                .setIframes(true)
                .setIframesOptions(iframesOptions);

        SauceOptions sauceOptions = SauceOptions.chrome()
                .setPlatformName(SaucePlatform.MAC_HIGH_SIERRA)
                .setBrowserVersion("68")
                .setVisualOptions(visualOptions)
                .build();

        assertEquals("My Project", sauceOptions.visual().getProjectName());
        assertEquals("768x1024", sauceOptions.visual().getViewportSize());
        assertEquals("baseBranch", sauceOptions.visual().getBaseBranch());
        assertEquals(diffOptions, sauceOptions.visual().getDiffOptions());
        assertEquals("#some-id, .some-selector", sauceOptions.visual().getIgnore());
        assertTrue(sauceOptions.visual().getFailOnNewStates());
        assertTrue(sauceOptions.visual().getAlwaysAcceptBaseBranch());
        assertTrue(sauceOptions.visual().getDisableBranchBaseline());
        assertTrue(sauceOptions.visual().getScrollAndStitchScreenshots());
        assertTrue(sauceOptions.visual().getDisableCORS());
        assertTrue(sauceOptions.visual().getIframes());
        assertEquals(iframesOptions, sauceOptions.visual().getIframesOptions());
    }

    @Test
    public void createsDefaultVisualValues() {
        VisualOptions visualOptions = new VisualOptions();

        MutableCapabilities visualCapabilities = new MutableCapabilities();
        visualCapabilities.setCapability("apiKey", System.getenv("SCREENER_API_KEY"));
        visualCapabilities.setCapability("branch", SystemManager.getCurrentGitBranch());
        visualCapabilities.setCapability("projectName", visualOptions.getDefaultBuildName());

        SauceOptions sauceOptions = new SauceOptions().setVisualOptions(visualOptions);

        assertEquals(visualCapabilities, sauceOptions.toCapabilities().getCapability("sauce:visual"));
    }

    @Test
    public void parsesCapabilitiesFromVisualOptions() {
        Map<String, Object> diffOptions = new HashMap<>();
        diffOptions.put("structure", true);
        diffOptions.put("layout", true);
        diffOptions.put("style", true);
        diffOptions.put("content", true);
        diffOptions.put("minLayoutPosition", 4);
        diffOptions.put("minLayoutDimension", 10);

        Map<String, Object> iframesOptions = new HashMap<>();
        iframesOptions.put("maxFrames", "Infinity");

        VisualOptions visualOptions = new VisualOptions();
        visualOptions
                .setProjectName("My Project")
                .setViewportSize(ViewportSize.samsung(Samsung.GALAXY_NOTE_8))
                .setBranch("branch")
                .setBaseBranch("baseBranch")
                .setDiffOptions(diffOptions)
                .setIgnore("#some-id, .some-selector")
                .setFailOnNewStates(true)
                .setAlwaysAcceptBaseBranch(true)
                .setDisableBranchBaseline(true)
                .setScrollAndStitchScreenshots(true)
                .setDisableCORS(true)
                .setIframes(true)
                .setIframesOptions(iframesOptions);

        SauceOptions sauceOptions = SauceOptions.chrome()
                .setPlatformName(SaucePlatform.MAC_HIGH_SIERRA)
                .setBrowserVersion("68")
                .setName("Parses Capabilities From Visual Options")
                .setVisualOptions(visualOptions)
                .build();

        MutableCapabilities visualCapabilities = new MutableCapabilities();
        visualCapabilities.setCapability("projectName", "My Project");
        visualCapabilities.setCapability("viewportSize", "414x846");
        visualCapabilities.setCapability("branch", "branch");
        visualCapabilities.setCapability("baseBranch", "baseBranch");
        visualCapabilities.setCapability("diffOptions", diffOptions);
        visualCapabilities.setCapability("ignore", "#some-id, .some-selector");
        visualCapabilities.setCapability("failOnNewStates", true);
        visualCapabilities.setCapability("alwaysAcceptBaseBranch", true);
        visualCapabilities.setCapability("disableBranchBaseline", true);
        visualCapabilities.setCapability("scrollAndStitchScreenshots", true);
        visualCapabilities.setCapability("disableCORS", true);
        visualCapabilities.setCapability("iframes", true);
        visualCapabilities.setCapability("iframesOptions", iframesOptions);
        visualCapabilities.setCapability("apiKey", System.getenv("SCREENER_API_KEY"));

        assertEquals(visualCapabilities, sauceOptions.toCapabilities().getCapability("sauce:visual"));
    }
}
