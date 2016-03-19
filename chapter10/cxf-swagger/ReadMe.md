chapter10 - cxf-swagger
-----------------------

This is an example how to use Swagger with an existing JAX-RS application.
The JAX-RS application is using CXF as the REST implementation and Jetty as the HTTP server.

### 10.3.1 Using Swagger with JAX-RS Rest services

You can try this example by running

    mvn compile exec:java

The example comes with two dummy orders included, which makes it easier to try the example.
For example to get the first order you can from a web browser open the url:

    http://localhost:9000/orders/1

The second order is no surprisingly with the order id 2, so you can get it using:

    http://localhost:9000/orders/2

The Swagger documentation of the Rest service can be accessed using:

    http://localhost:9000/swagger.json
