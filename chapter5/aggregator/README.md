Chapter 5 - aggregator
----------------

This directory holds examples of the aggregator EIP

### 5.2.1 - Aggregator using a header to correlate messages

This example can be run using:

    mvn test -Dtest=AggregateABCTest
    mvn test -Dtest=SpringAggregateABCTest

### 5.2.2 - Completion conditions for the Aggregator

First set of examples show how to use multiple completion conditions and can be run using:

    mvn test -Dtest=AggregateXMLTest
    mvn test -Dtest=SpringAggregateXMLTest

Next, there are examples for many of the aggregator's configuration options:

    mvn test -Dtest=AggregateABCEagerTest
    mvn test -Dtest=SpringAggregateABCEagerTest
    mvn test -Dtest=AggregateABCCloseTest
    mvn test -Dtest=SpringAggregateABCCloseTest
    mvn test -Dtest=AggregateABCInvalidTest
    mvn test -Dtest=SpringAggregateABCInvalidTest
    mvn test -Dtest=AggregateABCGroupTest
    mvn test -Dtest=SpringAggregateABCGroupTest
    mvn test -Dtest=AggregateTimeoutThreadpoolTest
    mvn test -Dtest=SpringAggregateTimeoutThreadpoolTest

There are also examples for using POJOs as the AggregationStrategy:

    mvn test -Dtest=AggregatePojoTest
    mvn test -Dtest=SpringAggregatePojoTest

### 5.2.3 - Using persistence with the Aggregator (using camel-leveldb)

This example can be run using:

    mvn test -Dtest=AggregateABCLevelDBTest
    mvn test -Dtest=SpringAggregateABCLevelDBTest

### 5.2.4 - Using recovery with the Aggregator

This example can be run using:

    mvn test -Dtest=AggregateABCRecoverTest
    mvn test -Dtest=SpringAggregateABCRecoverTest
