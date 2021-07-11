package com.saucelabs.saucebindings.options;

import com.saucelabs.saucebindings.*;
import com.saucelabs.saucebindings.pixels.ScreenResolution;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.UnexpectedAlertBehaviour.DISMISS;

public class SauceOptionsTest {
    private SauceOptions sauceOptions = new SauceOptions();

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Test
    public void usesLatestChromeWindowsVersionsByDefault() {
        Assert.assertEquals(Browser.CHROME, sauceOptions.getBrowserName());
        Assert.assertEquals(SaucePlatform.WINDOWS_10, sauceOptions.getPlatformName());
        assertEquals("latest", sauceOptions.getBrowserVersion());
    }

    @Test
    public void updatesBrowserBrowserVersionPlatformVersionValues() {
        sauceOptions.setBrowserName(Browser.FIREFOX);
        sauceOptions.setPlatformName(SaucePlatform.MAC_HIGH_SIERRA);
        sauceOptions.setBrowserVersion("68");

        assertEquals(Browser.FIREFOX, sauceOptions.getBrowserName());
        assertEquals("68", sauceOptions.getBrowserVersion());
        assertEquals(SaucePlatform.MAC_HIGH_SIERRA, sauceOptions.getPlatformName());
    }

    @Test
    public void fluentPatternWorks() {
        sauceOptions.setBrowserName(Browser.FIREFOX)
                .setBrowserVersion("68")
                .setPlatformName(SaucePlatform.MAC_HIGH_SIERRA);

        assertEquals(Browser.FIREFOX, sauceOptions.getBrowserName());
        assertEquals("68", sauceOptions.getBrowserVersion());
        assertEquals(SaucePlatform.MAC_HIGH_SIERRA, sauceOptions.getPlatformName());
    }

    @Test
    public void acceptsOtherW3CValues() {
        sauceOptions.setAcceptInsecureCerts(true);
        sauceOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        sauceOptions.setSetWindowRect(true);
        sauceOptions.setUnhandledPromptBehavior(UnhandledPromptBehavior.IGNORE);
        sauceOptions.setStrictFileInteractability(true);

        sauceOptions.timeout.setImplicitWait(1000);
        sauceOptions.timeout.setPageLoad(100000);
        sauceOptions.timeout.setScript(10000);

        assertEquals(true, sauceOptions.getAcceptInsecureCerts());
        assertEquals(PageLoadStrategy.EAGER, sauceOptions.getPageLoadStrategy());
        assertEquals(true, sauceOptions.getSetWindowRect());
        assertEquals(UnhandledPromptBehavior.IGNORE, sauceOptions.getUnhandledPromptBehavior());
        assertEquals(true, sauceOptions.getStrictFileInteractability());
        assertEquals(Duration.ofSeconds(1), sauceOptions.getImplicitWaitTimeout());
        assertEquals(Duration.ofSeconds(100), sauceOptions.getPageLoadTimeout());
        assertEquals(Duration.ofSeconds(10), sauceOptions.getScriptTimeout());
    }

