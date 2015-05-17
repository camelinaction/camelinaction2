Chapter 10 - Synchronicity and threading
========================================

This example covers the concepts of synchronicity and threading, and how this affects:

- the caller
- the service processing the call
- threads
- components
- eips
- transactions
- and much more

You can try the examples using the following commands:

    mvn test -Dtest=AsyncOneThreadTest
    mvn test -Dtest=SyncOneThreadTest
    mvn test -Dtest=AsyncMultipleThreadsTest
    mvn test -Dtest=SyncMultupleThreadsTest

The retrun early reply example is started using the following commands:

    mvn test -Dtest=EarlyReplyTest
    mvn test -Dtest=SpringEarlyReplyTest
