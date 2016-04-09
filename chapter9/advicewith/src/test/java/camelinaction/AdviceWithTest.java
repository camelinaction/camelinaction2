package camelinaction;

import org.apache.camel.test.junit4.CamelTestSupport;

/**
 * Unit test demonstrating various functionality of using advice-with
 */
public class AdviceWithTest extends CamelTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        // remember to override this method and return true to tell Camel that we are using advice-with in the routes
        return true;
    }
}
