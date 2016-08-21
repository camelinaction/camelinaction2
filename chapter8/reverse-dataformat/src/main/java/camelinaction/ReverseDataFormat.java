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
        // marshal from the Java object (graph) to the reverse type
        byte[] bytes = exchange.getContext().getTypeConverter().mandatoryConvertTo(byte[].class, graph);
        stream.write(bytes);
    }

    public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
        // unmarshal from the input stream of reverse to Java object (graph)
        byte[] bytes = exchange.getContext().getTypeConverter().mandatoryConvertTo(byte[].class, stream);
        return bytes;
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
