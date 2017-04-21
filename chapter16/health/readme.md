Chapter 16 - health
-------------------

This example shows how you can write a Camel route that exposes a HTTP service as a simple health check service.

### 16.1.1 Checking health at the network level

You can try this example using the following command:

    mvn compile exec:java

You can then open a web browser and access the following url to send a ping to Camel and get a pong back:

    http://localhost:8080/ping

