using FluentAssertions;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Remote;
using Simple.Sauce;

namespace SimpleSauce.Test
{
    [TestClass]
    [TestCategory("Acceptance")]
    public class SimpleSauceAcceptanceTests
    {
        private SauceOptions _sauceOptions;
        private IWebDriver _driver;
        private SauceSession _session;

        public TestContext TestContext { get; set; }
        [TestCleanup]
        public void Cleanup()
        {
            _session.Stop(true);
        }

        [TestMethod]
        public void Start_Default_IsChrome()
        {
            _session = new SauceSession();
            _driver = _session.Start();
            ((IJavaScriptExecutor)_driver).ExecuteScript("sauce:job-name=" + TestContext.TestName);

            var capabilities = ((RemoteWebDriver)_driver).Capabilities;
            capabilities.GetCapability("browserName").Should().Be("chrome");
        }
        [TestMethod]
        public void RunTestWithEdge()
        {
            _sauceOptions = new SauceOptions
            {
                BrowserName = Browser.Edge
            };

            _session = new SauceSession(_sauceOptions);
            _driver = _session.Start();
            ((IJavaScriptExecutor)_driver).ExecuteScript("sauce:job-name=" + TestContext.TestName);
            var capabilities = ((RemoteWebDriver)_driver).Capabilities;
            //Noticed that I started getting back msedge
            capabilities.GetCapability("browserName").Should().Be("msedge");
        }

        [TestMethod]
        public void RunTestWithSafariDefault()
        {
            _sauceOptions = new SauceOptions();
            _sauceOptions.WithSafari();
            _session = new SauceSession(_sauceOptions);
            _driver = _session.Start();
            ((IJavaScriptExecutor)_driver).ExecuteScript("sauce:job-name=" + TestContext.TestName);

            var capabilities = ((RemoteWebDriver)_driver).Capabilities;
            capabilities.GetCapability("browserName").Should().Be("Safari");
        }
    }
}
