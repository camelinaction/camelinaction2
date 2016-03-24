package camelinaction;

public interface OrderService {

    Order getOrder(int orderId) throws OrderNotFoundException;

    void updateOrder(Order order) throws OrderInvalidException;

    String createOrder(Order order) throws OrderInvalidException;

    void cancelOrder(int orderId);

}
