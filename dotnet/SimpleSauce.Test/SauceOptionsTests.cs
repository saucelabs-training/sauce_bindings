using System;
using System.Collections.Generic;
using FluentAssertions;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Edge;
using OpenQA.Selenium.Firefox;
using OpenQA.Selenium.IE;
using OpenQA.Selenium.Safari;
using Simple.Sauce;

namespace SimpleSauce.Test
{
    [TestClass]
    public class SauceOptionsTests : BaseTest
    {
        [TestInitialize]
        public void Setup()
        {
            SauceOptions = new SauceOptions();
        }

        [TestMethod]
        public void UsesLatestChromeWindowsVersionsByDefault()
        {
            SauceOptions.BrowserName.Should().BeEquivalentTo("chrome");
            SauceOptions.BrowserVersion.Should().BeEquivalentTo("latest");
            SauceOptions.PlatformName.Should().BeEquivalentTo("Windows 10");
        }

        [TestMethod]
        public void UpdatesBrowserBrowserVersionPlatformVersion()
        {
            SauceOptions.BrowserVersion = "68";
            SauceOptions.BrowserName = Browsers.Firefox;
            SauceOptions.PlatformName = Platforms.Mac.HighSierra;

            SauceOptions.BrowserName.Should().BeEquivalentTo("firefox");
            SauceOptions.BrowserVersion.Should().BeEquivalentTo("68");
            SauceOptions.PlatformName.Should().BeEquivalentTo("macOS 10.13");
        }

        /*
         * Safari is an intersting and frustrating animal because you can only use
         * a specific safariVersion with a specific platformVersion
         * In a previous iteration, I created something that I really like.
         * A good way for the user to avoid having to know which PlatformName needs to
         * go with which BrowserVersion. I really like that protection and that API.
         * However, I'm cool with simplifying the API and allowing 
         *   SauceOptions.BrowserVersion = "68";
         *   SauceOptions.BrowserName = Browsers.Safari;
         *   SauceOptions.PlatformName = Platforms.Mac.HighSierra;
         *
         * But that does leave more possibility to mess up the BrowserVersion with PlatformName.
         * I wonder if there's a way to get both advantages and not require the setting of
         * BrowserVersion if Platforms.Mac.HighSierra is set for example?
         */

        [TestMethod]
        [DynamicData(nameof(SafariAndMacConfigurations), typeof(SafariTests))]
        public void WithSafari_SpecificVersion_SetsCorrectBrowser(string safariVersion, Platforms expectedPlatform)
        {
            SauceOptions.WithSafari(safariVersion);
            SauceOptions.ConfiguredSafariOptions.PlatformName.Should().Be(expectedPlatform.Value);
        }
        public static IEnumerable<object[]> SafariAndMacConfigurations => new[]
        {
            new object[] {"12.0", Platforms.MacOsMojave },
            new object[] {"13.0", Platforms.MacOsHighSierra },
            new object[] {"12.1", Platforms.MacOsHighSierra },
            new object[] {"11.1", Platforms.MacOsHighSierra },
            new object[] {"11.0", Platforms.MacOsSierra },
            new object[] { "10.1", Platforms.MacOsSierra },
            new object[] {"9.0", Platforms.MacOsxElCapitan },
            new object[] { "10.0", Platforms.MacOsxElCapitan },
            new object[] { "8.0", Platforms.MacOsxYosemite },
        };
        public void WithSafari(string safariVersion)
        {
            ConfiguredSafariOptions.BrowserVersion = safariVersion;
            ConfiguredSafariOptions.PlatformName = MatchCorrectPlatformToBrowserVersion(safariVersion);
        }

        public string MatchCorrectPlatformToBrowserVersion(string safariBrowserVersion)
        {
            switch (safariBrowserVersion)
            {
                case "latest":
                    return Platforms.MacOsMojave.Value;
                case "12.0":
                    return Platforms.MacOsMojave.Value;
                case "13.0":
                    return Platforms.MacOsHighSierra.Value;
                case "12.1":
                    return Platforms.MacOsHighSierra.Value;
                case "11.1":
                    return Platforms.MacOsHighSierra.Value;
                case "11.0":
                    return Platforms.MacOsSierra.Value;
                case "10.1":
                    return Platforms.MacOsSierra.Value;
                case "9.0":
                    return Platforms.MacOsxElCapitan.Value;
                case "10.0":
                    return Platforms.MacOsxElCapitan.Value;
                case "8.0":
                    return Platforms.MacOsxYosemite.Value;
                default:
                    throw new IncorrectSafariVersionException();
            }
        }

