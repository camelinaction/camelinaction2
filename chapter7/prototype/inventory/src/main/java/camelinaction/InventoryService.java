package camelinaction;

import java.io.IOException;
import java.io.InputStream;

import org.apache.camel.converter.IOConverter;

public class InventoryService {

    public String listInventory() throws IOException {
        // just return fixed items
        InputStream is = InventoryService.class.getClassLoader().getResourceAsStream("items.xml");
        return IOConverter.toString(is, null);
    }
}
