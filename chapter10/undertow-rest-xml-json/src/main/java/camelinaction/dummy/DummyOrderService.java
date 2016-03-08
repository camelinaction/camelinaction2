package camelinaction.dummy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import camelinaction.Order;
import camelinaction.OrderInvalidException;
import camelinaction.OrderNotFoundException;
import camelinaction.OrderService;

public class DummyOrderService implements OrderService {

    // in memory dummy order system
    private Map<Integer, Order> orders = new HashMap<>();

    private final AtomicInteger idGen = new AtomicInteger();

    public DummyOrderService() throws Exception {
        // setup some dummy orders to start with
        setupDummyOrders();
    }

    @Override
    public Order getOrder(int orderId) throws OrderNotFoundException {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return order;
    }

    @Override
    public void updateOrder(Order order) throws OrderInvalidException {
        if (order.getAmount() == 0) {
            throw new OrderInvalidException("Use cancel instead");
        }

        int id = order.getId();
        orders.remove(id);
        orders.put(id, order);
    }

    @Override
    public String createOrder(Order order) throws OrderInvalidException {
        if (order.getAmount() <= 0) {
            throw new OrderInvalidException("Amount must be 1 or higher");
        }

        if ("kaboom".equals(order.getPartName())) {
            throw new IllegalStateException("Forced error due to kaboom");
        }

        int id = idGen.incrementAndGet();
        order.setId(id);
        orders.put(id, order);
        return "" + id;
    }

    @Override
    public void cancelOrder(int orderId) {
        orders.remove(orderId);
    }

    public void setupDummyOrders() throws Exception {
        Order order = new Order();
        order.setAmount(1);
        order.setPartName("motor");
        order.setCustomerName("honda");
        createOrder(order);

        order = new Order();
        order.setAmount(3);
        order.setPartName("brake");
        order.setCustomerName("toyota");
        createOrder(order);
    }

}
