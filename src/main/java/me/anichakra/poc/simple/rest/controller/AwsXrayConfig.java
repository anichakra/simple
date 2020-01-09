package me.anichakra.poc.simple.rest.controller;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.xray.entities.Segment;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;

@Configuration
public class AwsXrayConfig {

	@Bean
	public Filter tracingFilter() {
		return new AWSXRayServletFilter("simple-rest-service") {
			public Segment preFilter(ServletRequest request, ServletResponse response) {
				((HttpServletResponse) response).addHeader("Abc", "Anirban");

				return super.preFilter(request, response);

			}

		};
	}
}
