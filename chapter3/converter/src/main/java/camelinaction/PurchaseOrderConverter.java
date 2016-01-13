package camelinaction;

import java.math.BigDecimal;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;

@Converter
public final class PurchaseOrderConverter {

    @Converter
    public static PurchaseOrder toPurchaseOrder(byte[] data, Exchange exchange) {
        TypeConverter converter = exchange.getContext().getTypeConverter();

        String s = converter.convertTo(String.class, data);
        if (s == null || s.length() < 30) {
            throw new IllegalArgumentException("data is invalid");
        }

        s = s.replaceAll("##START##", "");
        s = s.replaceAll("##END##", "");

        String name = s.substring(0, 10).trim();
        String s2 = s.substring(10, 20).trim();
        String s3 = s.substring(20).trim();

        BigDecimal price = new BigDecimal(s2);
        price.setScale(2);

        Integer amount = converter.convertTo(Integer.class, s3);

        PurchaseOrder order = new PurchaseOrder(name, price, amount);
        return order;
    }

}
