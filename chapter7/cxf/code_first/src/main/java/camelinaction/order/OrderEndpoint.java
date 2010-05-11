package camelinaction.order;

import javax.jws.WebService;

@WebService
public interface OrderEndpoint {
    public String order(String partName, int amount, String customerName);
}
