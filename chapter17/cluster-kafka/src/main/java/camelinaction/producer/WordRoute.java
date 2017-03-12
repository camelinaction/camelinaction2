package camelinaction.producer;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaComponent;

public class WordRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // lets shutdown quicker
        getContext().getShutdownStrategy().setTimeout(10);

        // configure the kafka component to use the broker
        KafkaComponent kafka = new KafkaComponent();

        // you can specify more brokers separated by comma
        kafka.setBrokers("localhost:9092");

        // add component to CamelContext
        getContext().addComponent("kafka", kafka);

        // use a timer to trigger every 100 milli seconds and generate a random word
        // which is sent to kafka
        from("timer:foo?period=100")
            .bean(new WordBean())
            .to("kafka:words")
            .to("log:words?groupInterval=1000");
    }
}
