Chapter 15 - Running Camel in OSGi
--------------------------

This example shows how to package the project as an OSGi bundle, ready to be deployed
in OSGi container such as Apache Karaf/ServiceMix.

To package this application, execute the following command:

    mvn install

To run this example in Apache Karaf 4.0.8 or better, then start Karaf with

    bin/karaf        (Unix)
    bin/karaf.bat    (Windows)

To add Apache Camel to Karaf, run the following command:

    feature:repo-add camel 2.19.0
    feature:install http camel camel-cxf

To install this application run the following command:

    bundle:install -s mvn:com.camelinaction/riderautoparts-osgi/2.0.0

