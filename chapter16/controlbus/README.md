Chapter 16 - controlbus
-----------------------

This example shows how to use the controlbus component to manage a Camel application at runtime.

### 16.4.4 - Using controlbus to manage Camel

You can try this example using the following command:

    mvn compile exec:java

Then from a web browser or using a tool like curl you can control the route as shown below:

    $ curl http://localhost:8080/rest/ping
    PONG
    $ curl http://localhost:8080/rest/route/status
    Started
    $ curl http://localhost:8080/rest/route/stop
    $ curl http://localhost:8080/rest/route/status
    Stopped

