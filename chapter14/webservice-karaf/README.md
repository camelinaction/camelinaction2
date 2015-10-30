Chapter 14 - webservice-karaf
--------------------------

This example shows how to hook a web service up to a JAAS backend in Apache Karaf

### 14.2.2 - Hooking web service auth into JAAS backend

First start Apache Karaf such as using the following command:

    bin/karaf

Then install Camel and CXF WS Security support in Karaf:

    feature:repo-add camel 2.16.0
    feature:install camel-cxf cxf-ws-security camel-blueprint

where `2.16.0` is the Camel version to install.

Now we need to install a Camel application in Karaf.
At first we need to build this example:

    mvn clean install

And then from the Karaf command line type:

    install -s mvn:com.camelinaction/chapter14-webservice/2.0.0
    install -s mvn:com.camelinaction/chapter14-webservice-karaf/2.0.0

which will install and start the example (the `-s` flag refers to start).

The Camel commands is now able to manage the running Camel application, for example to list the routes

    camel:route-list

Next we can place an order to the Rider Auto Parts webservice running in Karaf using a JAX-WS client. On the command line execute:

    mvn compile exec:java -Pclient

Make sure to use "karaf" as the password or authentication will fail!
