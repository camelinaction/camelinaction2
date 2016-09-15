package camelinaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Basic Spring Boot application that loads the Camel XML file using @ImportResource
 */
@SpringBootApplication
@ImportResource("classpath:mycamel.xml")
public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }
}
