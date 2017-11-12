Chapter 15 - Running Camel in OSGi using a managed service factory
--------------------------

This example shows how to package the project as an OSGi bundle, ready to be deployed
in OSGi container such as Apache Karaf/ServiceMix.

To package this application, execute the following command:

    mvn install

To run this example in Apache Karaf 4.1.3 or better, then start Karaf with

    bin/karaf        (Unix)
    bin/karaf.bat    (Windows)

To add Apache Camel to Karaf, run the following command:

    feature:repo-add camel 2.20.1
    feature:install http camel camel-cxf

To install this application run the following command:

    bundle:install -s mvn:com.camelinaction/riderautoparts-osgi-msf/2.0.0

To spin up additional route instances, simply add configuration to Karaf's etc directory with a name like:

    camelinaction.fileinventoryroutefactory-path1.cfg 

and contents like:

    path=file://target/inventory/updates

There are example configuration files ready to go in the etc directory of this example.
