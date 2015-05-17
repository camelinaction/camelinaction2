package camelinaction;

import javax.sql.DataSource;

import org.apache.camel.Consume;
import org.springframework.jdbc.core.JdbcTemplate;

public class InventoryUpdater {

    private JdbcTemplate jdbc;

    public InventoryUpdater(DataSource ds) {
        jdbc = new JdbcTemplate(ds);
    }
    
    @Consume(uri = "jms:partnerInventoryUpdate")
    public void updateInventory(Inventory inventory) {
        jdbc.execute(toSql(inventory));
    }    
    
    private String toSql(Inventory inventory) {
        Object[] args = new Object[] {
            inventory.getSupplierId(), inventory.getPartId(), 
            inventory.getName(), inventory.getAmount()};
        return String.format("insert into partner_inventory " + 
            "(supplier_id, part_id, name, amount) values " + 
            "('%s', '%s', '%s', '%s')", args);
    }
}
