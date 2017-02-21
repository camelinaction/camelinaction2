package camelinaction;

/**
 * Shopping Cart DTO which Spring will automatic map from JSon to POJO
 */
public class CartDto {

    private String itemId;
    private int quantity;

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
