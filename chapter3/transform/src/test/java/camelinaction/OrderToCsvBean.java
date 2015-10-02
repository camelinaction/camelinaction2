package camelinaction;

/**
 * A bean which translates an order in custom inhouse format
 * to a CSV format.
 */
public class OrderToCsvBean {

    public String map(String custom) {
        String id = custom.substring(0, 9);
        String customerId = custom.substring(10, 19);
        String date = custom.substring(20, 29);
        String items = custom.substring(30);
        String[] itemIds = items.split("@");

        StringBuilder csv = new StringBuilder();
        csv.append(id.trim());
        csv.append(",").append(date.trim());
        csv.append(",").append(customerId.trim());
        for (String item : itemIds) {
            csv.append(",").append(item.trim());
        }

        return csv.toString();
    }

}
