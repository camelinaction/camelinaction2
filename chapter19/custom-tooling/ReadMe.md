chapter19 - custom-tooling
--------------------------

### 19.2.1 Developing custom tooling using the camel-catalog

Custom tool that uses the Camel Catalog to find out if any running Camel applications in the JVM
uses @deprecated components.

This code contains the logic and there is modules to run this tooling in Apache Karaf and hawtio in
other directories.

#### Building

You need to build the tool from the source code by running the following Maven goal:

    mvn clean install

This tool is then used in the following projects

    custom-tooling-karaf
    hawtio-custom-plugin

