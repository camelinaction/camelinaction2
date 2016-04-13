chapter9 - arquillian-web
-------------------------

An example how to use third party testing frameworks to test Camel applications.

This example uses Arquillian to test a Web Application using an embedded Jetty container.

### 9.6.1 Testing Camel application with Arquillian

You can run this example from the command line with:

    mvn jetty:run

And then from a web browser you can open the following url:

    http://localhost:8080/say

And a wise quote is returned.

This application can be tested using Arqullian which you can run using:

    mvn test -Dtest=QuoteArquillianTest

