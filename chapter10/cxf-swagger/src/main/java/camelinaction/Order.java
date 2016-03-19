package camelinaction;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A POJO class as model to represent the order.
 * <p/>
 * This class has been annotated with JAXB to make mapping to/from XML/JSon easier
 * using JAXB and Jackson.
 */
@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "order", description = "Details of the order")
public class Order {

    @XmlAttribute
    @ApiModelProperty(value = "The order id", required = true)
    private int id;
    @XmlAttribute
    @ApiModelProperty(value = "The name of the part", required = true)
    private String partName;
    @XmlAttribute
    @ApiModelProperty(value = "Number of items ordered", required = true)
    private int amount;
    @XmlAttribute
    @ApiModelProperty(value = "Name of the customer", required = true)
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
