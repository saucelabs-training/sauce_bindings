package com.saucelabs.saucebindings;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SauceSessionTest {
    private SauceOptions sauceOptions = spy(new SauceOptions());
    private SauceSession sauceSession = spy(new SauceSession());
    private SauceSession sauceOptsSession = spy(new SauceSession(sauceOptions));
    private RemoteWebDriver dummyRemoteDriver = mock(RemoteWebDriver.class);
    private MutableCapabilities dummyMutableCapabilities = mock(MutableCapabilities.class);

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        doReturn(dummyRemoteDriver).when(sauceSession).createDriver(any(URL.class), any(MutableCapabilities.class));
    }

    @Test
    public void sauceSessionDefaultsToChromeOnWindows() {
        Browser actualBrowser = sauceSession.getSauceOptions().getBrowserName();
        String actualBrowserVersion = sauceSession.getSauceOptions().getBrowserVersion();
        SaucePlatform actualPlatformName = sauceSession.getSauceOptions().getPlatformName();

        assertEquals(Browser.CHROME, actualBrowser);
        assertEquals(SaucePlatform.WINDOWS_10, actualPlatformName);
    }

    @Test
    public void sauceSessionUsesProvidedSauceOptions() {
        doReturn(dummyMutableCapabilities).when(sauceOptions).toCapabilities();
        doReturn(dummyRemoteDriver).when(sauceOptsSession).createDriver(any(URL.class), eq(dummyMutableCapabilities));

        sauceOptsSession.start();

        verify(sauceOptions).toCapabilities();
    }

    @Test
    public void defaultsToUSWestDataCenter() {
        String expectedDataCenterEndpoint = DataCenter.US_WEST.getValue();
        assertEquals(expectedDataCenterEndpoint, sauceSession.getDataCenter().getValue());
    }

    @Test
    public void setsDataCenter() {
        String expectedDataCenterEndpoint = DataCenter.US_EAST.getValue();
        sauceSession.setDataCenter(DataCenter.US_EAST);
        assertEquals(expectedDataCenterEndpoint, sauceSession.getDataCenter().getValue());
    }

    @Test
    public void setsSauceURLDirectly() throws MalformedURLException {
        sauceSession.setSauceUrl(new URL("http://example.com"));
        String expetedSauceUrl = "http://example.com";
        assertEquals(expetedSauceUrl, sauceSession.getSauceUrl().toString());
    }

    @Test(expected = SauceEnvironmentVariablesNotSetException.class)
    public void startThrowsErrorWithoutUsername() {
        doReturn(null).when(sauceOptions).getEnvironmentVariable("SAUCE_USERNAME");
        sauceOptsSession.start();
    }

    @Test(expected = SauceEnvironmentVariablesNotSetException.class)
    public void startThrowsErrorWithoutAccessKey() {
        doReturn(null).when(sauceOptions).getEnvironmentVariable("SAUCE_ACCESS_KEY");
        sauceOptsSession.start();
    }

    @Test
    public void stopCallsDriverQuitPassing() {
        sauceSession.start();
        sauceSession.stop(true);

        verify(dummyRemoteDriver).quit();
    }

    @Test
    public void stopWithBooleanTrue() {
        sauceSession.start();
        sauceSession.stop(true);
        verify(dummyRemoteDriver).executeScript("sauce:job-result=passed");
    }

    @Test
    public void stopWithBooleanFalse() {
        sauceSession.start();
        sauceSession.stop(false);
        verify(dummyRemoteDriver).executeScript("sauce:job-result=failed");
    }

    @Test
    public void stopWithStringPassed() {
        sauceSession.start();
        sauceSession.stop("passed");
        verify(dummyRemoteDriver).executeScript("sauce:job-result=passed");
    }

    @Test
    public void stopWithStringFailed() {
        sauceSession.start();
        sauceSession.stop("failed");
        verify(dummyRemoteDriver).executeScript("sauce:job-result=failed");
    }
}