        [TestMethod]
        public void AcceptsAllW3CValues()
        {
            SauceOptions.PageLoadStrategy = PageLoadStrategy.Eager;
            SauceOptions.AcceptInsecureCerts = true;
            SauceOptions.SetWindowRect = true;
            SauceOptions.Timeouts.Implicit = 4;
            SauceOptions.Timeouts.PageLoad = 44;
            SauceOptions.Timeouts.Script = 33;

            var proxy = new Proxy();
            SauceOptions.Proxy = proxy;
            SauceOptions.StrictFileInteractability = true;
            SauceOptions.UnhandledPromptBehavior = UnhandledPromptBehavior.Dismiss;

            var expectedTimeouts = new Dictionary<string, int>();
            expectedTimeouts.Add("implicit", 4);
            expectedTimeouts.Add("pageLoad", 44);
            expectedTimeouts.Add("script", 33);

            SauceOptions.AcceptInsecureCerts.Should().BeTrue();
            SauceOptions.PageLoadStrategy.Should().BeEquivalentTo("eager");
            SauceOptions.Proxy.Should().BeEquivalentTo(proxy);
            SauceOptions.SetWindowRect.Should().BeTrue();
            SauceOptions.Timeouts.Should().BeEquivalentTo(expectedTimeouts);
            SauceOptions.StrictFileInteractability.Should().BeTrue();
            SauceOptions.UnhandledPromptBehavior.Should().BeEquivalentTo("dismiss");
        }

        [TestMethod]
        public void AcceptsAllSauceLabsValues()
        {
            var customData = new Dictionary<string, string> {{"foo", "foo"}, {"bar", "bar"}};

            var args = new List<string> {"--silent", "-a", "-q"};

            var prerun = new Dictionary<string, object>
            {
                {"executable", "http://url.to/your/executable.exe"},
                {"args", args},
                {"background", false},
                {"timeout", new TimeSpan(120)}
            };

            var tags = new List<string> {"foo", "bar"};

            SauceOptions.AvoidProxy = true;
            SauceOptions.BuildName = "Sample Build Name";
            SauceOptions.CapturePerformance = true;
            SauceOptions.ChromedriverVersion = "71";
            SauceOptions.Timeouts.CommandTimeout = 2;
            SauceOptions.CustomData = customData;
            SauceOptions.ExtendedDebugging = true;
            SauceOptions.Timeouts.IdleTimeout = 3;
            SauceOptions.IedriverVersion = "3.141.0";
            SauceOptions.Timeouts.MaxDuration = new TimeSpan(300);
            SauceOptions.TestName = "Sample Test Name";
            SauceOptions.ParentTunnel = "Mommy";
            SauceOptions.Prerun = prerun;
            SauceOptions.Priority = 0;
            SauceOptions.TestVisibility = TestVisibility.Public;
            SauceOptions.RecordLogs = false;
            SauceOptions.RecordScreenshots = false;
            SauceOptions.RecordVideo = false;
            SauceOptions.ScreenResolution = "10x10";
            SauceOptions.SeleniumVersion = "3.141.59";
            SauceOptions.Tags = tags;
            SauceOptions.TimeZone = "San Francisco";
            SauceOptions.TunnelIdentifier = tags;
            SauceOptions.VideoUploadOnPass = false;

            SauceOptions.AvoidProxy.Should().BeTrue;
            SauceOptions.Build.Should().BeEquivalentTo("Sample Build Name");
            SauceOptions.CapturePerformance.Should().BeTrue;
            SauceOptions.ChromedriverVersion.Should().BeEquivalentTo("71");
            SauceOptions.CommandTimeout.Should().BeEquivalentTo(new TimeSpan(2));
            SauceOptions.CustomData.Should().BeEquivalentTo(customData);
            SauceOptions.ExtendedDebugging.Should().BeTrue;
            SauceOptions.IdleTimeout.Should().BeEquivalentTo(new TimeSpan(3));
            SauceOptions.IedriverVersion.Should().BeEquivalentTo("3.141.0");
            SauceOptions.MaxDuration.Should().BeEquivalentTo(new TimeSpan(300));
            SauceOptions.Name.Should().BeEquivalentTo("Sample Test Name");
            SauceOptions.ParentTunnel.Should().BeEquivalentTo("Mommy");
            SauceOptions.Prerun.Should().BeEquivalentTo(prerun);
            SauceOptions.Priority.Should().BeEquivalentTo(0);
            SauceOptions.Public.Should().BeEquivalentTo("team");
            SauceOptions.RecordLogs.Should().BeFalse;
            SauceOptions.RecordScreenshots.Should().BeFalse;
            SauceOptions.RecordVideo.Should().BeFalse;
            SauceOptions.ScreenResolution.Should().BeEquivalentTo("10x10");
            SauceOptions.SeleniumVersion.Should().BeEquivalentTo("3.141.59");
            SauceOptions.Tags.Should().BeEquivalentTo(tags);
            SauceOptions.TimeZone.Should().BeEquivalentTo("San Francisco");
            SauceOptions.TunnelIdentifier.Should().BeEquivalentTo(tags);
            SauceOptions.VideoUploadOnPass.Should().BeFalse;
        }

