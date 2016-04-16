chapter9 - pax-exam
-------------------

Example of testing a Camel OSGi application with Pax-Exam testing framework.

### 9.6.2 Using Pax Exam to test Camel applications

You need to build the example first with

    mvn install

Then you can install this example into Apache Karaf/ServiceMix using

    feature:repo-add camel 2.17.0
    feature:install camel
    feature:repo-add mvn:com.camelinaction/chapter9-pax-exam/2.0.0/xml/features
    feature:install chapter9-pax-exam

Then from a web browser you can call the example using the following url:

    http://localhost:8181/camel/say

You can run the unit test tht runs Pax Exam using

    mvn test

