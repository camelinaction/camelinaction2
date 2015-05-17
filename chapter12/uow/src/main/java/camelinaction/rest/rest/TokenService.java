package camelinaction.rest.rest;

import java.util.Random;

/**
 * A token service that generates uuid tokens
 */
public class TokenService {

    private static Random random = new Random();

    /**
     * Generate next token
     */
    public static long token() {
        return Math.abs(random.nextLong());
    }
}
