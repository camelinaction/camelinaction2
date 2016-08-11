Chapter 19 - hawtio custom plugin
---------------------------------

### 19.3.3 Building hawtio plugins

This is a hawtio plugin to run the custom tooling that can check if any Camel application
is using deprecated Camel components.

#### Installing

To install this you first need to build the source code module of the following modules

    chapter19/custom-tooling
    chapter19/deprecated-component-karaf

and then afterwards this module using Maven:

    mvn clean install

#### Installing in Karaf/ServiceMix

Then in a running Apache Karaf/ServiceMix container (requires Karaf 4.x) run the following

    feature:repo-add camel 2.17.3
    feature:install camel
    feature:install camel-quartz

And then install hawtio

    feature:repo-add hawtio 1.4.63
    feature:install hawtio

Then install this custom hawtio plugin

    install -s mvn:com.camelinaction/chapter19-hawtio-custom-plugin/2.0.0/war

Then open a web browser at

    http://localhost:8181/hawtio

And login with karaf/karaf (default username/password) and you should see the custom plugin
named `Camel Deprecated` in the top bar menu.

Because there is no Camel applications running in the JVM, there is no output from the plugin.
So you can install a Camel application that uses a deprecated component from the Karaf shell:

    install -s mvn:com.camelinaction/chapter19-deprecated-component-karaf/2.0.0

And when you run the check again then it should repor that `quart` component is deprecated.


#### Installing in Apache Tomcat

You can install this plugin in Apache Tomcat or similar web container.
We will use Tomcat as the example here.

Start Apache Tomcat in the foreground such as running

    bin/catalina run

Then install hawtio by downloading the hawtio-default-offline WAR from:

    http://hawt.io/getstarted/index.html

.. and then rename the downloaded .war to `hawtio.war` and copy the file to Apache Tomcat
in the webapps directory. This will install hawtio in Tomcat using the context-path `hawtio`.

Then install this plugin by copying the `target/oldstuff-plugin.war` to Apache Tomcat
in the webapps directory.

Then open a web browser at:

    http://localhost:8080/hawtio

... and you should see the `Oldstuff` plugin being active in the top menu bar.

After this you would need to install some Camel applications that uses deprecated components.

