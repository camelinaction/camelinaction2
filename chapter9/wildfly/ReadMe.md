chapter9 - wildfly
------------------

How to get started using Arquillian to do integration tests of Camel deployments in an existing running WildlFly server.

### 9.2.7 Camel testing with WildFly

To try this example you need to download and install JBoss WildFly:

    http://wildfly.org

Then you need to setup `JBOSS_HOME` environment variable such as:

    export JBOSS_HOME=/opt/wildfly-11.0.0.Final

After this is done you are ready for running the system tests, which is as simple as running Maven test.

    mvn integration-test -P wildfly

