package com.saucelabs.simplesauce;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.chrome.ChromeOptions;

public class SauceOptions {
    //TODO can probably use BrowserType enum from Selenium to do BrowserType.CHROME
    @Getter
    @Setter
    public String browser = "Chrome";

    @Getter @Setter public String browserVersion = "latest";
    @Getter @Setter public String operatingSystem = "Windows 10";
    @Getter public  ChromeOptions chromeOptions;

    public SauceOptions withChrome()
    {
        chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("w3c", true);
        browser = "Chrome";
        return this;
    }
}