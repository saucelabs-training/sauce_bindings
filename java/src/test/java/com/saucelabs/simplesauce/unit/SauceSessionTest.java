package com.saucelabs.simplesauce.unit;

import com.saucelabs.simplesauce.ConcreteRemoteDriver;
import com.saucelabs.simplesauce.DataCenter;
import com.saucelabs.simplesauce.SauceEnvironmentVariablesNotSetException;
import com.saucelabs.simplesauce.SauceSession;
import com.saucelabs.simplesauce.interfaces.EnvironmentManager;
import com.saucelabs.simplesauce.interfaces.RemoteDriverInterface;
import org.hamcrest.core.IsNot;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SauceSessionTest {

    private SauceSession concreteSauceSession;
    private SauceSession fakeSauceSession;
    private EnvironmentManager fakeEnvironmentManager;

    @Before
    public void setUp()
    {
        concreteSauceSession = new SauceSession();
        RemoteDriverInterface fakeRemoteDriver = mock(RemoteDriverInterface.class);
        fakeEnvironmentManager = mock(EnvironmentManager.class);
        fakeSauceSession = new SauceSession(fakeRemoteDriver, fakeEnvironmentManager);
        //TODO move the 2 lines below to the tests that are relevant. This isn't relevant to all of the tests.
        //However, I did this so that I can leave the code cleaner by removing checked exceptions
        //when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_USERNAME")).thenReturn("test-name");
        //when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_ACCESS_KEY")).thenReturn("accessKey");
    }

    @Test
    public void startSession_defaultConfig_usWestDataCenter() throws MalformedURLException
    {
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_USERNAME")).thenReturn("test-name");
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_ACCESS_KEY")).thenReturn("accessKey");

        fakeSauceSession.start();

        String expectedDataCenterUrl = DataCenter.USWest;
        assertEquals(fakeSauceSession.sauceDataCenter,
                IsEqualIgnoringCase.equalToIgnoringCase(expectedDataCenterUrl));
    }
    @Test(expected = SauceEnvironmentVariablesNotSetException.class)
    public void getUserName_usernameNotSetInEnvironmentVariable_throwsException() {
        fakeSauceSession.getUserName();
    }
    @Test
    public void getUserName_usernameSetInEnvironmentVariable_returnsValue()  {
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_USERNAME")).thenReturn("test-name");
        String actualUserName = fakeSauceSession.getUserName();
        assertEquals(actualUserName, IsNot.not(""));

    }
    @Test(expected = SauceEnvironmentVariablesNotSetException.class)
    public void getAccessKey_keyNotSetInEnvironmentVariable_throwsException()  {
        fakeSauceSession.getAccessKey();
    }
    @Test
    public void getAccessKey_keySetInEnvironmentVariable_returnsValue() {
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_ACCESS_KEY")).thenReturn("accessKey");
        String actualAccessKey = fakeSauceSession.getAccessKey();
        assertEquals(actualAccessKey, IsNot.not(""));
    }
    @Test
    public void defaultConstructor_instantiated_setsConcreteDriverManager()
    {
        assertEquals(concreteSauceSession.getDriverManager(), instanceOf(ConcreteRemoteDriver.class));
    }

    @Test
    public void startSession_setsBrowserKey() throws MalformedURLException {
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_USERNAME")).thenReturn("test-name");
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_ACCESS_KEY")).thenReturn("accessKey");

        fakeSauceSession.start();
        String expectedBrowserCapabilityKey = "browserName";
        String actualBrowser = fakeSauceSession.sauceSessionCapabilities.getCapability(expectedBrowserCapabilityKey).toString();
        assertNotEquals("", actualBrowser);
    }
    @Test
    @Ignore("The problem with this approach is that you need to know which method" +
        "to call to get the desired behavior. However, if we move the logic out from" +
        "the setSauceOptions() method into another method, this test will no longer work." +
        "So this test is implementation specific. The test above is not.")
    public void getCapabilities_browserNameCapSet_validKeyExists2() {
        concreteSauceSession.setSauceOptions();
        String expectedBrowserCapabilityKey = "browserName";
        String actualBrowser = concreteSauceSession.setSauceOptions().getCapability(expectedBrowserCapabilityKey).toString();
        assertEquals(actualBrowser, IsNot.not(""));
    }
    @Test
    public void setCapability_platformName_returnsCorrectOs() throws MalformedURLException {
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_USERNAME")).thenReturn("test-name");
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_ACCESS_KEY")).thenReturn("accessKey");

        fakeSauceSession.start();
        String correctPlatformKey = "platformName";
        String actualBrowser = fakeSauceSession.setSauceOptions().getCapability(correctPlatformKey).toString();
        assertEquals(actualBrowser, IsEqualIgnoringCase.equalToIgnoringCase("Windows 10"));
    }
    @Test
    public void startSession_default_returnsLatestBrowser() throws MalformedURLException {
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_USERNAME")).thenReturn("test-name");
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_ACCESS_KEY")).thenReturn("accessKey");

        fakeSauceSession.start();
        String correctKey = "browserVersion";
        String browserSetThroughSauceSession = fakeSauceSession.setSauceOptions().getCapability(correctKey).toString();
        assertEquals("latest", browserSetThroughSauceSession);
    }
    @Test
    public void noSauceOptionsSet_whenCreated_defaultIsChrome()
    {
        String actualBrowser = concreteSauceSession.setSauceOptions().getBrowserName();
        assertEquals(actualBrowser, IsEqualIgnoringCase.equalToIgnoringCase("Chrome"));
    }
    @Test
    public void noSauceOptionsSet_whenCreated_defaultIsWindows10() {
        String actualOs = concreteSauceSession.setSauceOptions().getPlatform().name();
        assertEquals(actualOs, IsEqualIgnoringCase.equalToIgnoringCase("win10"));
    }
    @Test
    public void sauceOptions_defaultConfiguration_setsSauceOptions()
    {
        concreteSauceSession.setSauceOptions();
        boolean hasAccessKey = concreteSauceSession.getSauceOptionsCapability().asMap().containsKey("accessKey");
        assertTrue("You need to have Sauce Credentials set (SAUCE_USERNAME, SAUCE_ACCESSKEY) before this unit test will pass", hasAccessKey);
    }

    @Test
    public void defaultSafari_notSet_returnsLatestVersion()
    {
        fakeSauceSession.withSafari();
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_USERNAME")).thenReturn("test-name");
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_ACCESS_KEY")).thenReturn("accessKey");

        String safariVersionSetThroughSauceSession = fakeSauceSession.setSauceOptions().getVersion();
        assertEquals("latest", safariVersionSetThroughSauceSession);
    }
    @Test
    public void withSafari_browserName_setToSafari()
    {
        fakeSauceSession.withSafari();
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_USERNAME")).thenReturn("test-name");
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_ACCESS_KEY")).thenReturn("accessKey");

        String actualBrowserNameSetThroughSauceSession = fakeSauceSession.setSauceOptions().getBrowserName();
        assertEquals("safari", actualBrowserNameSetThroughSauceSession);
    }
    @Test
    public void withSafari_versionChangedFromDefault_returnsCorrectVersion()
    {
        fakeSauceSession.withSafari().withBrowserVersion("11.1");
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_USERNAME")).thenReturn("test-name");
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_ACCESS_KEY")).thenReturn("accessKey");

        String actualBrowserVersionSetThroughSauceSession = fakeSauceSession.setSauceOptions().getVersion();
        assertEquals("11.1", actualBrowserVersionSetThroughSauceSession);
    }
    @Test
    //TODO How to parameterize this?
    public void withOs_changedFromDefault_returnsCorrectOs()
    {
        fakeSauceSession.withPlatform("Windows 10");
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_USERNAME")).thenReturn("test-name");
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_ACCESS_KEY")).thenReturn("accessKey");

        String actualOsSetThroughSauceSession = fakeSauceSession.setSauceOptions().getPlatform().toString();
        assertEquals("WIN10", actualOsSetThroughSauceSession);
    }
    @Test
    @Ignore("Future enhancement")
    public void withOs_linux_allowsOnlyChromeOrFirefox()
    {
        fakeSauceSession.withPlatform("Linux");
        fail();
    }
    @Test
    @Ignore("Future enhancement")
    public void withOs_windows10_doesntAllowSafari() {
        fakeSauceSession.withPlatform("Windows 10");
        fail();
    }
    @Test
    @Ignore("Future enhancement")
    public void withOs_windows8_1_allowsOnlyChromeOrFfOrIe()
    {
        fakeSauceSession.withPlatform("Windows 8.1");
        fail();
    }
    @Test
    @Ignore("Future enhancement")
    public void withOs_windows8_allowsOnlyChromeOrFfOrIe()
    {
        fakeSauceSession.withPlatform("Windows 8");
        fail();
    }
    @Test
    @Ignore("Future enhancement")
    public void withOs_mac_allowsOnlyChromeOrFfOrSafari()
    {
        fakeSauceSession.withPlatform("Windows 8");
        fail();
    }
    @Test
    @Ignore("Future enhancement")
    public void withSafari_versionChangedToInvalid_shouldNotBePossible()
    {
        //TODO it should not be possible to set an invalid version
        fakeSauceSession.withSafari().withBrowserVersion("1234");
        fail();
    }
}
