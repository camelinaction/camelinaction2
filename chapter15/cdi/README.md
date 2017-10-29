Chapter 15 - Running Camel with CDI
--------------------------

This example shows how to deploy Camel to WildFly using CDI.

To try this example you need to download and install JBoss WildFly:

    http://wildfly.org

We used 10.1.0.Final at the time of writing. You will also need to install WildFly Camel 
as it's a separate project from WildFly. You can find instructions on how to install 
Wildfly Camel in the project's documentation at http://wildfly-extras.github.io/wildfly-camel/. 
At the time of writing, the latest version was 4.9.0, so we used that.

After installing WildFly Camel you can boot WildFly up with Camel support by running:

    ./bin/standalone.sh -c standalone-full-camel.xml

You can package this example using:

    mvn install -Pdeploy

This will also deploy the WAR to a WildFly instance running locally.


