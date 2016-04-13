package camelinaction;

import java.util.Random;

/**
 * A simple bean that return what many wise men have said for centuries.
 */
public class Quotes {

    public static String[] QUOTES = new String[]{"Camel rocks", "Donkeys are bad", "We like beer"};

    public String say() {
        int idx = new Random().nextInt(3);
        return String.format("{\"quote\": \"%s\"}", QUOTES[idx]);
    }
}
