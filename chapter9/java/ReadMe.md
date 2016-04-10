chapter9 - java
---------------

How to get started using the `camel-test` module for testing Camel routes with the Java DSL.

### 9.1.2 Using camel-test to test Java Camel routes

You can try this example by running this goal:

    mvn test -Dtest=FirstTest

And the improved version of the test can be run with:

    mvn test -Dtest=FirstNoSleepTest


### 9.1.3 Unit testing an existing RouteBuilder class

This example can be run using:

    mvn test -Dtest=ExistingRouteBuilderTest
