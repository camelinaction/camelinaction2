Wire Tap Example
----------------

The first example shows you how to use a Wire Tap from Camel. 
To run this example, execute the following on the command line:

mvn compile exec:java -Dexec.mainClass=camelinaction.OrderRouterWithWireTap

The second example shows you how to use a processor to intercept the message
from Camel route. To run this example, execute the following on the command line:

mvn compile exec:java -Dexec.mainClass=camelinaction.OrderRouterWithOutputProcessor