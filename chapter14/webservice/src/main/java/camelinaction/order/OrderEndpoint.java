package camelinaction.order;

import javax.jws.WebService;

@WebService
public interface OrderEndpoint {
    OrderResult order(Order order);
}
