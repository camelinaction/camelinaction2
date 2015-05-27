package camelinaction;

import java.util.Random;

/**
 * Generates an unique random id
 */
public class GuidGenerator {

    public static int generate() {
        Random ran = new Random();
        return ran.nextInt(10000000);
    }

}
