package camelinaction;

import io.swagger.annotations.ApiModelProperty;

/**
 * DTO of the cart
 */
public class CartDto {

    private String itemId;
    private int quantity;

    public CartDto() {
    }

    public CartDto(String itemId) {
        this.itemId = itemId;
    }

    @ApiModelProperty(value = "Id of the item in the shopping cart")
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @ApiModelProperty(value = "How many items to purchase")
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
