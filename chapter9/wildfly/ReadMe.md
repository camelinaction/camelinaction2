chapter9 - wildfly
------------------

How to get started using Arquillian to do integration tests of Camel deployments in an existing running WidlFly server.

### 9.2.6 Camel testing with WildFly

To try this example you need to download and install JBoss Wildfly:

    http://wildfly.org

Then you need to setup `JBOSS_HOME` environment variable such as:

    export JBOSS_HOME=/opt/wildfly-10.0.0.Final

After this is done you are ready for running the system tests, which is as simple as running Maven test.

    mvn test -Dtest=FirstWildFlyTest

