chapter9 - advicewith
---------------------

This directory includes a set of examples demonstrating using advice-with to unit test Camel routes.

### 9.4.4 Using adviceWith to add interceptors to an existing route

You can try this example by running this goal:

    mvn test -Dtest=SimulateErrorUsingInterceptorTest

### 9.4.5 Using adviceWith to manipulate routes for testing

You can try these examples by running these goal:

    mvn test -Dtest=AdviceWithMockEndpointsTest
    mvn test -Dtest=ReplaceFromTest

### 9.4.6 Using weave with advice with to manipulate routes

You can try these examples by running these goal:

    mvn test -Dtest=WeaveByIdTest
    mvn test -Dtest=WeaveByTypeTest
    mvn test -Dtest=WeaveByTypeSelectFirstTest

