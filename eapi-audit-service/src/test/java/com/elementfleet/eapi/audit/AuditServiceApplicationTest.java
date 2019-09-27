package com.elementfleet.eapi.audit;

import org.junit.Before;
import org.mockito.MockitoAnnotations;



public class AuditServiceApplicationTest {

	AuditServiceApplication auditServiceApplication;
	@Before
	public void setUp() throws Exception {
		auditServiceApplication=new AuditServiceApplication();
		MockitoAnnotations.initMocks(this);
	}

	
/*	@Bean
    @Primary
    @Test
    public  void testDefaultSample() {
		AlwaysSampler sampler = auditServiceApplication.defaultSample();
		assertNotNull(sampler);
		assertTrue("AlwaysSampler bean returning successfully. ",sampler instanceof AlwaysSampler );
    }
	
	@Test
	public void testRestTemplateSender() {
		ZipkinProperties zipkin = new ZipkinProperties();
		ZipkinRestTemplateCustomizer zipkinRestTemplateCustomizer = new DefaultZipkinRestTemplateCustomizer(zipkin);
		assertNotNull(auditServiceApplication.restTemplateSender(zipkin, zipkinRestTemplateCustomizer));
	}*/
}
