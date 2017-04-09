package camelinaction;

/**
 * Rating DTO which Spring will automatic map from JSon to POJO
 */
public class RatingDto {

    private int itemNo;
    private int rating;

    public int getItemNo() {
        return itemNo;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
