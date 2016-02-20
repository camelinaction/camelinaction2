Chapter 10 - camel-cxf-rest-karaf
---------------------------------

This is a Karaf based example of the `camel-cxf-rest` example, which is covered in section 10.1.5.


To try this example you need to first build it

    mvn clean install

And then start Apache Karaf (such as version 4.0.4)

    bin/karaf

And from the Karaf Shell install the example feature:

    feature:repo-add mvn:com.camelinaction/chapter10-camel-cxf-rest-karaf/2.0.0/xml/features

.. and install the example

    feature:install chapter10-camel-cxf-rest-karaf

Then wait a while, and if all is okay you should see a Camel application if you type

    camel:context-list



