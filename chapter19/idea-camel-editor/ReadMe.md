Chapter19 - fabric8 Camel editor
--------------------------------

### 19.1.2 Camel IDEA plugin

This is a Camel example used to demonstrate the Apache Camel IDEA plugin,
which you can install as a plugin to IntelliJ IDEA.

#### Installing Apache Camel IDEA

From IDEA open `Preference` -> `Plugins` -> `Browse Repositories` and then type `Camel` in the search field to
find the `Apache Camel IDEA Plugin` plugin, which you can then install.

IDEA likely require to be restarted after installing the plugin.

When IDEA is loaded again, the Camel plugin should automatic be active when you have
loaded a project that uses Camel.

If you open the Camel route source code, eg the `MyRoute` class, then you should notice that
IDEA shows a Camel icon in the gutter where the route begins.



#### Using the Camel IDEA plugin

Open the Java file `MySpringBootRouter` and position the cursor on the line with the Camel endpoint

    from("timer:trigger")

Then activate forge with (cmd + 4 / cmd + alt + 4) and in the menu select `Camel Edit Endpoint` which
should show a wizard where you can edit the timer endpoint in a type safe manner.

