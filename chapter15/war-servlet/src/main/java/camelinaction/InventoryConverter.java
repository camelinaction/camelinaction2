package camelinaction;

import camelinaction.inventory.UpdateInventoryInput;
import org.apache.camel.Converter;

/**
 * A Camel converter which can convert from CSV (String) to model objects.
 * <p/>
 * By annotation this class with @Converter we tell Camel this is a converter class
 * it should scan and register methods as type converters.
 */
@Converter
public final class InventoryConverter {

    private InventoryConverter() {
    }

    /**
     * This method can convert from CSV (String) to model object.
     * <p/>
     * By annotation this method with @Converter we tell Camel to include this method
     * as a type converter in its type converter registry.
     *
     * @param csv the from type
     * @return the to type
     */
    @Converter
    public static UpdateInventoryInput toInput(String csv) {
        String[] lines = csv.split(",");
        if (lines == null || lines.length != 4) {
            throw new IllegalArgumentException("CSV line is not valid: " + csv);
        }

        UpdateInventoryInput input = new UpdateInventoryInput();
        input.setSupplierId(lines[0]);
        input.setPartId(lines[1]);
        input.setName(lines[2]);
        input.setAmount(lines[3]);

        return input;
    }

}
