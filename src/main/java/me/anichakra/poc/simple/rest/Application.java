package me.anichakra.poc.simple.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.context.config.annotation.EnableContextInstanceData;

@SpringBootApplication
//@EnableContextInstanceData

public class Application {

    public static void main(String[] args) {
        System.setProperty(
                "node.id", String.valueOf(Math.random()).substring(2, 4));
        SpringApplication.run(Application.class, args);
    }

}
