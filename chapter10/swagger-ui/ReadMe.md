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


### Using Swagger UI for external REST services

You can use the embedded Swagger UI to browse REST APIs from remote servers.

In another shell start the `chapter10/spark-rest-ping` example

    $ cd chapter10/spark-rest-ping
    $ mvn compile exec:java

Which starts a new Camel application with a ping REST service running on `http://localhost:9090/ping`.

The Rest API for the ping service is available at: `http://localhost:9090/api-doc`

Now open the Swagger UI at the following url:

    http://localhost:8080/chapter10-swagger-ui

And in the address bar paste the following url:

    http://localhost:9090/api-doc

Then you should see the ping rest service, which you can try from within the Swagger UI.

This is only possible because CORS is enabled in the ping service and the API docs.

From a command shell you can use curl to see the HTTP headers where you can see the CORS headers

    curl -i http://localhost:9090/ping

    curl -i http://localhost:9090/api-docs