        [TestMethod]
        public void AcceptsChromeOptionsClass()
        {
            var options = new ChromeOptions();
            SauceOptions = new SauceOptions(options);

            SauceOptions.BrowserName.Should().BeEquivalentTo("chrome");
            SauceOptions.SeleniumOptions.Should().BeEquivalentTo(options);
        }

        [TestMethod]
        public void AcceptsEdgeOptionsClass()
        {
            var options = new EdgeOptions();
            SauceOptions = new SauceOptions(options);

            SauceOptions.BrowserName.Should().BeEquivalentTo("MicrosoftEdge");
            SauceOptions.SeleniumOptions.Should().BeEquivalentTo(options);
        }

        [TestMethod]
        public void AcceptsFirefoxOptionsClass()
        {
            var options = new FirefoxOptions();
            SauceOptions = new SauceOptions(options);

            SauceOptions.BrowserName.Should().BeEquivalentTo("firefox");
            SauceOptions.SeleniumOptions.Should().BeEquivalentTo(options);
        }

        [TestMethod]
        public void AcceptsInternetExplorerOptionsClass()
        {
            var options = new InternetExplorerOptions();
            SauceOptions = new SauceOptions(options);

            SauceOptions.BrowserName.Should().BeEquivalentTo("internet explorer");
            SauceOptions.SeleniumOptions.Should().BeEquivalentTo(options);
        }

        [TestMethod]
        public void AcceptsSafariOptionsClass()
        {
            var options = new SafariOptions();
            SauceOptions = new SauceOptions(options);

            SauceOptions.BrowserName.Should().BeEquivalentTo("safari");
            SauceOptions.SeleniumOptions.Should().BeEquivalentTo(options);
        }

        [TestMethod]
        public void CreatesDefaultBuildName()
        {
            Environment.SetEnvironmentVariable("BUILD_TAG", "Not Empty");
            Environment.SetEnvironmentVariable("BUILD_NAME", "TEMP BUILD");
            Environment.SetEnvironmentVariable("BUILD_NUMBER", "11");
            
            SauceOptions.BuildName.Should().BeEquivalentTo("TEMP BUILD: 11");
        }

        [TestMethod]
        public void MergesCapabilitiesFromDictionary()
        {
            //I don't think this is a use case with .NET bindings
            //var capabilities = new Dictionary<string, object>();
            //// Add all the capabilities here
            //capabilities.Add("key", "value");
            //SauceOptions.SetCapabilities(capabilities);
            //SauceOptions.Build.Should().BeEquivalentTo(build);
        }

