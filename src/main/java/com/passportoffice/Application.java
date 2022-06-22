package com.passportoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.passportoffice",
        "com.passportoffice.api",
        "com.passportoffice.configuration",
        "com.passportoffice.service",
        "validation.validator",
        "test"
})
public class Application {

    public static void main(String[] args) {
        new SpringApplication(Application.class).run(args);
    }
}
