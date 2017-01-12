Chapter 19 - camel-maven-validate
---------------------------------

Example that demonstrates the Apache Camel Validation using Maven tooling.

### 19.1.3 Camel Validation using Maven

You can run the validation using

    mvn camel:validate

... and there should be some errors reported.

The validate plugin has been configured to run as part of building the project, so if you run

    mvn clean install

The validation runs and in case of errors it causes the build to fail.

You can find more details about the Apache Camel Validation plugin at:

    https://github.com/apache/camel/blob/master/tooling/maven/camel-maven-plugin/src/main/docs/camel-maven-plugin.adoc

