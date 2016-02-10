package camelinaction;

/**
 * Jackson is able to map to this POJO without any annotations. But you can customize anyhow you want (also use JAXB).
 * See more details at: https://github.com/FasterXML/jackson-annotations/wiki/Jackson-Annotations
 */
public class Order {

    private int id;
    private String partName;
    private int amount;
    private String customerName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
