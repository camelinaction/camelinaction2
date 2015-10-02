package camelinaction;

import java.math.BigDecimal;

public class PurchaseOrder {

    private final String name;
    private final BigDecimal price;
    private final int amount;

    public PurchaseOrder(String name, BigDecimal price, int amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public String toString() {
        return "Ordering " + amount + " of " + name + " at total " + price;
    }
}
