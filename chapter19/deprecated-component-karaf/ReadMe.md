Chapter 19 - Deprecated Component Karaf
---------------------------------------

### 19.2.2 Camel tooling with Karaf

This is a Camel application to run in Karaf that uses a deprecated Camel component.
Which we can use custom tooling to check if any Camel application is using deprecated Camel components.

#### Installation

To build this project use

    mvn clean install

Then in a running Apache Karaf/ServiceMix container (requires Karaf 4.x) run the following

    feature:repo-add camel 2.18.2
    feature:install camel
    feature:install camel-quartz

And then install the example

    osgi:install -s mvn:com.camelinaction/deprecated-component-karaf/2.0.0

