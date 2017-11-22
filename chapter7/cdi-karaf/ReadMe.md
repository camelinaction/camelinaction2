chapter7 - cdi-karaf
--------------------

A simple hello example running Camel microservice using CDI and OSGi.

Note: Running camel-cdi on Apache Karaf is deprecated and not recommended to be used.
This example was initially created before the Camel team decided to drop support CDI on OSGi.

### 7.2.1 CDI Camel as microservice 

To try this example you need to first build it

    mvn clean install

And then start Apache Karaf (such as version 4.0.x)

    bin/karaf

And from the Karaf Shell install the example feature:

    feature:repo-add mvn:com.camelinaction/chapter7-cdi-karaf/2.0.0/xml/features

.. and install the example

    feature:install chapter7-cdi-karaf

Then wait a while, and if all is okay you should see a Camel application if you type

    camel:context-list

And you can list the Camel routes with:

    camel:route-list

From a web browser you can open the following url to call the hello service

    http://localhost:8080/hello
    
    
