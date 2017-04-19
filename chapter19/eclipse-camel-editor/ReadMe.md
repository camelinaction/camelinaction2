Chapter19 - JBoss Camel Developer Tools
---------------------------------------

### 19.1.1 JBoss Camel Developer Tools

This is a Camel example used to demonstrate the JBoss Camel Developer Tools
which you can run from Eclipse.


#### Installing JBoss Camel Developer Tools in Eclipse

You need to use Eclipse Neon 4.6

From Eclipse you choose `Help` -> `Eclipse Marketplace...`
  and then type `Camel` in the find field and press the Go button. 

In the selection list search select `Red Hat Developer Studio Integration Stack` which is the 
name chosen for the Camel and integration tools as part of the Red Hat JBoss Fuse product.

Don't worry the tooling is open source and 100% free to use.

Then click `Install` on the selected and prepare for it to download and install a bunch of stuff which can take some time.

If the editor does not seem to be installing, then mind that its known to happen that a license agreement confirmation
box may show up below the window so you have to move the windows to find it.

After you restart Eclipse then you still need to install more. In the `Install/Update` page which is shown
on the center of the screen, select

    JBoss Fuse Development
    
And click the `Install` button. In any wizards that shows up, then click `Next` to install all the features
and to accept any license agreements.

Don't worry the tooling is open source and 100% free to use.
    
    
#### Using the editor

Open the `camel-context.xml` XML file which has the Camel route. The Camel route will be displayed graphically:

![Screenshot](img/camelRoute.png)

You can now use the graphical drag'n'drop editor to edit the EIP patterns.
The editor is 2-way editor, so you can always edit the XML source directly, and switch over
to the graphical editor, and continue editing. There is no lock-in.

For more details see: http://tools.jboss.org/features/apachecamel.html

