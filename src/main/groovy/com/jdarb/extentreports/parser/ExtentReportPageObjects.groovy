package com.jdarb.extentreports.parser

import geb.Module
import geb.Page
import groovy.transform.PackageScope

@PackageScope
class GebConstants {

  static Map notRequired = [required: false]

}

@PackageScope
class ExtentReportMainPage extends Page {
  static url = ''

  static at = { title == 'ExtentReports 2.0' }

  static content = {
    testSelectors { $('#test-collection > li > div.test-head').moduleList TestSelectorModule }
    detailsView { $('#test-details-wrapper > div > div.card-panel.details-view').module DetailsViewModule }
  }
}

@PackageScope
class TestSelectorModule extends Module {
  static content = {
    name { $('span.test-name').text() }
    status { $('div.test-head > span.test-status').text() }
  }
}

@PackageScope
class DetailsViewModule extends Module {
  static content = {
    name { children('h5').text() }
    stepDetails { children '.details-container' children '.test-body' children '.test-steps' module StepDetailsModule }
  }
}

@PackageScope
class StepDetailsModule extends Module {
  static content = {
    logs(GebConstants.notRequired) { children 'table' children 'tbody' children 'tr' moduleList DetailsLogModule }
    tests(GebConstants.notRequired) { children 'ul' children 'li' moduleList DetailsTestModule }
  }
}

@PackageScope
class DetailsLogModule extends Module {
  static content = {
    status { children '.status' getAttribute('title') }
    details { children '.step-details' text() }
  }
}

@PackageScope
class DetailsTestModule extends Module {
  static content = {
    header { children '.collapsible-header' }
    name { header.children '.test-node-name' text() }
    status { header.children '.test-info' children '.test-status' text() }
    stepDetails { children '.collapsible-body' children 'div' module StepDetailsModule }
  }
}
