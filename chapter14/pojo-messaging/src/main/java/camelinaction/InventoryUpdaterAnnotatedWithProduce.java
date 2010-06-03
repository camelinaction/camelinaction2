package camelinaction;

import javax.sql.DataSource;

import org.apache.camel.Consume;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

public class InventoryUpdaterAnnotatedWithProduce {
    
    @Produce(uri = "jms:partnerAudit")
    ProducerTemplate partnerAudit;
    
    private JdbcTemplate jdbc;

    public InventoryUpdaterAnnotatedWithProduce(DataSource ds) {
        jdbc = new JdbcTemplate(ds);
    }
    
    @Consume(uri = "jms:partnerInventoryUpdate")
    public void updateInventory(Inventory inventory) {
        jdbc.execute(toSql(inventory));
        partnerAudit.sendBody(inventory);
    }    
    
    private String toSql(Inventory inventory) {
        Object[] args = new Object[] {inventory.getSupplierId(), inventory.getPartId(), inventory.getName(), inventory.getAmount()};
        return String.format("insert into partner_inventory (supplier_id, part_id, name, amount) values ('%s', '%s', '%s', '%s')", args);
    }
}