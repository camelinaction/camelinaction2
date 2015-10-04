Chapter 16 - jolokia-embedded-war
---------------------------------

This example shows how to embed Jolokia into a WAR application.

### 16.2.3 Embedding Jolokia WAR Agent into existing WAR application

You can also build the example and deploy the WAR file to a web container such as Apache Tomcat by

    mvn clean install

And then copy the generated WAR file `target/chapter16-jolokia-war.war` to the deploy directory of Apache Tomcat.

As an alternative to copy the WAR file to Apache Tomcat, you can run directly from Maven with the following goal;

    mvn jetty:run

