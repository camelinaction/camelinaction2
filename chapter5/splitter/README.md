Chapter 5 - splitter
----------------

This directory holds examples of the splitter EIP

### 5.3.1 - Split an ArrayList message body into multiple messages

This example can be run using:

    mvn test -Dtest=SplitterABCTest
    mvn test -Dtest=SpringSplitterABCTest

### 5.3.2 - Using beans for splitting

This example can be run using:

    mvn test -Dtest=SplitterBeanTest
    mvn test -Dtest=SpringSplitterBeanTest

### 5.3.4 - Aggregating split messages

This example can be run using:

    mvn test -Dtest=SplitterAggregateABCTest
    mvn test -Dtest=SpringSplitterAggregateABCTest

### 5.3.5 - When errors happen during a split

You have two choices for handling errors with the Splitter: using stopOnException:

    mvn test -Dtest=SplitterStopOnExceptionABCTest
    mvn test -Dtest=SpringSplitterStopOnExceptionABCTest

or by using a AggregationStrategy:

    mvn test -Dtest=SplitterAggregateExceptionABCTest
    mvn test -Dtest=SpringSplitterAggregateExceptionABCTest    
