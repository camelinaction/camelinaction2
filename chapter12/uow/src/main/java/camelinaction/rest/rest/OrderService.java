package camelinaction.rest.rest;

import org.apache.camel.Header;

public class OrderService {

    public Order getOrder(@Header("id") String id) {
        if ("123".equals(id)) {
            Order order = new Order();
            order.setId(123);
            order.setAmount(1);
            order.setMotor("Honda");
            return order;
        } else {
            return null;
        }
    }
}
