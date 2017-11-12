Chapter 12 - riderautoparts-partner-karaf
-----------------------------------------

This is a Karaf based example of the `riderautoparts-partner` example, which is covered in section 12.1.1 to 12.1.3.

IMPORTANT: Embedded ActiveMQ in Apache Karaf is not a recommended approach.
Its better to run AcitveMQ as a separate instance with OSGi.
 Apache ActiveMQ is not so OSGi friendly and has some problems of the latest 5.14.x releases.
 
This example currently does not work! 


To try this example you need to first build it

    mvn clean install

And then start Apache Karaf (such as version 4.1.3)

    bin/karaf

And from the Karaf Shell install the example feature:

    feature:repo-add mvn:com.camelinaction/chapter12-riderautoparts-partner-karaf/2.0.0/xml/features

.. and install the example

    feature:install chapter12-riderautoparts-partner-karaf

Then wait a while. Despite Karaf being a dynamic container its a good idea to restart Karaf
because we install a whole lot of stuff in one goal which can cause it to not work.

    camel:context-list

Then from the hawtio web console you can browse the application, and send a XML message to the ActiveMQ queue

    http://localhost:8181/hawtio/

Login using `karaf/karaf` as username and password.


In the `partners` queue in the ActiveMQ plugin, you can click the `Send` button and send the following XML

    <?xml version="1.0"?><partner id="123"><date>201702250815</date><code>200</code><time>4387</time></partner>

Notice the first time you need to configured login credentials, which hawtio shows on the screen in the `Send` dialog.
Use `karaf/karaf` as user name and password, and close the prefernce by clicking the X button in the top righter corner.


If all goes well, the message gets de-queued from the ActiveMQ queue and processed by Camel and inserted into the database.

In the Karaf log you will see something like:

```
2017-02-26 16:09:02,402 | INFO  | nsumer[partners] | partnerToDB                      | 74 - org.apache.camel.camel-core - 2.20.1 | incoming message <?xml version="1.0"?><partner id="123"><date>201702250815</date><code>200</code><time>4387</time></partner>
2017-02-26 16:09:02,419 | INFO  | nsumer[partners] | XPathBuilder                     | 74 - org.apache.camel.camel-core - 2.20.1 | Created default XPathFactory org.apache.xpath.jaxp.XPathFactoryImpl@4ada27a1
2017-02-26 16:09:02,657 | INFO  | nsumer[partners] | partnerToDB                      | 74 - org.apache.camel.camel-core - 2.20.1 | inserted into database
```

If you see `inserted into database` then it worked.

