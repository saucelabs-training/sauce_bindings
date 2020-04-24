# frozen_string_literal: true

require 'spec_helper'
require 'sauce_bindings/capybara_session'

module SauceBindings
  describe CapybaraSession do
    let(:valid_response) do
      {status: 200,
       body: {value: {sessionId: '0', capabilities: Selenium::WebDriver::Remote::Capabilities.chrome}}.to_json,
       headers: {"content_type": 'application/json'}}
    end
    let(:default_capabilities) do
      {browserName: 'chrome',
       browserVersion: 'latest',
       platformName: 'Windows 10',
       'sauce:options': {build: 'TEMP BUILD: 11'}}
    end

    def expect_request(body: nil, endpoint: nil)
      body = (body || {desiredCapabilities: default_capabilities,
                       capabilities: {firstMatch: [default_capabilities]}}).to_json
      endpoint ||= 'https://ondemand.us-west-1.saucelabs.com/wd/hub/session'
      stub_request(:post, endpoint).with(body: body).to_return(valid_response)
    end

    before do
      allow_any_instance_of(Net::HTTP).to receive(:proxy_uri)
      allow_any_instance_of(Selenium::WebDriver::Remote::Http::Default).to receive(:use_proxy?).and_return(false)
      allow(ENV).to receive(:[]).with('DISABLE_CAPYBARA_SELENIUM_OPTIMIZATIONS')
      allow(ENV).to receive(:[]).with('BUILD_TAG').and_return('')
      allow(ENV).to receive(:[]).with('BUILD_NAME').and_return('TEMP BUILD')
      allow(ENV).to receive(:[]).with('BUILD_NUMBER').and_return('11')
      allow(ENV).to receive(:[]).with('SAUCE_USERNAME').and_return('foo')
      allow(ENV).to receive(:[]).with('SAUCE_ACCESS_KEY').and_return('123')
    end

    describe '#new' do
      it 'registers a Capybara Driver' do
        CapybaraSession.new

        expect(Capybara.drivers).to include(:sauce)
        expect(Capybara.current_driver).to eq :sauce
      end
    end

    describe '#start' do
      it 'starts the session with Capybara driver' do
        expect_request

        driver = CapybaraSession.new.start
        expect(driver).to eq Capybara.current_session.driver.browser

        Capybara.current_session.driver.instance_variable_set('@browser', nil)
      end
    end

    describe '#stop' do
      it 'quits the driver' do
        cappy_driver = instance_double(Capybara::Selenium::Driver)
        driver = instance_double(Selenium::WebDriver::Remote::Driver, session_id: '1234')
        allow(cappy_driver).to receive(:browser).and_return(driver)

        allow(SauceWhisk::Jobs).to receive(:change_status).with('1234', true)

        session = CapybaraSession.new

        allow(Capybara.current_session).to receive(:driver).and_return(cappy_driver)
        allow(Capybara.current_session).to receive(:quit)

        session.start
        session.stop(true)

        expect(SauceWhisk::Jobs).to have_received(:change_status).with('1234', true)
        expect(Capybara.current_session).to have_received(:quit)

        Capybara.current_session.driver.instance_variable_set('@browser', nil)
      end
    end
  end
end