    @Test
    public void acceptsSauceLabsSettings() {
        Map<String, Object> customData = new HashMap<>();
        customData.put("foo", "foo");
        customData.put("bar", "bar");

        List<String> args = new ArrayList<>();
        args.add("--silent");
        args.add("-a");
        args.add("-q");

        Map<Prerun, Object> prerun = new HashMap<>();
        prerun.put(Prerun.EXECUTABLE, "http://url.to/your/executable.exe");
        prerun.put(Prerun.ARGS, args);
        prerun.put(Prerun.BACKGROUND, false);
        prerun.put(Prerun.TIMEOUT, 120);

        List<String> tags = new ArrayList<>();
        tags.add("Foo");
        tags.add("Bar");
        tags.add("Foobar");

        sauceOptions.sauce().setAvoidProxy(true);
        sauceOptions.sauce().setBuild("Sample Build Name");
        sauceOptions.sauce().setCapturePerformance(true);
        sauceOptions.sauce().setChromedriverVersion("71");
        sauceOptions.sauce().setCommandTimeout(2);
        sauceOptions.sauce().setCustomData(customData);
        sauceOptions.sauce().setExtendedDebugging(true);
        sauceOptions.sauce().setIdleTimeout(3);
        sauceOptions.sauce().setIedriverVersion("3.141.0");
        sauceOptions.sauce().setMaxDuration(300);
        sauceOptions.sauce().setName("Test name");
        sauceOptions.sauce().setParentTunnel("Mommy");
        sauceOptions.sauce().setPrerun(prerun);
        sauceOptions.sauce().setPriority(0);
        sauceOptions.sauce().setJobVisibility(JobVisibility.TEAM);
        sauceOptions.sauce().setRecordLogs(false);
        sauceOptions.sauce().setRecordScreenshots(false);
        sauceOptions.sauce().setRecordVideo(false);
        sauceOptions.sauce().setScreenResolution(ScreenResolution.XGA.getValue());
        sauceOptions.sauce().setSeleniumVersion("3.141.59");
        sauceOptions.sauce().setTags(tags);
        sauceOptions.sauce().setTimeZone("San Francisco");
        sauceOptions.sauce().setTunnelIdentifier("tunnelname");
        sauceOptions.sauce().setVideoUploadOnPass(false);

        assertEquals(true, sauceOptions.sauce().getAvoidProxy());
        assertEquals("Sample Build Name", sauceOptions.sauce().getBuild());
        assertEquals(true, sauceOptions.sauce().getCapturePerformance());
        assertEquals("71", sauceOptions.sauce().getChromedriverVersion());
        assertEquals(Integer.valueOf(2), sauceOptions.sauce().getCommandTimeout());
        assertEquals(customData, sauceOptions.sauce().getCustomData());
        assertEquals(true, sauceOptions.sauce().getExtendedDebugging());
        assertEquals(Integer.valueOf(3), sauceOptions.sauce().getIdleTimeout());
        assertEquals("3.141.0", sauceOptions.sauce().getIedriverVersion());
        assertEquals(Integer.valueOf(300), sauceOptions.sauce().getMaxDuration());
        assertEquals("Test name", sauceOptions.sauce().getName());
        assertEquals("Mommy", sauceOptions.sauce().getParentTunnel());
        assertEquals(prerun, sauceOptions.sauce().getPrerun());
        assertEquals(Integer.valueOf(0), sauceOptions.sauce().getPriority());
        assertEquals(JobVisibility.TEAM, sauceOptions.sauce().getJobVisibility());
        assertEquals(false, sauceOptions.sauce().getRecordLogs());
        assertEquals(false, sauceOptions.sauce().getRecordScreenshots());
        assertEquals(false, sauceOptions.sauce().getRecordVideo());
        assertEquals("1024x768", sauceOptions.sauce().getScreenResolution());
        assertEquals("3.141.59", sauceOptions.sauce().getSeleniumVersion());
        assertEquals(tags, sauceOptions.sauce().getTags());
        assertEquals("San Francisco", sauceOptions.sauce().getTimeZone());
        assertEquals("tunnelname", sauceOptions.sauce().getTunnelIdentifier());
        assertEquals(false, sauceOptions.sauce().getVideoUploadOnPass());
    }

    @Test
    public void createsDefaultBuildName() {
        assertNotNull(sauceOptions.sauce().getBuild());
    }

    @SneakyThrows
    public Map<String, Object> serialize(String key) {
        InputStream input = new FileInputStream(new File("src/test/java/com/saucelabs/saucebindings/options.yml"));
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(input);
        return (Map<String, Object>) data.get(key);
    }

