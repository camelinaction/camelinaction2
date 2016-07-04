Chapter 19 - Custom Tooling Karaf
---------------------------------

This is a Karaf command to run the custom tooling that can check if any Camel application
is using deprecated Camel components.

### Installing

To install this you first need to build the source code module of the following modules

 - chapter19/custom-tooling
 - chapter19/deprecated-component-karaf

and then afterwards this module using Maven:

    mvn clean install

Then in a running Apache Karaf/ServiceMix container (requires Karaf 4.x) run the following

    feature:repo-add camel 2.17.2
    feature:install camel
    feature:install camel-quartz

And then install the tooling

    install -s mvn:com.camelinaction/custom-tooling/2.0.0
    install -s mvn:com.camelinaction/custom-tooling-karaf/2.0.0

Then you can run the following command from the shell

    custom:deprecated-components

However there is no Camel applications running, so lets install an application that uses a deprecated component

    install -s mvn:com.camelinaction/deprecated-component-karaf/2.0.0

And run the command again

    custom:deprecated-components

And it should report that the `quartz` component is in use which has been deprecated.

