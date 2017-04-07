Chapter 16 - jolokia-karaf
--------------------------

This example shows how to use Camel commands in Apache Karaf/ServiceMix (Jolokia is not required)

### 16.4.3 - Using Camel commands to manage Camel

First start Apache Karaf such as using the following command:

    bin/karaf

Then install Camel in Karaf:

    feature:repo-add camel 2.19.0
    feature:install camel

where `2.19.0` is the Camel version to install.

Now we need to install a Camel application in Karaf.
At first we need to build this example:

    mvn clean install

And then from the Karaf command line type:

    install -s mvn:camelinaction/chapter16-jolokia-karaf/2.0.0

which will install and start the example (the `-s` flag refers to start).

The Camel commands is now able to manage the running Camel application, for example to list the routes

    camel:route-list

