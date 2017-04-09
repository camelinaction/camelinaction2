Chapter 15 - Startup ordering
--------------------------

There are 2 examples you can try.

The inventory example demonstrates how to specify the route startup ordering in the routes
in both Java and Spring DSL:

    mvn test -Dtest=InventoryJavaDSLTest
    mvn test -Dtest=InventorySpringXMLTest

The manual route with stop demonstrates how to stop a route from a route when the route
no longer should be running. You can try this example, running the following command:

    mvn test -Dtest=ManualRouteWithStopTest

And the variation of the example using an onCompletion to stop the route:

    mvn test -Dtest=ManualRouteWithOnCompletionTest
    mvn test -Dtest=SpringManualRouteWithOnCompletionTest

