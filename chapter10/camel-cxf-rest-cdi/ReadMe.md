Chapter 10 - camel-cxf-rest-cdi
-------------------------------

This is a CDI based example of the `camel-cxf-rest` example.

### 10.1.5 - Using camel-cxfrs with REST services

This example can be run using:

    mvn test -Dtest=RestOrderServiceTest

And you can also run Camel CDI from the command line with

    mvn camel:run

From a command line you can use curl to retrieve order 1 in xml using:

    curl -i http://localhost:8080/orders/1

And to get the order as json format you need to specify the accept header as shown:

    curl -i --header "Accept: application/json" http://localhost:8080/orders/1


