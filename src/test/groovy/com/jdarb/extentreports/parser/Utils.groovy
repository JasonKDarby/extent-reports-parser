package com.jdarb.extentreports.parser

import com.relevantcodes.extentreports.ExtentReports
import com.relevantcodes.extentreports.ExtentTest
import com.relevantcodes.extentreports.LogStatus
import geb.driver.SauceLabsDriverFactory
import org.openqa.selenium.WebDriver

class Utils {

  static WebDriver newSauceLabsDriver() {
    def sauceBrowser = System.getProperty('geb.saucelabs.browser')
    String username = System.getenv('GEB_SAUCE_LABS_USER')
    String accessKey = System.getenv('GEB_SAUCE_LABS_ACCESS_PASSWORD')
    new SauceLabsDriverFactory().create(sauceBrowser, username, accessKey)
  }

  static WebDriver sauceLabsDriver = newSauceLabsDriver()

  static File withTemporaryReportFile(Closure c) {
    String reportName = "${UUID.randomUUID().toString()}.html"
    File reportFile = new File("build/$reportName")
    reportFile.deleteOnExit()
    ExtentReports extentReport = new ExtentReports(reportFile.absolutePath)
    c(extentReport)
    extentReport.close()
    reportFile
  }

  static File simpleExtentReportFile = withTemporaryReportFile { ExtentReports extentReport ->
    ExtentTest basicTest = extentReport.startTest 'basic test'
    basicTest.log(LogStatus.PASS, 'PASS')
    extentReport.endTest basicTest
  }

  static List<TestNode> simpleTestExpectation = [
      [
          name: 'basic test',
          status: 'Pass',
          stepDetails: [
              logs: [
                  [
                      status: 'pass',
                      details: 'PASS'
                  ] as DetailLog
              ],
              tests: []
          ] as StepDetails
      ] as TestNode
  ]

  static File nestedExtentReportFile = withTemporaryReportFile { ExtentReports extentReport ->
    ExtentTest topLevelTest = extentReport.startTest 'top level test'
    ExtentTest childLevelTest = extentReport.startTest 'child level test'
    childLevelTest.log(LogStatus.INFO, 'INFO')
    childLevelTest.log(LogStatus.PASS, 'PASS')
    extentReport.endTest childLevelTest
    topLevelTest.appendChild childLevelTest
    topLevelTest.log(LogStatus.INFO, 'INFO')
    topLevelTest.log(LogStatus.PASS, 'PASS')
    extentReport.endTest topLevelTest
  }

  static List<TestNode> nestedTestExpectation = [
      [
          name: 'top level test',
          status: 'Pass',
          stepDetails: [
              logs: [
                  [
                      status: 'info',
                      details: 'INFO'
                  ] as DetailLog,
                  [
                      status: 'pass',
                      details: 'PASS'
                  ]  as DetailLog
              ],
              tests: [
                  [
                      name: 'child level test',
                      status: 'Pass',
                      stepDetails: [
                          logs: [
                              [
                                  status: 'info',
                                  details: 'INFO'
                              ] as DetailLog,
                              [
                                  status: 'pass',
                                  details: 'PASS'
                              ] as DetailLog
                          ],
                          tests: []
                      ] as StepDetails
                  ] as TestNode
              ]
          ] as StepDetails
      ] as TestNode
  ]

}
