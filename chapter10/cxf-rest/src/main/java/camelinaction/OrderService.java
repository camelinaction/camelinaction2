package camelinaction;

/**
 * An pure Java interface that defines the services for
 * the Rider Auto Parts order status applicatopn.
 */
public interface OrderService {

    Order getOrder(int orderId);

    void updateOrder(Order order);

    String createOrder(Order order);

    void cancelOrder(int orderId);

}
