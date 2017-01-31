Chapter 19 - Custom Tooling Karaf
---------------------------------

### 19.2.2 Camel tooling with Karaf

This is a Karaf command to run the custom tooling that can check if any Camel application
is using deprecated Camel components.

#### Installing

To install this you first need to build the source code module of the following modules

 - chapter19/custom-tooling
 - chapter19/deprecated-component-karaf

and then afterwards this module using Maven:

    mvn clean install

Then in a running Apache Karaf/ServiceMix container (requires Karaf 4.x) run the following

    feature:repo-add camel 2.18.2
    feature:install camel
    feature:install camel-quartz

And then install the tooling

    install -s mvn:com.camelinaction/chapter19-custom-tooling/2.0.0
    install -s mvn:com.camelinaction/chapter19-custom-tooling-karaf/2.0.0

#### Running the custom command in Karaf

Then you can run the following command from the shell

    oldstuff:deprecated-components

However there is no Camel applications running, so lets install a Camel application that uses a deprecated component

    install -s mvn:com.camelinaction/chapter19-deprecated-component-karaf/2.0.0

And run the command again

    oldstuff:deprecated-components

And it should report that the `quartz` component is in use which has been deprecated.

