Chapter 10 - camel-cxf-rest-karaf
---------------------------------

This is a Karaf based example of the `camel-cxf-rest` example.

### 10.1.5 - Using camel-cxfrs with REST services

To try this example you need to first build it

    mvn clean install

And then start Apache Karaf (such as version 4.1.x)

    bin/karaf

And from the Karaf Shell install the example feature:

    feature:repo-add mvn:com.camelinaction/chapter10-camel-cxf-rest-karaf/2.0.0/xml/features

.. and install the example

    feature:install chapter10-camel-cxf-rest-karaf

Then wait a while, and if all is okay you should see a Camel application if you type

    camel:context-list

And you can list the Camel routes with:

    camel:route-list

From a command line you can use curl to retrieve order 1 in xml using:

    curl -i http://localhost:8080/orders/1

And to get the order as json format you need to specify the accept header as shown:

    curl -i --header "Accept: application/json" http://localhost:8080/orders/1

You can see more details of the Camel application and Karaf using the hawtio web console

    http://localhost:8181/hawtio

And use `karaf/karaf` as username and password to login.


