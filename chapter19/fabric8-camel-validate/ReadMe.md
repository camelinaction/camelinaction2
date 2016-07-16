Chapter 19 - Validate
=====================

Example that demonstrates the fabric8 Camel Validation using Maven tooling.

19.1.3 Camel Validation using Maven

You can run the validation using

    mvn fabric8-camel:validate

... and there should be some errors reported.

The validate plugin has been configured to run as part of building the project, so if you run

    mvn clean install

The validation runs and in case of errors it causes the build to fail.

You can find more details about the fabric8 Camel Validation plugin at:

    http://fabric8.io/guide/camelMavenPlugin.html

