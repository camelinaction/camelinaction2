package camelinaction.order;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "partName",
    "amount",
    "customerName"
})
@XmlRootElement(name = "order")
public class Order {

    @XmlElement(required = true)
    private String partName;
    @XmlElement(required = true)
    private int amount;
    @XmlElement(required = true)
    private String customerName;

    public Order() {
    }
    
    public Order(String partName, int amount, String customerName) {        
        this.partName = partName;
        this.amount = amount;
        this.customerName = customerName;
    }
    
    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    @Override
    public String toString() {        
        return "Order[partName=" + partName + ", amount=" + amount + ", customerName=" + customerName + "]";
    }

}
