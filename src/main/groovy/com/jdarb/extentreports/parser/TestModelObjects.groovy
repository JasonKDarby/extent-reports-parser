package com.jdarb.extentreports.parser

import groovy.transform.CompileStatic
import groovy.transform.Immutable

@Immutable
@CompileStatic
class TestNode {
  String name
  String status
  StepDetails stepDetails
}

@Immutable
@CompileStatic
class StepDetails {
  List<DetailLog> logs
  List<TestNode> tests
}

@Immutable
@CompileStatic
class DetailLog {
  String status
  String details
}
