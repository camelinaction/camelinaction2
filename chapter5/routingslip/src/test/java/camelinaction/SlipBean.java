package camelinaction;

import org.apache.camel.RoutingSlip;

/**
 * A bean which turns uses the routing slip EIP
 * <p/>
 * When the slip method is invoked Camel will detect the @RoutingSlip
 * and then continue routing using the Routing Slip EIP pattern using the
 * output from the method invocation as the slip header.
 */
public class SlipBean {

    @RoutingSlip
    public String slip(String body) {
        // always include A
        String answer = "mock:a";

        // extra step if we are cool
        if (body.contains("Cool")) {
            answer += ",mock:b";
        }

        // and always include C as well
        answer += ",mock:c";
        return answer;
    }
}
