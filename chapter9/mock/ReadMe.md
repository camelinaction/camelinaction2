chapter9 - mock
---------------

This directory includes a set of examples demonstrating using the mock component when testing Camel routes.

### 9.3.2 Unit testing with the Mock component

You can try these examples by running these goal:

    mvn test -Dtest=FirstMockTest
    mvn test -Dtest=SpringFirstMockTest
    mvn test -Dtest=SecondMockTest
    mvn test -Dtest=SpringSecondMockTest

### 9.3.5 Testing the ordering of messages

You can try this example by running this goal:

    mvn test -Dtest=GapTest
