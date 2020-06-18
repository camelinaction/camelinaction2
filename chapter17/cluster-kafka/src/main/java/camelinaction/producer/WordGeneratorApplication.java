package camelinaction.producer;

import org.apache.camel.main.Main;

public class WordGeneratorApplication {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new WordRoute());
        main.run();
    }
}
