import os
from selenium import webdriver
from selenium.webdriver.remote.remote_connection import RemoteConnection
from simplesauce.options import SauceOptions


SAUCE_USERNAME = os.getenv('SAUCE_USERNAME', None)
SAUCE_ACCCESS_KEY = os.getenv('SAUCE_ACCESS_KEY', None)

us_ondemand = 'ondemand.saucelabs.com'
eu_ondemand = 'ondemand.eu-central-1.saucelabs.com'

US_SAUCE_DC_URL = 'https://{}:{}@{}/wd/hub'.format(SAUCE_USERNAME, SAUCE_ACCCESS_KEY, us_ondemand)
EU_SAUCE_DC_URL = 'https://{}:{}@o{}/wd/hub'.format(SAUCE_USERNAME, SAUCE_ACCCESS_KEY, eu_ondemand)


class SauceSession():

    def __init__(self, **kwargs):

        # TODO: flesh this out
        self.options = SauceOptions() if not kwargs else kwargs
        self.remote_url = "https://ondemand.saucelabs.com/wd/hub"
        self.driver = {}

    def start(self):
        caps = self.options

        executor = RemoteConnection(self.remote_url, resolve_ip=False)
        self.driver = webdriver.Remote(
            command_executor=executor,
            desired_capabilities=caps,
            keep_alive=True
        )
        return self.driver

    def stop(self):
        self.driver.quit()