    @Test
    public void setsCapabilitiesFromMap() {
        Map<String, Object> map = serialize("exampleValues");

        sauceOptions.mergeCapabilities(map);

        Map<String, Object> customData = new HashMap<>();
        customData.put("foo", "foo");
        customData.put("bar", "bar");

        List<String> args = new ArrayList<>();
        args.add("--silent");
        args.add("-a");
        args.add("-q");

        Map<Prerun, Object> prerun = new HashMap<>();
        prerun.put(Prerun.EXECUTABLE, "http://url.to/your/executable.exe");
        prerun.put(Prerun.ARGS, args);
        prerun.put(Prerun.BACKGROUND, false);
        prerun.put(Prerun.TIMEOUT, 120);

        List<String> tags = new ArrayList<>();
        tags.add("foo");
        tags.add("bar");
        tags.add("foobar");

        Map<String, Object> diffOptions = new HashMap<>();
        diffOptions.put("structure", true);
        diffOptions.put("layout", true);
        diffOptions.put("style", true);
        diffOptions.put("content", true);
        diffOptions.put("minLayoutPosition", 4);
        diffOptions.put("minLayoutDimension", 10);

        Map<String, Object> iframesOptions = new HashMap<>();
        iframesOptions.put("maxFrames", "Infinity");

        assertEquals(Browser.FIREFOX, sauceOptions.getBrowserName());
        assertEquals("68", sauceOptions.getBrowserVersion());
        assertEquals(SaucePlatform.MAC_HIGH_SIERRA, sauceOptions.getPlatformName());
        assertEquals(true, sauceOptions.getAcceptInsecureCerts());
        assertEquals(PageLoadStrategy.EAGER, sauceOptions.getPageLoadStrategy());
        assertEquals(true, sauceOptions.getSetWindowRect());
        assertEquals(UnhandledPromptBehavior.ACCEPT, sauceOptions.getUnhandledPromptBehavior());
        assertEquals(true, sauceOptions.getStrictFileInteractability());
        assertEquals(Duration.ofSeconds(1), sauceOptions.getImplicitWaitTimeout());
        assertEquals(Duration.ofSeconds(59), sauceOptions.getPageLoadTimeout());
        assertEquals(Duration.ofSeconds(29), sauceOptions.getScriptTimeout());
        assertEquals(true, sauceOptions.sauce().getAvoidProxy());
        assertEquals("Sample Build Name", sauceOptions.sauce().getBuild());
        assertEquals(true, sauceOptions.sauce().getCapturePerformance());
        assertEquals("71", sauceOptions.sauce().getChromedriverVersion());
        assertEquals(Integer.valueOf(2), sauceOptions.sauce().getCommandTimeout());
        assertEquals(customData, sauceOptions.sauce().getCustomData());
        assertEquals(true, sauceOptions.sauce().getExtendedDebugging());
        assertEquals(Integer.valueOf(3), sauceOptions.sauce().getIdleTimeout());
        assertEquals("3.141.0", sauceOptions.sauce().getIedriverVersion());
        assertEquals(Integer.valueOf(300), sauceOptions.sauce().getMaxDuration());
        assertEquals("Sample Test Name", sauceOptions.sauce().getName());
        assertEquals("Mommy", sauceOptions.sauce().getParentTunnel());
        assertEquals(prerun, sauceOptions.sauce().getPrerun());
        assertEquals(Integer.valueOf(0), sauceOptions.sauce().getPriority());
        assertEquals(JobVisibility.TEAM, sauceOptions.sauce().getJobVisibility());
        assertEquals(false, sauceOptions.sauce().getRecordLogs());
        assertEquals(false, sauceOptions.sauce().getRecordScreenshots());
        assertEquals(false, sauceOptions.sauce().getRecordVideo());
        assertEquals("10x10", sauceOptions.sauce().getScreenResolution());
        assertEquals("3.141.59", sauceOptions.sauce().getSeleniumVersion());
        assertEquals(tags, sauceOptions.sauce().getTags());
        assertEquals("San Francisco", sauceOptions.sauce().getTimeZone());
        assertEquals("tunnelname", sauceOptions.sauce().getTunnelIdentifier());
        assertEquals(false, sauceOptions.sauce().getVideoUploadOnPass());

        assertEquals("My Project", sauceOptions.visual().getProjectName());
        assertEquals("393x786", sauceOptions.visual().getViewportSize());
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

    @Test(expected = InvalidSauceOptionsArgumentException.class)
    public void setsBadBrowserFromMap()  {
        Map<String, Object> map = serialize("badBrowser");
        sauceOptions.mergeCapabilities(map);
    }

    @Test(expected = InvalidSauceOptionsArgumentException.class)
    public void setsBadPlatformFromMap() {
        Map<String, Object> map = serialize("badPlatform");
        sauceOptions.mergeCapabilities(map);
    }

    @Test(expected = InvalidSauceOptionsArgumentException.class)
    public void setsBadJobVisibilityFromMap() {
        Map<String, Object> map = serialize("badJobVisibility");
        sauceOptions.mergeCapabilities(map);
    }

    @Test(expected = InvalidSauceOptionsArgumentException.class)
    public void setsBadPromptFromMap() {
        Map<String, Object> map = serialize("badPrompt");
        sauceOptions.mergeCapabilities(map);
    }

    @Test(expected = InvalidSauceOptionsArgumentException.class)
    public void setsBadTimeoutFromMap() {
        Map<String, Object> map = serialize("badTimeout");
        sauceOptions.mergeCapabilities(map);
    }

    @Test(expected = InvalidSauceOptionsArgumentException.class)
    public void setsBadPrerunFromMap() {
        Map<String, Object> map = serialize("badPrerun");
        sauceOptions.mergeCapabilities(map);
    }

    @Test(expected = InvalidSauceOptionsArgumentException.class)
    public void setsBadPageLoadFromMap() {
        Map<String, Object> map = serialize("badPageLoad");
        sauceOptions.mergeCapabilities(map);
    }

    @Test
    public void parsesCapabilitiesFromW3CValues() {
        sauceOptions.setBrowserName(Browser.FIREFOX);
        sauceOptions.setPlatformName(SaucePlatform.MAC_BIG_SUR);
        sauceOptions.setBrowserVersion("77");
        sauceOptions.setAcceptInsecureCerts(true);
        sauceOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        sauceOptions.setSetWindowRect(true);
        sauceOptions.setUnhandledPromptBehavior(UnhandledPromptBehavior.IGNORE);
        sauceOptions.setStrictFileInteractability(true);
        sauceOptions.timeout.setImplicitWait(1000);
        sauceOptions.timeout.setPageLoad(100000);
        sauceOptions.timeout.setScript(10000);
        sauceOptions.sauce().setBuild("Build Name");

        Map<Timeouts, Integer> timeouts = new HashMap<>();
        timeouts.put(Timeouts.IMPLICIT, 1000);
        timeouts.put(Timeouts.SCRIPT, 10000);
        timeouts.put(Timeouts.PAGE_LOAD, 100000);

        MutableCapabilities expectedCapabilities = new MutableCapabilities();
        expectedCapabilities.setCapability("browserName", "firefox");
        expectedCapabilities.setCapability("browserVersion", "77");
        expectedCapabilities.setCapability("platformName", "macOS 11.00");
        expectedCapabilities.setCapability("acceptInsecureCerts", true);
        expectedCapabilities.setCapability("setWindowRect", true);
        expectedCapabilities.setCapability("strictFileInteractability", true);
        expectedCapabilities.setCapability("pageLoadStrategy", PageLoadStrategy.EAGER);
        expectedCapabilities.setCapability("timeouts", timeouts);
        expectedCapabilities.setCapability("unhandledPromptBehavior", UnhandledPromptBehavior.IGNORE);

        MutableCapabilities sauceCapabilities = new MutableCapabilities();
        sauceCapabilities.setCapability("build", "Build Name");
        sauceCapabilities.setCapability("username", SystemManager.get("SAUCE_USERNAME"));
        sauceCapabilities.setCapability("accessKey", SystemManager.get("SAUCE_ACCESS_KEY"));
        expectedCapabilities.setCapability("sauce:options", sauceCapabilities);
        MutableCapabilities actualCapabilities = sauceOptions.toCapabilities();

        assertEquals(expectedCapabilities.asMap().toString(), actualCapabilities.asMap().toString());
    }

    @Test
    public void parsesCapabilitiesFromSauceValues() {
        Map<String, Object> customData = new HashMap<>();
        customData.put("foo", "foo");
        customData.put("bar", "bar");

        List<String> args = new ArrayList<>();
        args.add("--silent");
        args.add("-a");
        args.add("-q");

        Map<Prerun, Object> prerun = new HashMap<>();
        prerun.put(Prerun.EXECUTABLE, "http://url.to/your/executable.exe");
        prerun.put(Prerun.ARGS, args);
        prerun.put(Prerun.BACKGROUND, false);
        prerun.put(Prerun.TIMEOUT, 120);

        List<String> tags = new ArrayList<>();
        tags.add("Foo");
        tags.add("Bar");
        tags.add("Foobar");

        sauceOptions.sauce().setAvoidProxy(true);
        sauceOptions.sauce().setBuild("Sample Build Name");
        sauceOptions.sauce().setCapturePerformance(true);
        sauceOptions.sauce().setChromedriverVersion("71");
        sauceOptions.sauce().setCommandTimeout(2);
        sauceOptions.sauce().setCustomData(customData);
        sauceOptions.sauce().setExtendedDebugging(true);
        sauceOptions.sauce().setIdleTimeout(3);
        sauceOptions.sauce().setIedriverVersion("3.141.0");
        sauceOptions.sauce().setMaxDuration(300);
        sauceOptions.sauce().setName("Test name");
        sauceOptions.sauce().setParentTunnel("Mommy");
        sauceOptions.sauce().setPrerun(prerun);
        sauceOptions.sauce().setPriority(0);
        sauceOptions.sauce().setJobVisibility(JobVisibility.TEAM);
        sauceOptions.sauce().setRecordLogs(false);
        sauceOptions.sauce().setRecordScreenshots(false);
        sauceOptions.sauce().setRecordVideo(false);
        sauceOptions.sauce().setScreenResolution(ScreenResolution.XGA.getValue());
        sauceOptions.sauce().setSeleniumVersion("3.141.59");
        sauceOptions.sauce().setTags(tags);
        sauceOptions.sauce().setTimeZone("San Francisco");
        sauceOptions.sauce().setTunnelIdentifier("tunnelname");
        sauceOptions.sauce().setVideoUploadOnPass(false);

        MutableCapabilities sauceCapabilities = new MutableCapabilities();
        sauceCapabilities.setCapability("avoidProxy", true);
        sauceCapabilities.setCapability("build", "Sample Build Name");
        sauceCapabilities.setCapability("capturePerformance", true);
        sauceCapabilities.setCapability("chromedriverVersion", "71");
        sauceCapabilities.setCapability("commandTimeout", 2);
        sauceCapabilities.setCapability("custom-data", customData);
        sauceCapabilities.setCapability("extendedDebugging", true);
        sauceCapabilities.setCapability("idleTimeout", 3);
        sauceCapabilities.setCapability("iedriverVersion", "3.141.0");
        sauceCapabilities.setCapability("maxDuration", 300);
        sauceCapabilities.setCapability("name", "Test name");
        sauceCapabilities.setCapability("parentTunnel", "Mommy");
        sauceCapabilities.setCapability("prerun", prerun);
        sauceCapabilities.setCapability("priority", 0);
        sauceCapabilities.setCapability("public", "team");
        sauceCapabilities.setCapability("recordLogs", false);
        sauceCapabilities.setCapability("recordScreenshots", false);
        sauceCapabilities.setCapability("recordVideo", false);
        sauceCapabilities.setCapability("screenResolution", "1024x768");
        sauceCapabilities.setCapability("seleniumVersion", "3.141.59");
        sauceCapabilities.setCapability("tags", tags);
        sauceCapabilities.setCapability("timeZone", "San Francisco");
        sauceCapabilities.setCapability("tunnelIdentifier", "tunnelname");
        sauceCapabilities.setCapability("videoUploadOnPass", false);
        sauceCapabilities.setCapability("username", SystemManager.get("SAUCE_USERNAME"));
        sauceCapabilities.setCapability("accessKey", SystemManager.get("SAUCE_ACCESS_KEY"));

        MutableCapabilities expectedCapabilities = new MutableCapabilities();
        expectedCapabilities.setCapability("browserName", "chrome");
        expectedCapabilities.setCapability("browserVersion", "latest");
        expectedCapabilities.setCapability("platformName", "Windows 10");

        expectedCapabilities.setCapability("sauce:options", sauceCapabilities);
        MutableCapabilities actualCapabilities = sauceOptions.toCapabilities();

        // toString() serializes the enums
        assertEquals(expectedCapabilities.asMap().toString(), actualCapabilities.asMap().toString());
    }

    @Test
    public void parsesCapabilitiesFromSeleniumValues() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--foo");
        firefoxOptions.addPreference("foo", "bar");
        firefoxOptions.setUnhandledPromptBehaviour(DISMISS);

        sauceOptions = SauceOptions.firefox(firefoxOptions).build();
        sauceOptions.sauce().setBuild("Build Name");

        MutableCapabilities expectedCapabilities = new MutableCapabilities();
        expectedCapabilities.setCapability("browserName", "firefox");
        expectedCapabilities.setCapability("browserVersion", "latest");
        expectedCapabilities.setCapability("platformName", "Windows 10");
        expectedCapabilities = expectedCapabilities.merge(firefoxOptions);

        MutableCapabilities sauceCapabilities = new MutableCapabilities();
        sauceCapabilities.setCapability("build", "Build Name");
        sauceCapabilities.setCapability("username", SystemManager.get("SAUCE_USERNAME"));
        sauceCapabilities.setCapability("accessKey", SystemManager.get("SAUCE_ACCESS_KEY"));
        expectedCapabilities.setCapability("sauce:options", sauceCapabilities);
        MutableCapabilities actualCapabilities = sauceOptions.toCapabilities();

        assertEquals(expectedCapabilities.asMap().toString(), actualCapabilities.asMap().toString());
    }

