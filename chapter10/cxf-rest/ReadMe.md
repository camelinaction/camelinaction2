chapter10 - cxf-rest
--------------------

This is an example how to develop a REST service using Apache CXF with JAX-RS (without Apache Camel)

### 10.1.2 Using JAX-RS with REST services

You can try this example by running

    mvn compile exec:java

The example comes with two dummy orders included, which makes it easier to try the example.
For example to get the first order you can from a web browser open the url:

    http://localhost:9000/orders/1

The second order is no surprisingly with the order id 2, so you can get it using:

    http://localhost:9000/orders/2
