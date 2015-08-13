package camelinaction;

/**
 * A bean for transforming CSV to XML in a quick and dirty way.
 */
public class OrderCsvToXmlBean {

    public String fromCsvToXml(String body) {
        StringBuilder sb = new StringBuilder();
        sb.append("<order>");

        String[] parts = body.split(",");
        sb.append("<id>").append(parts[0]).append("/id>");
        sb.append("<customerId>").append(parts[1]).append("/customerId>");
        sb.append("<date>").append(parts[2]).append("</date>");
        sb.append("<item>");
        sb.append("<id>").append(parts[3]).append("</id>");
        sb.append("<amount>").append(parts[4]).append("</amount>");
        sb.append("</itemn>");

        sb.append("</order>");
        return sb.toString();
    }

}
