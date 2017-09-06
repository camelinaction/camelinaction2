package camelinaction;

import java.util.Random;

import javax.inject.Named;

import camelinaction.inventory.UpdateInventoryInput;
import camelinaction.inventory.UpdateInventoryOutput;

/**
 * Various service methods using in this example
 *
 * @version $Revision$
 */
@Named("inventoryService")
public class InventoryService {

    private Random ran = new Random();

    /**
     * To convert from model to CSV in a simply way
     */
    public String xmlToCsv(UpdateInventoryInput input) {
        return input.getSupplierId() + "," + input.getPartId() 
            + "," + input.getName() + "," + input.getAmount();
    }

    /**
     * To return an OK reply back to the webservice client
     */
    public UpdateInventoryOutput replyOk() {
        UpdateInventoryOutput ok = new UpdateInventoryOutput();
        ok.setCode("OK");
        return ok;
    }

    /**
     * To simulate updating the inventory by calling some external system which takes a bit of time
     */
    public void updateInventory(UpdateInventoryInput input) throws Exception {
        // simulate updating using some CPU processing
        int sleep = ran.nextInt(1000);
        Thread.sleep(sleep);

        System.out.println("Inventory " + input.getPartId() + " updated");
    }

}
