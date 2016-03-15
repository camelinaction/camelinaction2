Chapter 10 - jetty-rest-security-cdi
------------------------------------

This is a CDI based example of the `jetty-rest-security` example.

### 10.2.4 Configuring Rest DSL

To try this example you need to first build it

    mvn clean install

And then start the example using

    mvn camel:run

From a web browser you can open the following url to retrieve order 1:

    http://localhost:8080/orders/1

which should require login using HTTP Basic Authentication.
You can login using `jack` as username and `123` as password.

The example also provides an unit test which you can run with:

    mvn test -Dtest=OrderServiceTest

The unit test using `camel-test-cdi` module for testing, so its worthwhile to take a look
how to do this if you are a CDI user.

