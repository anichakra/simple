package me.anichakra.poc.simple.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)

public class CorsFilterConfiguration {
	private String allowOrigin = "*";
	private String allowedHeader = "*";

	public String getAllowOrigin() {
		return allowOrigin;
	}

	public void setAllowOrigin(String allowOrigin) {
		this.allowOrigin = allowOrigin;
	}

	public String getAllowedHeader() {
		return allowedHeader;
	}

	public void setAllowedHeader(String allowedHeader) {
		this.allowedHeader = allowedHeader;
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin(allowOrigin);
		config.addAllowedHeader(allowedHeader);
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}