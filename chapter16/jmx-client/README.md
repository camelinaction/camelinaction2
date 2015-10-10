Chapter 16 - jmxclient
----------------------

This example shows how to JMX clients can manage a remote Camel application running in another JVM.

### 16.5.1 - Accessing the Camel management API using Java

To run the example you need to start the Camel application first using:

    mvn compile exec:java -Pserver

And then you can run the clients using:

    mvn compile exec:java -Pclient
    mvn compile exec:java -Pclient2

The `client` is using pure JMX api (no camel-core JAR on classpath is required).
And the second client `client2` is using JMX Proxy where camel-core JAR is required on the classpath.
