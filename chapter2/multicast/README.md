Chapter 2 - Multicast Example
----------------

The first example shows you how to use a Multicast from Camel. 

### 2.6.3 - Using multicasting

To run this example, execute the following on the command line:

	mvn clean test -Dtest=OrderRouterWithMulticastTest
	mvn clean test -Dtest=SpringOrderRouterWithMulticastTest

The next example shows you how to use a Multicast from Camel with parallel option. 
To run this example, execute the following on the command line:

	mvn clean test -Dtest=OrderRouterWithParallelMulticastTest
	mvn clean test -Dtest=SpringOrderRouterWithParallelMulticastTest

The last example shows you how to use a Multicast from Camel but stopping the multicast on an exception. 
To run this example, execute the following on the command line:
	
	mvn clean test -Dtest=OrderRouterWithMulticastSOETest
	mvn clean test -Dtest=SpringOrderRouterWithMulticastSOETest
