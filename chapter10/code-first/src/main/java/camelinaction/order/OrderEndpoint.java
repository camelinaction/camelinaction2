package camelinaction.order;

import javax.jws.WebService;

/**
 * Code first approach for the Rider Auto Parts Order web-service
 */
@WebService
public interface OrderEndpoint {

    /**
     * Web service operation to submit an order
     */
    String order(String partName, int amount, String customerName);

}