    @Test
    public void parsesW3CAndSauceAndSeleniumSettings() {
        MutableCapabilities expectedCapabilities = new MutableCapabilities();
        MutableCapabilities sauceCapabilities = new MutableCapabilities();

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--foo");
        firefoxOptions.setUnhandledPromptBehaviour(DISMISS);

        sauceOptions = SauceOptions.firefox(firefoxOptions).build();

        expectedCapabilities = expectedCapabilities.merge(firefoxOptions);
        expectedCapabilities.setCapability("browserVersion", "latest");
        expectedCapabilities.setCapability("platformName", "Windows 10");
        expectedCapabilities.setCapability("acceptInsecureCerts", true);

        sauceOptions.sauce().setBuild("CUSTOM BUILD: 12");
        sauceCapabilities.setCapability("build", "CUSTOM BUILD: 12");

        sauceOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        expectedCapabilities.setCapability("pageLoadStrategy", PageLoadStrategy.EAGER);
        sauceOptions.setAcceptInsecureCerts(true);
        expectedCapabilities.setCapability("acceptInsecureCerts", true);
        sauceOptions.timeout.setImplicitWait(1000)
                .setPageLoad(100000)
                .setScript(10000);
        Map<Timeouts, Integer> timeouts = new HashMap<>();
        timeouts.put(Timeouts.IMPLICIT, 1000);
        timeouts.put(Timeouts.SCRIPT, 10000);
        timeouts.put(Timeouts.PAGE_LOAD, 100000);
        expectedCapabilities.setCapability("timeouts", timeouts);
        sauceOptions.setUnhandledPromptBehavior(UnhandledPromptBehavior.IGNORE);
        expectedCapabilities.setCapability("unhandledPromptBehavior", UnhandledPromptBehavior.IGNORE);

        sauceOptions.sauce().setExtendedDebugging(true);
        sauceCapabilities.setCapability("extendedDebugging", true);
        sauceOptions.sauce().setName("Test name");
        sauceCapabilities.setCapability("name", "Test name");
        sauceOptions.sauce().setParentTunnel("Mommy");
        sauceCapabilities.setCapability("parentTunnel", "Mommy");

        sauceOptions.sauce().setJobVisibility(JobVisibility.SHARE);
        sauceCapabilities.setCapability("public", JobVisibility.SHARE);
        sauceCapabilities.setCapability("username", SystemManager.get("SAUCE_USERNAME"));
        sauceCapabilities.setCapability("accessKey", SystemManager.get("SAUCE_ACCESS_KEY"));

        expectedCapabilities.setCapability("sauce:options", sauceCapabilities);
        MutableCapabilities actualCapabilities = sauceOptions.toCapabilities();

        assertEquals(expectedCapabilities.asMap().toString(), actualCapabilities.asMap().toString());
    }
}
