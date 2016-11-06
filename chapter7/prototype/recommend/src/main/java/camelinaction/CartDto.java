package camelinaction;

/**
 * Shopping Cart DTO which Spring will automatic map from JSon to POJO
 */
public class CartDto {

    private String itemId;
    private int amount;

    public CartDto() {
    }

    public CartDto(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartDto cartDto = (CartDto) o;

        return itemId.equals(cartDto.itemId);
    }

    @Override
    public int hashCode() {
        return itemId.hashCode();
    }
}
