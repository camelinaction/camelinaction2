package camelinaction;

import java.io.InputStream;

import org.apache.camel.Exchange;
import org.w3c.dom.Document;

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

    public void toSoap(Exchange exchange) {
        String body = exchange.getIn().getBody(String.class);
        if (body.contains("ActiveMQ in Action")) {
            // load the soapFault.xml into a DOM
            InputStream is = exchange.getContext().getClassResolver().loadResourceAsStream("camelinaction/soapFault.xml");
            Document dom = exchange.getContext().getTypeConverter().convertTo(Document.class, is);

            // set a fault to indicate a failure
            exchange.getOut().setFault(true);
            exchange.getOut().setBody(dom);
        } else {
            // load the soapOK.xml into a DOM
            InputStream is = exchange.getContext().getClassResolver().loadResourceAsStream("camelinaction/soapOk.xml");
            Document dom = exchange.getContext().getTypeConverter().convertTo(Document.class, is);

            // set a xml reply
            exchange.getOut().setBody(dom);
        }
    }
}
