Chapter 2 - Content-Based Router Example
----------------

These examples show you how to use a Content-Based Router from Camel. 
The first example is a simple CBR and can be run using:

    mvn test -Dtest=OrderRouterTest

The next example has a CBR with an otherwise clause to catch bad orders.
To run this example, execute the following command:

    mvn test -Dtest=OrderRouterOtherwiseTest

The last example has a CBR with an otherwise clause to catch bad orders and
stop routing. To run this example, execute the following command:

    mvn test -Dtest=OrderRouterWithStopTest

