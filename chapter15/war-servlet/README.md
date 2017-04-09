Chapter 15 - Running Camel as Web Application
--------------------------

This example shows how to run Camel as a Web Application (war)

You can package this example as a war file using

    mvn install

And the .war file is generated in the target directory. You can then deploy this WAR file in
Apache Tomcat/Jetty or other web containers.

You can also try this example using the Jetty plugin, running the following command:

    mvn jetty:run

