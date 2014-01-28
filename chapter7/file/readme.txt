File Component Examples
------------------------

These examples show you how to use the file component in Camel.

Reading Files
------------------------

To run the file reader example, execute the following command:

mvn compile exec:java -Dexec.mainClass=camelinaction.FilePrinter

Writing Files
------------------------

To run the file writer example, execute the following command:

mvn compile exec:java -Dexec.mainClass=camelinaction.FileSaver

For the file writer, you will need to enter some text input after 
the "Enter something:" prompt.

Writing Files with Custom Name
---------------------------

To run the file writer with fileName expression example, execute 
the following command:

mvn compile exec:java -Dexec.mainClass=camelinaction.FileSaverWithFileName

For the file writer, you will need to enter some text input after
the "Enter something:" prompt.

