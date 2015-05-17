package camelinaction;

public class OrderService {

    public String validate(String body) throws OrderValidationException {
        // an order must contain an amount
        if (!(body.contains("amount"))) {
            throw new OrderValidationException("Invalid order");
        }

        // attach the order id 
        return body + ",id=123";
    }

    public String enrich(String body) throws OrderException {
        if (body.contains("ActiveMQ in Action")) {
            throw new OrderException("ActiveMQ in Action is out of stock");
        }

        // attach the order status
        return body + ",status=OK";
    }

    public String toCsv(String body) throws OrderException {
        if (body.contains("xml")) {
            throw new OrderException("xml files not allowed");
        }

        return body.replaceAll("#", ",");
    }

}
