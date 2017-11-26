package camelinaction.component;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a My endpoint.
 */
@UriEndpoint(firstVersion = "1.0-SNAPSHOT", scheme = "mycomponent", title = "My", syntax="mycomponent:name", 
             consumerClass = MyConsumer.class, label = "custom")
public class MyEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = "true")
    private String name;
    @UriParam(defaultValue = "10")
    private int option = 10;

    public MyEndpoint() {
    }

    public MyEndpoint(String uri, MyComponent component) {
        super(uri, component);
    }

    public MyEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        return new MyProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new MyConsumer(this, processor);
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * Some description of this option, and what it does
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Some description of this option, and what it does
     */
    public void setOption(int option) {
        this.option = option;
    }

    public int getOption() {
        return option;
    }
}
