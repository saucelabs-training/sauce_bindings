package com.saucelabs.simplesauce;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SauceSessionTest {
    private SauceSession sauceSession = spy(new SauceSession());
    private RemoteWebDriver dummyRemoteDriver = mock(RemoteWebDriver.class);
    private JavascriptExecutor dummyJSExecutor = mock(JavascriptExecutor.class);

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        doReturn(dummyRemoteDriver).when(sauceSession).createRemoteWebDriver(any(URL.class), any(MutableCapabilities.class));
    }

    @Test
    public void sauceSessionDefaultsToLatestChromeOnWindows() {
        String actualBrowser = sauceSession.getSauceOptions().getBrowserName();
        String actualBrowserVersion = sauceSession.getSauceOptions().getBrowserVersion();
        String actualPlatformName = sauceSession.getSauceOptions().getPlatformName();

        assertEquals(BrowserType.CHROME, actualBrowser);
        assertEquals("Windows 10", actualPlatformName);
        assertEquals("latest", actualBrowserVersion);
    }

    @Test
    public void sauceSessionUsesProvidedSauceOptions() {
        SauceOptions sauceOptions = spy(new SauceOptions());
        MutableCapabilities caps = sauceOptions.toCapabilities();

        sauceSession = spy(new SauceSession(sauceOptions));
        doReturn(dummyRemoteDriver).when(sauceSession).createRemoteWebDriver(anyObject(), eq(caps));

        sauceSession.start();

        verify(sauceOptions, times(2)).toCapabilities();
    }

    @Test
    public void defaultsToUSWestDataCenter() {
        String expectedDataCenterEndpoint = DataCenter.US_WEST.getEndpoint();
        assertEquals(expectedDataCenterEndpoint, sauceSession.getDataCenter().getEndpoint());
    }

    @Test
    public void setsDataCenter() {
        String expectedDataCenterEndpoint = DataCenter.US_EAST.getEndpoint();
        sauceSession.setDataCenter(DataCenter.US_EAST);
        assertEquals(expectedDataCenterEndpoint, sauceSession.getDataCenter().getEndpoint());
    }

    @Test
    public void defaultSauceURLUsesENVForUsernameAccessKey() {
        doReturn("test-name").when(sauceSession).getEnvironmentVariable("SAUCE_USERNAME");
        doReturn("accesskey").when(sauceSession).getEnvironmentVariable("SAUCE_ACCESS_KEY");

        String expetedSauceUrl = "https://test-name:accesskey@ondemand.us-west-1.saucelabs.com/wd/hub";
        assertEquals(expetedSauceUrl, sauceSession.getSauceUrl().toString());
    }

    @Test
    public void setUserNameAndAccessKey() {
        sauceSession.setUsername("test-username");
        sauceSession.setAccessKey("test-accesskey");

        String expetedSauceUrl = "https://test-username:test-accesskey@ondemand.us-west-1.saucelabs.com/wd/hub";
        assertEquals(expetedSauceUrl, sauceSession.getSauceUrl().toString());
    }

    @Test
    public void setsSauceURLDirectly() throws MalformedURLException {
        sauceSession.setSauceUrl(new URL("http://example.com"));
        String expetedSauceUrl = "http://example.com";
        assertEquals(expetedSauceUrl, sauceSession.getSauceUrl().toString());
    }

    @Test(expected = SauceEnvironmentVariablesNotSetException.class)
    public void startThrowsErrorWithoutUsername() {
        doReturn(null).when(sauceSession).getEnvironmentVariable("SAUCE_USERNAME");
        sauceSession.start();
    }

    @Test(expected = SauceEnvironmentVariablesNotSetException.class)
    public void startThrowsErrorWithoutAccessKey() {
        doReturn(null).when(sauceSession).getEnvironmentVariable("SAUCE_ACCESS_KEY");
        sauceSession.start();
    }

    @Test
    public void stopCallsDriverQuitPassing() {
        sauceSession.start();
        sauceSession.stop(true);

        verify(dummyRemoteDriver).quit();
    }

    @Test
    public void stopWithBooleanTrue() {
        doReturn(dummyJSExecutor).when(sauceSession).getJSExecutor();
        sauceSession.start();
        sauceSession.stop(true);
        verify(dummyJSExecutor).executeScript("sauce:job-result=passed");
    }

    @Test
    public void stopWithBooleanFalse() {
        doReturn(dummyJSExecutor).when(sauceSession ).getJSExecutor();
        sauceSession .start();
        sauceSession .stop(false);
        verify(dummyJSExecutor).executeScript("sauce:job-result=failed");
    }

    @Test
    public void stopWithStringPassed() {
        doReturn(dummyJSExecutor).when(sauceSession ).getJSExecutor();
        sauceSession .start();
        sauceSession .stop("passed");
        verify(dummyJSExecutor).executeScript("sauce:job-result=passed");
    }

    @Test
    public void stopWithStringFailed() {
        doReturn(dummyJSExecutor).when(sauceSession ).getJSExecutor();
        sauceSession .start();
        sauceSession .stop("failed");
        verify(dummyJSExecutor).executeScript("sauce:job-result=failed");
    }
}
