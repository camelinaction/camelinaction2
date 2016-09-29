Chapter 2 - Content-Based Router Example
----------------

These examples show you how to use a Content-Based Router from Camel. 

### 2.6.1 - Using a content-based router

The first example is a simple CBR and can be run using:

    mvn test -Dtest=OrderRouterTest
    mvn test -Dtest=SpringOrderRouterTest

The next example has a CBR with an otherwise clause to catch bad orders.
To run this example, execute the following command:

    mvn test -Dtest=OrderRouterOtherwiseTest
    mvn test -Dtest=SpringOrderRouterOtherwiseTest

The last example has a CBR with an otherwise clause to catch bad orders and
stop routing. To run this example, execute the following command:

    mvn test -Dtest=OrderRouterWithStopTest
    mvn test -Dtest=SpringOrderRouterWithStopTest