        [TestMethod]
        public void ParsesCapabilitiesFromW3CValues()
        {
            SauceOptions.BrowserName = "firefox";
            SauceOptions.BrowserVersion = "68";
            SauceOptions.PlatformName = "macOS 10.13";
            SauceOptions.PageLoadStrategy = "eager";
            SauceOptions.AcceptInsecureCerts = true;
            SauceOptions.SetWindowRect = true;
            SauceOptions.Timeouts.Implicit = new TimeSpan(4);
            SauceOptions.Timeouts.PageLoad = new TimeSpan(44);
            SauceOptions.Timeouts.Script = new TimeSpan(33);
            var proxy = new Proxy();
            SauceOptions.Proxy = proxy;
            SauceOptions.StrictFileInteractability = true;
            SauceOptions.UnhandledPromptBehavior = "dismiss";
            
            var timeouts = new Dictionary<string, TimeSpan>();
            timeouts.Add("implicit", new TimeSpan(4));
            timeouts.Add("pageLoad", new TimeSpan(44));
            timeouts.Add("script", new TimeSpan(33));
            
            var expectedCapabilities = new Dictionary<string, object>();
            
            SauceOptions.ToCapabilities.Should().BeEquivalentTo(expectedCapabilities);
        }

        [TestMethod]
        public void ParsesCapabilitiesFromSauceLabsValues()
        {
            var customData = new Dictionary<string, string> {{"foo", "foo"}, {"bar", "bar"}};

            var args = new List<string> {"--silent", "-a", "-q"};

            var prerun = new Dictionary<string, object>
            {
                {"executable", "http://url.to/your/executable.exe"},
                {"args", args},
                {"background", false},
                {"timeout", new TimeSpan(120)}
            };

            var tags = new List<string> {"foo", "bar"};

            SauceOptions.AvoidProxy = true;
            SauceOptions.Build = "Sample Build Name";
            SauceOptions.CapturePerformance = true;
            SauceOptions.ChromedriverVersion = "71";
            SauceOptions.CommandTimeout = new TimeSpan(2);
            SauceOptions.CustomData = customData;
            SauceOptions.ExtendedDebugging = true;
            SauceOptions.IdleTimeout = new TimeSpan(3);
            SauceOptions.IedriverVersion = "3.141.0";
            SauceOptions.MaxDuration = new TimeSpan(300);
            SauceOptions.Name = "Sample Test Name";
            SauceOptions.ParentTunnel = "Mommy";
            SauceOptions.Prerun = prerun;
            SauceOptions.Priority = 0;
            SauceOptions.Public = "team";
            SauceOptions.RecordLogs = false;
            SauceOptions.RecordScreenshots = false;
            SauceOptions.RecordVideo = false;
            SauceOptions.ScreenResolution = "10x10";
            SauceOptions.SeleniumVersion = "3.141.59";
            SauceOptions.Tags = tags;
            SauceOptions.TimeZone = "San Francisco";
            SauceOptions.TunnelIdentifier = tags;
            SauceOptions.VideoUploadOnPass = false;

            var expectedCapabilities = new Dictionary<string, object>();
            
            SauceOptions.ToCapabilities.Should().BeEquivalentTo(expectedCapabilities);
        }

        [TestMethod]
        public void ParsesCapabilitiesFromSeleniumValues()
        {
            var options = new ChromeOptions();
            SauceOptions = new SauceOptions(options);

            var expectedCapabilities = new Dictionary<string, object>();
            
            SauceOptions.ToCapabilities.Should().BeEquivalentTo(expectedCapabilities);
        }

        [TestMethod]
        public void ParsesW3CAndSauceAndSeleniumValues()
        {
            var options = new ChromeOptions();
            SauceOptions = new SauceOptions(options);

            SauceOptions.PlatformName = "macOS 10.13";
            SauceOptions.PageLoadStrategy = "eager";
            SauceOptions.AcceptInsecureCerts = true;
            SauceOptions.AvoidProxy = true;
            SauceOptions.Build = "Sample Build Name";
            SauceOptions.CapturePerformance = true;

            var expectedCapabilities = new Dictionary<string, object>();
            
            SauceOptions.ToCapabilities.Should().BeEquivalentTo(expectedCapabilities);
        }

    }
}