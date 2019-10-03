package me.anichakra.poc.simple.rest;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.javafaker.Faker;

@SpringBootApplication
//@EnableContextInstanceData

public class Application {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.setProperty("node.id", String.valueOf(SecureRandom.getInstanceStrong().nextLong()).substring(2, 4) + "-"
				+ new Faker(Locale.ENGLISH).gameOfThrones().character());
		SpringApplication.run(Application.class, args);
	}
}
