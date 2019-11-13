package me.anichakra.poc.simple.rest.controller;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;

@Configuration
public class AwsXrayConfig {

  @Bean
  public Filter tracingFilter() {
    return new AWSXRayServletFilter("simple-rest-service");
  }
}