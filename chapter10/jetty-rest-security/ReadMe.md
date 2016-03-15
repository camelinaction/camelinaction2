Chapter 10 - jetty-rest-security
--------------------------------

An example how to configure basic security on Jetty with a REST service

### 10.2.4 Configuring Rest DSL

To try this example you need to first build it

    mvn clean install

And then run it

    mvn compile exec:java

Then from a web browser you can access the following url: `http://localhost:8080/orders/1`
which should present with a login box. You can pass in `jack` as username and `123` as the password.

The example also provides unit tests which you can run with the following Maven goals:

    mvn test -Dtest=OrderServiceTest
    mvn test -Dtest=SpringOrderServiceTest

