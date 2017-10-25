Chapter 17 - cluster-kafka
--------------------------

This directory holds an example how to cluster Camel Kafka routes.
 
The application has two parts

- A producer which sends random words to a Kafka topic
- A consumer which receives the messages from the Kafka topic

### 17.4 Clustered Kafka

This example requires to run a Kafka Broker

#### Preparing Kafka broker

From Apache Kafka (http://kafka.apache.org/quickstart) you download Kafka and follow the instructions to run Kafka.

    tar xf ~/Downloads/kafka_2.12-0.11.0.1.tgz
    cd kafka_2.12-0.11.0.1
     
Then you start Zookeeper
     
    bin/zookeeper-server-start.sh config/zookeeper.properties
    
And from another shell you start Kafka
    
    bin/kafka-server-start.sh config/server.properties

This will run a single node Kafka cluster. This is okay for this example as its Camel we cluster and not Kafka.
In a production system you would of course have to make Kafka highly available in a cluster.

### Running the producer

When Kafka is up and running you can start the producer which sends messages to Kafka

    cd chapter17/cluster-kafka
    mvn compile exec:java -P producer
    
You should then see in the console printed
    
```
[) thread #0 - ThroughputLogger] words                          INFO  Received: 76 new messages, with total 76 so far. Last group took: 773 millis which is: 98.318 messages per second. average: 98.318
[) thread #0 - ThroughputLogger] words                          INFO  Received: 94 new messages, with total 170 so far. Last group took: 1002 millis which is: 93.812 messages per second. average: 95.775```
```

The producer will send approximately 100 messages per second which is logged once pr second as shown above.

### Running the consumer

In another shell you can run the foo consumer which will receive the messages that the producer sends

    cd chapter17/cluster-kafka
    mvn compile exec:java -P foo
    
You should then see in the console the words being printed, with an increasing counter as prefix:
    
```
[read #0 - KafkaConsumer[words]] route1                         INFO  Foo got word #21-Hawt
[read #0 - KafkaConsumer[words]] route1                         INFO  Foo got word #22-Bad
[read #0 - KafkaConsumer[words]] route1                         INFO  Foo got word #23-Dude
[read #0 - KafkaConsumer[words]] route1                         INFO  Foo got word #24-Beer
[read #0 - KafkaConsumer[words]] route1                         INFO  Foo got word #25-Dude
[read #0 - KafkaConsumer[words]] route1                         INFO  Foo got word #26-Beer
[read #0 - KafkaConsumer[words]] route1                         INFO  Foo got word #27-Donkey
[read #0 - KafkaConsumer[words]] route1                         INFO  Foo got word #28-Beer
[read #0 - KafkaConsumer[words]] route1                         INFO  Foo got word #29-Bad
[read #0 - KafkaConsumer[words]] route1                         INFO  Foo got word #30-Camel
[read #0 - KafkaConsumer[words]] route1                         INFO  Foo got word #31-Rocks
[read #0 - KafkaConsumer[words]] route1                         INFO  Foo got word #32-Whiskey
```

### Failover the consumer

You can scale up the consumers by starting a new consumer from a shell

    cd chapter17/cluster-kafka
    mvn compile exec:java -P bar

When the 2nd consumer is up and running, you should be able to stop or kill the running consumer, and then see that the 2nd consumer
will failover and start processing the messages. You can then bring the first consumer back online and it should be able to failover if you kill the 2nd consumer and so on.
