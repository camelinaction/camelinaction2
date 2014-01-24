Camel in Action - Chapter 5 - Error Handler Use Case
====================================================

This is a simple use case example to demonstrate the error handling in Camel.

There is a server and a client part of this example.
The server is implemented in the camelinaction.HttpServer class which exposes a
http service over http://localhost:8765/rider.

The client will listen for files in the target/rider folder and try to send them
to the server. In case of failures the Camel error handler will kick in and
redeliver. If redeliver exhausted then the file will be uploaded to a remote FTP server.
However we simulate the ftp server by write the files using plain File API instead.

To start the use case you can use maven:

    mvn compile exec:java -PServer

    mvn compile exec:java -PClient

Then follow the instructions on the console.