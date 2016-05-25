This is intended to be a library to assist with testing [Anshoo Aurora's Extent Reports](https://github.com/anshooarora/extentreports).

It was originally created to assist with testing test framework plugins that generate reports.

To use it you'll need to have a [SauceLabs](https://saucelabs.com/) account and provide the environment variables `GEB_SAUCE_LAB_USER` and `GEB_SAUCE_LABS_ACCESS_PASSWORD` as required by the [geb-saucelabs plugin](http://www.gebish.org/manual/current/#geb-saucelabs-plugin).

Both the tests and the library itself use [Geb](http://www.gebish.org/).  I didn't have much luck trying to use headless options like [htmlunit](http://htmlunit.sourceforge.net/).

I'll add more features as necessary or as requested.

Check out the tests for more information, but as a quick sample:
```groovy
List<TestNode> result = new ExtentReportParser(reportUrl)
[
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
] == result
```
