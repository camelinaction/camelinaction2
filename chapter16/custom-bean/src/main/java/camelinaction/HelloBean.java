package camelinaction;

import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.api.management.ManagedOperation;
import org.apache.camel.api.management.ManagedResource;

@ManagedResource
public class HelloBean {

    private String greeting = "Hello";

    @ManagedAttribute(description = "The greeting to use")
    public String getGreeting() {
        return greeting;
    }

    @ManagedAttribute(description = "The greeting to use")
    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    @ManagedOperation(description = "Say the greeting")
    public String say() {
        return greeting;
    }
}
