package me.anichakra.poc.simple.rest;

import java.nio.file.SecureDirectoryStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableContextInstanceData

public class Application {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.setProperty(
                "node.id", String.valueOf(SecureRandom.getInstanceStrong().nextLong()).substring(2, 4));
        SpringApplication.run(Application.class, args);
    }
}
