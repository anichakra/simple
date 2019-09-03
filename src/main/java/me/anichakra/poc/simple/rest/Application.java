package me.anichakra.poc.simple.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
	    System.setProperty("node.id", String.valueOf(Math.random()).substring(2,4));
	    SpringApplication.run(Application.class, args);
	       
	}
}