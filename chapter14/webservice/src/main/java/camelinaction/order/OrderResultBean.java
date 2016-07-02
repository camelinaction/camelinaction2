package camelinaction.order;

public class OrderResultBean {

    public OrderResultBean() {
    }
    
    public static OrderResult orderOK() {
        return new OrderResult("OK"); 
    }
    
    public static OrderResult orderFailed() {
        return new OrderResult("Failed!"); 
    }
}
