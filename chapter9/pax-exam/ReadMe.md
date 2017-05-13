chapter9 - pax-exam
-------------------

Example of testing a Camel OSGi application with Pax-Exam testing framework.

### 9.6.2 Using Pax Exam to test Camel applications

You can run the integration test that runs Pax Exam:

    mvn integration-test -P pax

#### Installing in existing Karaf/ServiceMix

You can install this example into Apache Karaf/ServiceMix using:

    feature:repo-add camel 2.19.0
    feature:install camel
    feature:repo-add mvn:com.camelinaction/chapter9-pax-exam/2.0.0/xml/features
    feature:install camel-quote

Then from a web browser you can call the example using the following url:

    http://localhost:8181/camel/say


