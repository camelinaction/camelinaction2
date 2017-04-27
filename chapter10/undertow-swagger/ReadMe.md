chapter10 - undertow-swagger
----------------------------

This example shows how to use Undertow to define REST endpoints in Camel routes using the Rest DSL in Java,
and using camel-swagger-java to expose the REST service APIs.

### 10.3.3 Documenting Rest DSL services

You can try this example by running

    mvn compile exec:java

You can try calling this service using:
     
     http://localhost:8080/orders/1
     http://localhost:8080/orders/2

However from the command line you can try the example using `curl` where you can wee the HTTP headers as well

To get an order in json format

    curl --header "Accept: application/json" http://localhost:8080/orders/1

And to get the same order in xml format

    curl --header "Accept: application/xml" http://localhost:8080/orders/1

#### Swagger Doc

And the Swagger API documentation is accessible at:

     http://localhost:8080/api-doc/swagger.json
     http://localhost:8080/api-doc/swagger.yaml

     
