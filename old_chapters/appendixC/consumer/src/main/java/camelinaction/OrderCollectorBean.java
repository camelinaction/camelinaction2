package camelinaction;

import org.apache.camel.ConsumerTemplate;

public class OrderCollectorBean {
    private ConsumerTemplate consumer;

    public void setConsumer(ConsumerTemplate consumer) {
        this.consumer = consumer;
    }

    public String getOrders() {
        String order = "";
        String orders = "";

        while (order != null) {
            order = consumer.receiveBody("activemq:orders", 1000, String.class);
            if (order != null) {
                orders = orders + "," + order;
            }
        }
        return orders;
    }
}
