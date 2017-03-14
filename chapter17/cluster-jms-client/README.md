Chapter 17 - cluster-jms-client
-------------------------------

This directory holds an example how to cluster ActiveMQ JMS client used by the Camel JMS component. 

### 17.3.1 Client side clustering with JMS and ActiveMQ

This example requires to run two instances of Apache ActiveMQ brokers separately. To do this

#### Preparing ActiveMQ brokers

From Apache ActiveMQ you can download the broker as a .zip or .tar.gz file.

You then create a folder to hold the two message brokers and its shared data

    mkdir brokers
    cd brokers
    
And create the shared folder
    
    mkdir shared-data
    
Then un-compress the ActiveMQ download and rename the folder
    
    tar xf ~/Downloads/apache-activemq-5.14.3-bin.tar.gz
    mv apache-activemq-5.14.3 broker-1
     
And do the same for the 2nd broker
     
    tar xf ~/Downloads/apache-activemq-5.14.3-bin.tar.gz
    mv apache-activemq-5.14.3 broker-2
    
Then copy the configuration file to the brokers from this source code
    
    cd chapter17/cluster-jms-client
    cp activemq-1.xml <folder with brokers>/broker-1/conf/activemq.xml
    cp activemq-2.xml <folder with brokers>/broker-2/conf/activemq.xml
     
This will then override the default configuration with the configuration from this source code
which has been pre-configured to allow running two ActiveMQ brokers on the same computer without
network port clashing and using the same shared data directory

#### Starting ActiveMQ brokers

Then from two command shells you can run each broker

    cd broker-1
    bin/activemq console
    
    cd broker-2
    bin/activemq-console
    
Which runs each broker in the foreground and logs to the console, so you can see what happens.

### Running the client

When the brokers are running you can run the client which will send messages to the master broker

    mvn camel:run
    
You should then see the client logs
    
```
[               ActiveMQ Task-1] FailoverTransport              INFO  Successfully connected to tcp://localhost:61616
[ent) thread #0 - timer://clock] route1                         INFO  Sent message: Time is now Sun Feb 19 21:26:56 CET 2017
[ent) thread #0 - timer://clock] route1                         INFO  Sending message: Time is now Sun Feb 19 21:26:57 CET 2017
[ent) thread #0 - timer://clock] route1                         INFO  Sent message: Time is now Sun Feb 19 21:26:57 CET 2017
[ent) thread #0 - timer://clock] route1                         INFO  Sending message: Time is now Sun Feb 19 21:26:58 CET 2017
```

As you can see the client has connected to the master broker which is running on TCP port 61616.

If you then stop the master broker by `ctrl + c` in its shell, then the slave broker should become master.
And the client now logs
```
[ent) thread #0 - timer://clock] route1                         INFO  Sent message: Time is now Sun Feb 19 21:27:01 CET 2017
[ocalhost/127.0.0.1:61616@59469] FailoverTransport              WARN  Transport (tcp://localhost:61616) failed , attempting to automatically reconnect: {}
java.io.EOFException
	at java.io.DataInputStream.readInt(DataInputStream.java:392)
	at org.apache.activemq.openwire.OpenWireFormat.unmarshal(OpenWireFormat.java:268)
	at org.apache.activemq.transport.tcp.TcpTransport.readCommand(TcpTransport.java:240)
	at org.apache.activemq.transport.tcp.TcpTransport.doRun(TcpTransport.java:232)
	at org.apache.activemq.transport.tcp.TcpTransport.run(TcpTransport.java:215)
	at java.lang.Thread.run(Thread.java:745)
[ocalhost/127.0.0.1:61616@59472] FailoverTransport              WARN  Transport (tcp://localhost:61616) failed , attempting to automatically reconnect: {}
java.io.EOFException
	at java.io.DataInputStream.readInt(DataInputStream.java:392)
	at org.apache.activemq.openwire.OpenWireFormat.unmarshal(OpenWireFormat.java:268)
	at org.apache.activemq.transport.tcp.TcpTransport.readCommand(TcpTransport.java:240)
	at org.apache.activemq.transport.tcp.TcpTransport.doRun(TcpTransport.java:232)
	at org.apache.activemq.transport.tcp.TcpTransport.run(TcpTransport.java:215)
	at java.lang.Thread.run(Thread.java:745)
[ent) thread #0 - timer://clock] route1                         INFO  Sending message: Time is now Sun Feb 19 21:27:02 CET 2017
[               ActiveMQ Task-2] FailoverTransport              INFO  Successfully reconnected to tcp://localhost:51515
[               ActiveMQ Task-2] FailoverTransport              INFO  Successfully reconnected to tcp://localhost:51515
[ent) thread #0 - timer://clock] route1                         INFO  Sent message: Time is now Sun Feb 19 21:27:02 CET 2017
[ent) thread #0 - timer://clock] route1                         INFO  Sending message: Time is now Sun Feb 19 21:27:07 CET 2017
[ent) thread #0 - timer://clock] route1                         INFO  Sent message: Time is now Sun Feb 19 21:27:07 CET 2017
[ent) thread #0 - timer://clock] route1                         INFO  Sending message: Time is now Sun Feb 19 21:27:08 CET 2017
```

As you can see the client had a few WARN logs when the master broker was stopped, and then it logs that its attempting to reconnect
which was successful on the other broker that runs on TCP port 51515.

