package com.elementfleet.eapi.audit; 

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

import com.elementfleet.eapi.framework.service.annotation.Microservice;
@Microservice(scanBasePackages= {"com.elementfleet","com.element"})
@EntityScan(basePackages = {"com.elementfleet.eapi.audit.entity"} )
@EnableJpaRepositories(basePackages = {"com.elementfleet.eapi.audit.repository"})
public class AuditServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuditServiceApplication.class, args);
	}
	
	@Configuration
	class Config {

		@LoadBalanced
		@Bean
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}
}