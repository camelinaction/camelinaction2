package camelinaction;

import org.junit.Assert;
import org.junit.Test;

public class MyCommandTest {

    @Test
    public void testSuccess() {
        // reset counter
        GlobalState.COUNTER = 0;

        String out = new MyCommand().execute();

        Assert.assertEquals("Count 1", out);
    }

    @Test
    public void testFailure() {
        // reset counter
        GlobalState.COUNTER = 0;

        // the first 4 should be okay
        Assert.assertEquals("Count 1", new MyCommand().execute());
        Assert.assertEquals("Count 2", new MyCommand().execute());
        Assert.assertEquals("Count 3", new MyCommand().execute());
        Assert.assertEquals("Count 4", new MyCommand().execute());

        // this should fail
        try {
            Assert.assertEquals("Count 5", new MyCommand().execute());
            Assert.fail("Should throw exception");
        } catch (Exception e) {
            // expected
        }

    }
}
