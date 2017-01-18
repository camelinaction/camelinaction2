Chapter 15 - Running Camel with CDI
============================================

This example shows how to deploy Camel to Karaf using CDI.

### 15.6.3 Karaf deployment

To try this example you need to first build it

    mvn clean install

And then start Apache Karaf (such as version 4.0.x)

    bin/karaf

And from the Karaf Shell install the example feature:

    feature:repo-add mvn:com.camelinaction/riderautoparts-cdi-karaf/2.0.0/xml/features

.. and install the example

    feature:install riderautoparts-cdi-karaf

Then wait a while, and if all is okay you should see a Camel application if you type

    camel:context-list

And you can list the Camel routes with:

    camel:route-list

From a web browser you can open the following url to call the hello service

    http://localhost:8080/hello



