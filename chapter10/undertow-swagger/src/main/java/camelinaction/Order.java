package camelinaction;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "order", description = "An order")
public class Order {

    @XmlAttribute
    @ApiModelProperty(name = "id", required = true, value = "The order id")
    private int id;

    @XmlAttribute
    @ApiModelProperty(name = "partName", required = true, value = "The name of the item to order")
    private String partName;

    @XmlAttribute
    @ApiModelProperty(name = "amount", required = true, value = "Number of items to order")
    private int amount;

    @XmlAttribute
    @ApiModelProperty(name = "customerName", required = true, value = "The name of the customer")
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
