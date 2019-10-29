package com.saucelabs.simplesauce;

import com.saucelabs.simplesauce.SauceOptions;
import com.saucelabs.simplesauce.SauceSession;
import com.saucelabs.simplesauce.interfaces.EnvironmentManager;
import com.saucelabs.simplesauce.interfaces.RemoteDriverInterface;
import org.junit.Before;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaseConfigurationTest {
    public SauceSession mockSauceSession;
    protected RemoteDriverInterface fakeRemoteDriver;
    protected EnvironmentManager fakeEnvironmentManager;
    protected SauceOptions sauceOptions;

    @Before
    public void setUp()
    {
        fakeRemoteDriver = mock(RemoteDriverInterface.class);
        fakeEnvironmentManager = mock(EnvironmentManager.class);
        sauceOptions = new SauceOptions();
        mockSauceSession = new SauceSession(fakeRemoteDriver, fakeEnvironmentManager);
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_USERNAME")).thenReturn("test-name");
        when(fakeEnvironmentManager.getEnvironmentVariable("SAUCE_ACCESS_KEY")).thenReturn("accessKey");
    }

    public SauceSession instantiateSauceSession() {
        return new SauceSession(sauceOptions, fakeRemoteDriver, fakeEnvironmentManager);
    }
}
