chapter10 - swagger-ui
----------------------

This example shows how to embed the Swagger UI into an existing Camel web application.

### 10.3.6 Using CORS and Swagger web console

You can try this example by running

    mvn compile jetty:run-war

Then open a web browser at the following url

    http://localhost:8080/chapter10-swagger-ui/?url=rest/api-doc/camel-1/swagger.json

This opens the Swagger UI with the Rider Auto Parts Order Service selected.

In the Swagger UI you can expand the list of operations and then use the GET operation
to get the orders with id 1 or 2.

