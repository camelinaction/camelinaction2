package camelinaction;

/**
 * A bean which computes the routing slip to use at runtime.
 * <p/>
 * This implementation is kept simple on purpose.
 */
public class ComputeSlip {

    public String compute(String body) {
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
