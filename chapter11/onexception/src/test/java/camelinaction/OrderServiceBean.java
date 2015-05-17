package camelinaction;

import java.io.FileNotFoundException;
import java.net.ConnectException;

public class OrderServiceBean {

    public String handleOrder(String body) throws OrderFailedException {
        if (body.contains("ActiveMQ")) {
            throw new OrderFailedException("Cannot order ActiveMQ");
        }

        return body + ",id=123";
    }

    public void saveToDB(String order) throws OrderFailedException {
        // simulate no connection to DB and throw it wrapped in order failed exception
        throw new OrderFailedException("Cannot store in DB", new ConnectException("Cannot connect to DB"));
    }
    
    public void enrichFromFile(String order) throws OrderFailedException {
        // simulate no file found and throw it wrapped in order failed exception
        throw new OrderFailedException("Cannot load file", new FileNotFoundException("Cannot find file"));
    }

}
