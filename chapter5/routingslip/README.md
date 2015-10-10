Chapter 5 - routingslip
----------------

This directory holds examples of the routing slip EIP

### 5.4 - Routing slip examples

The first example uses a "header" expression to get the routing slip and parses the destinations within using a comma delimiter:

	mvn test -Dtest=RoutingSlipSimpleTest
	mvn test -Dtest=SpringRoutingSlipSimpleTest

This example show how you can use a custom bean to calculate the routing slip based on the incoming message:

	mvn test -Dtest=RoutingSlipHeaderTest
	mvn test -Dtest=SpringRoutingSlipHeaderTest

Instead of using a "header" expression, you can use any expression. In this example we use a "method" expression:
	
	mvn test -Dtest=RoutingSlipTest
	mvn test -Dtest=SpringRoutingSlipTest

Finally, we have an example of a routing slip created by annotation a POJO with @RoutingSlip:

	mvn test -Dtest=RoutingSlipBeanTest
	mvn test -Dtest=SpringRoutingSlipBeanTest
	
