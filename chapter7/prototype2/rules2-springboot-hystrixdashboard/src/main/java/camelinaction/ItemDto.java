package camelinaction;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemDto {

    @XmlElement
    private int itemNo;
    @XmlElement
    private String name;
    @XmlElement
    private String description;
    @XmlElement
    private int number;
    @XmlElement
    private int rating;

    public ItemDto() {
    }

    public ItemDto(int itemNo, String name, String description, int number) {
        this.itemNo = itemNo;
        this.name = name;
        this.description = description;
        this.number = number;
    }

    public int getItemNo() {
        return itemNo;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
