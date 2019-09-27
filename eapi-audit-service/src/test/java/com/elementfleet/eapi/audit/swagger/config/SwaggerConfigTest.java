package com.elementfleet.eapi.audit.swagger.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.elementfleet.eapi.audit.swagger.config.SwaggerConfig;



public class SwaggerConfigTest {
	private SwaggerConfig swagger;
	
	@Before
	public void setup() {
		swagger=new SwaggerConfig();
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void testProductApi() {
		assertNotNull(swagger.productApi());
	}
	
	

	@Test
	public void testSwagger() {
		swagger.getContactEmail();
		swagger.getContactName();
		swagger.getContactUrl();
		swagger.getDescription();
		swagger.getDescription();
		swagger.getLicense();
		swagger.getLicenseUrl();
		swagger.getTermsOfService();
		swagger.getTitle();
		swagger.getVersion();
		
		swagger.setContactEmail("ab@gmail.com");
		swagger.setContactName("Name");
		swagger.setContactUrl("https//localhost.com");
		swagger.setDescription("desc");
		swagger.setLicense("license");
		swagger.setLicenseUrl("abc");
		swagger.setTermsOfService("termsOfService");
		swagger.setTitle("title");
		swagger.setVersion("version");
	    assertNotNull(swagger);
	}
}
