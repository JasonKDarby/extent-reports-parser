package com.jdarb.extentreports.parser

import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.StaticHandler
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ParserSpec extends Specification {

  @AutoCleanup @Shared HttpServer httpServer

  def setupSpec() {
    Vertx vertx = Vertx.vertx()
    Router router = Router.router(vertx)
    router.route().handler(StaticHandler.create('build'))
    httpServer = vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
  }

  @Unroll
  def 'parse a simple report'() {
    given: 'a report url'
    URL reportUrl = "http://localhost:8080/${reportFile.name}".toURL()

    when: 'the report is parsed'
    List<TestNode> result = new ExtentReportParser(reportUrl).tests

    then: 'the parsed result is as expected'
    result == expected

    where:
    reportFile                    | expected
    Utils.simpleExtentReportFile  | Utils.simpleTestExpectation
    Utils.nestedExtentReportFile  | Utils.nestedTestExpectation
  }

}
