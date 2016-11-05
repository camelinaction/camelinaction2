package camelinaction;

/**
 * DTO of shopping item
 */
public class ItemDto {

    private int itemNo;
    private String name;
    private String description;
    private int rating;

    public ItemDto() {
    }

    public ItemDto(int itemNo, String name, String description, int rating) {
        this.itemNo = itemNo;
        this.name = name;
        this.description = description;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
