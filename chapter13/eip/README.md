Chapter 13 - eip
----------------

This directory holds examples how to use concurrency with EIP patterns

### 13.3.2 - Using concurrency with the Multicast EIP

The example can be run with and without concurrency:

    mvn test -Dtest=MulticastTest
    mvn test -Dtest=MulticastParallelTest


### 13.3.3 Using concurrency with the WireTap EIP

The example can be run with:

    mvn test -Dtest=WireTapTest
    mvn test -Dtest=SpringWireTapTest

### 13.3.3 - Returning an early reply to the caller with the WireTap EIP

The example can be run with:

    mvn test -Dtest=EarlyReplyTest
    mvn test -Dtest=SpringEarlyReplyTest

