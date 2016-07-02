package camelinaction;

public class UpdateInventory {

    private String supplierId;
    private String partId;
    private String name;
    private String amount;

    public UpdateInventory(String supplierId, String partId, String name, String amount) {
        this.supplierId = supplierId;
        this.partId = partId;
        this.name = name;
        this.amount = amount;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return supplierId + "," + partId + "," + name + "," + amount;
    }
}
