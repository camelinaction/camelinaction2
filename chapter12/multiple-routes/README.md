Chapter 12 - multiple-routes
----------------------------

This directory holds an example how to use transactions across multiple Camel routes

### 12.3.4 - Using transactions with a non-transacted route

The example can be run with:

    mvn test -Dtest=TXToNonTXTest
    mvn test -Dtest=SpringTXToNonTXTest

### 12.3.4 - Using transactions with another transacted route

The example can be run with:

    mvn test -Dtest=TXToTXTest
    mvn test -Dtest=SpringTXToTXTest

