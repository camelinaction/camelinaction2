Chapter19 - fabric8 Camel editor
--------------------------------

19.1.2 fabric8 Camel editor

This is a Camel example used to demonstrate the fabric8 Camel editor, which you can run from IDEA or Eclipse.


### Installing fabric8 in IDEA

From IDEA open `Preference` -> `Plugins` -> `Browse Repositories` and then type `Forge` in the search field to
find the `JBoss Forge` plugin, which you can then install.

IDEA may require to be restarted when installing the JBoss Forge plugin.

When IDEA is loaded again, you can activate the JBoss Forge plugin by pressing

    cmd + 4         (windows/linux)
    cmd + alt + 4   (mac)

Then JBoss Forge should be starting which takes a while.

After a while the JBoss Forge menu appears, and you select the `Install addon` command from the list.

In the coordinate field you type

    io.fabric8.forge:camel,2.2.227

To install the fabric camel addon for JBoss Forge. The version number can be replaced with the latest release
which you can find from Maven Central using the following url:

   http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22io.fabric8.forge%22%20AND%20a%3A%22camel%22



### Installing fabric8 in Eclipse

TODO: This is harder

When IDEA is loaded again, you can activate the JBoss Forge plugin by pressing

    cmd + 4         (windows/linux)
    cmd + alt + 4   (mac)

Then JBoss Forge should be starting which takes a while.

After a while the JBoss Forge menu appears, and you select the `Install addon` command from the list.

In the coordinate field you type

    io.fabric8.forge:camel,2.2.227

To install the fabric camel addon for JBoss Forge. The version number can be replaced with the latest release
which you can find from Maven Central using the following url:

   http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22io.fabric8.forge%22%20AND%20a%3A%22camel%22


### Using the fabric8 Camel editor

Open the Java file `MySpringBootRouter` and position the cursor on the line with the Camel endpoint

    from("timer:trigger")

Then activate forge with (cmd + 4 / cmd + alt + 4) and in the menu select `Camel Edit Endpoint` which
should show a wizard where you can edit the timer endpoint in a type safe manner.

