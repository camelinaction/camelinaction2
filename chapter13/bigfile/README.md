Chapter 13 - bigfile
--------------------

This directory holds examples how to split and process a big file using concurrency

### 13.1.1 - Running the example without concurrency

First you need to create a big file which you can do using:

    mvn compile exec:java -PCreateBigFile -Dlines=1000

Then the example can be run using:

    mvn test -Dtest=BigFileTest

### 13.1.2 - Using parallel processing

The example can be run using:

    mvn test -Dtest=BigFileParallelTest
    mvn test -Dtest=SpringBigFileParallelTest

### 13.1.2 - Using a custom thread pool

The example using a cached thread pool can be run with:

    mvn test -Dtest=BigFileCachedThreadPoolTest
    mvn test -Dtest=SpringBigFileCachedThreadPoolTest

And using a fixed thread pool can be run with:

    mvn test -Dtest=BigFileFixedThreadPoolTest
    mvn test -Dtest=SpringBigFileFixedThreadPoolTest

### 13.1.2 - Using SEDA

The example can be run using:

    mvn test -Dtest=BigFileSedaTest
    mvn test -Dtest=SpringBigFileSedaTest

