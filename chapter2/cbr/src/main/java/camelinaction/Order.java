package camelinaction;

import java.io.Serializable;

public class Order implements Serializable {
    private static final long serialVersionUID = 3445195052621139633L;
    private String name;
    private int amount;
    
    public Order() {
    }
    
    public Order(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (this.getClass() != other.getClass()) {
            return false;
        }
        Order that = (Order) other;
        return this.name.equals(that.name) && this.amount == that.amount;
    }
    
    @Override
    public String toString() {
        return "Order[" + name + " , " + amount + "]";
    }
}
