package com.jdarb.extentreports.parser

import geb.Browser
import geb.driver.SauceLabsDriverFactory
import groovy.transform.Canonical
import groovy.transform.PackageScope
import org.openqa.selenium.WebDriver

@Canonical
class ExtentReportParser {

  URL extentReportUrl

  List<TestNode> getTests() {

    String sauceBrowser = System.getProperty('geb.saucelabs.browser')
    String username = System.getenv('GEB_SAUCE_LABS_USER')
    String accessKey = System.getenv('GEB_SAUCE_LABS_ACCESS_PASSWORD')

    WebDriver sauceLabsDriver = new SauceLabsDriverFactory().create(sauceBrowser, username, accessKey)

    List<TestNode> result = []

    Browser.drive(baseUrl: extentReportUrl, driver: sauceLabsDriver) {
      //Geb populates page already but without initializing a new page variable IDEA doesn't know what type page is.
      ExtentReportMainPage page = to ExtentReportMainPage

      List<TestSelectorModule> testSelectors = page.testSelectors
      testSelectors.each { TestSelectorModule testSelector ->
        testSelector.click()

        StepDetails stepDetails = parseStepDetails(page.detailsView.stepDetails)

        result << new TestNode(name: testSelector.name, status: testSelector.status, stepDetails: stepDetails)
      }
    }
    result
  }

  @PackageScope
  static StepDetails parseStepDetails(stepDetailsModule) {
    List<DetailLog> logs = []
    List<TestNode> tests = []

    logs.addAll stepDetailsModule.logs.collect { DetailsLogModule log ->
      new DetailLog(status: log.status, details: log.details)
    }

    tests.addAll stepDetailsModule.tests.collect { DetailsTestModule test ->
      test.click()
      new TestNode(name: test.name, status: test.status, stepDetails: parseStepDetails(test.stepDetails))
    }

    new StepDetails(logs, tests)
  }
}
