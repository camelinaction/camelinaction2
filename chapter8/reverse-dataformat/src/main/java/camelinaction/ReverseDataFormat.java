package camelinaction;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.DataFormatName;
import org.apache.camel.support.ServiceSupport;

/**
 * A <a href="http://camel.apache.org/data-format.html">data format</a> ({@link DataFormat})
 * for Reverse data.
 */
public class ReverseDataFormat extends ServiceSupport implements DataFormat, DataFormatName {

    public String getDataFormatName() {
        return "reverse";
    }

    public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
        byte[] bytes = exchange.getContext().getTypeConverter().mandatoryConvertTo(byte[].class, graph);
        String body = reverseBytes(bytes);
        stream.write(body.getBytes());
    }

    public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
        byte[] bytes = exchange.getContext().getTypeConverter().mandatoryConvertTo(byte[].class, stream);
        String body = reverseBytes(bytes);
        return body;
    }

    private String reverseBytes(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length);
        for (int i = data.length - 1; i >= 0; i--) {
            char ch = (char) data[i];
            sb.append(ch);
        }
        return sb.toString();
    }

    @Override
    protected void doStart() throws Exception {
        // init logic here
    }

    @Override
    protected void doStop() throws Exception {
        // cleanup logic here
    }

}
