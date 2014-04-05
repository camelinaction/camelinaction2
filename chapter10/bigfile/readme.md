Chapter 10 - Introducing concurrency 
========================================

This example covers how to process a big file with and without concurrency.

You can try this example using the following commands:

At first we need to create a big file for testing:

    mvn compile exec:java -PCreateBigFile -Dlines=1000

.. and then we can start the example without concurrency:

    mvn test -Dtest=BigFileTest

.. and with concurrency:

    mvn test -Dtest=BigFileParallelTest
    mvn test -Dtest=SpringBigFileParallelTest

... and using a custom thread pool for concurrency:

    mvn test -Dtest=BigFileCachedThreadPoolTest
    mvn test -Dtest=SpringBigFileCachedThreadPoolTest

    mvn test -Dtest=BigFileFixedThreadPoolTest
    mvn test -Dtest=SpringBigFileFixedThreadPoolTest

.. and using SEDA for concurrency:

    mvn test -Dtest=BigFileSedaTest
    mvn test -Dtest=SpringBigFileSedaTest
