Chapter 10 - jetty-rest-security-karaf
--------------------------------------

This is a Karaf based example of the `jetty-rest-security` example.

### 10.2.4 Configuring Rest DSL

To try this example you need to first build it

    mvn clean install

Then copy the file `src/main/resources/rest-users.properties` to the Apache Karaf installation
in the `etc` directory.

And then start Apache Karaf (such as version 4.1.x)

    bin/karaf

And from the Karaf Shell install the example feature:

    feature:repo-add mvn:com.camelinaction/chapter10-jetty-rest-security-karaf/2.0.0/xml/features

.. and install the example

    feature:install chapter10-jetty-rest-security-karaf

Then wait a while, and if all is okay you should see a Camel application if you type

    camel:context-list

And you can list the Camel routes with:

    camel:route-list

From a web browser you can open the following url to retrieve order 1:

    http://localhost:8080/orders/1

which should require login using HTTP Basic Authentication.
You can login using `jack` as username and `123` as password.

You can see more details of the Camel application and Karaf using the hawtio web console

    http://localhost:8181/hawtio

And use `karaf/karaf` as username and password to login.


