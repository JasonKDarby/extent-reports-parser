package com.jdarb.extentreports.parser

import geb.spock.GebSpec
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.StaticHandler
import spock.lang.AutoCleanup
import spock.lang.Shared

class CSSSelectorSpec extends GebSpec {

  @AutoCleanup @Shared HttpServer httpServer
  File reportFile = Utils.nestedExtentReportFile

  def setupSpec() {
    Vertx vertx = Vertx.vertx()
    Router router = Router.router(vertx)
    router.route().handler(StaticHandler.create('build'))
    httpServer = vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
  }

  def setup() {
    driver = Utils.sauceLabsDriver
    baseUrl = "http://localhost:8080/${reportFile.name}"
    to ExtentReportMainPage
  }

  def 'testSelector'() {
    expect:
    testSelectors*.name == ['top level test']

    and:
    testSelectors*.status == ['Pass']
  }

  def 'detailsView'() {
    given:
    def logs = detailsView.stepDetails.logs

    and:
    def tests = detailsView.stepDetails.tests

    expect:
    detailsView.name == 'top level test'

    and:
    logs.size() == 2

    and:
    tests.size() == 1

    and:
    logs*.status == ['info', 'pass']

    and:
    logs*.details == ['INFO', 'PASS']
  }

  def 'nestedDetailsView'() {
    given:
    def testHeaders = detailsView.stepDetails.tests
    List details = []
    List statuses = []

    when:
    testHeaders.each { it.click() }

    and:
    details.addAll testHeaders*.stepDetails.logs*.details

    and:
    statuses.addAll testHeaders*.stepDetails.logs*.status

    then:
    details == [['INFO', 'PASS']]

    and:
    statuses == [['info', 'pass']]

    and:
    testHeaders*.name == ['child level test']

    and:
    testHeaders*.status == ['Pass']
  }

}
