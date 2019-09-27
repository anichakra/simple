package com.elementfleet.eapi.audit.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.util.ReflectionTestUtils;

import com.elementfleet.eapi.audit.service.AuditTrailService;
import com.elementfleet.eapi.model.AuditTrailModel;
import com.elementfleet.eapi.model.AuditTrailRequest;
import com.elementfleet.eapi.model.AuditTrailResponse;
import com.elementfleet.eapi.model.security.UserDetails;

public class AuditControllerTest {

	@Qualifier("auditTrailServiceImpl")
	@Mock
	AuditTrailService service;

	private AuditController auditController;

	@Before
	public void setup() {
		auditController = new AuditController();
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(auditController, "service", service);
	}

	@Test
	public void testGetAuditRecords() throws IOException, ParseException {
		AuditTrailRequest request = new AuditTrailRequest();
		UserDetails userDetails = new UserDetails();
		AuditTrailResponse response = null;
		when(service.getAuditTrail(Mockito.isA(AuditTrailRequest.class), Mockito.isA(UserDetails.class)))
				.thenReturn(response);
		assertEquals(response, auditController.getAuditRecords(request, userDetails));
	}

	@Test
	public void testGetFileUploadDetail() throws IOException {
		String fileId = "1";
		List<AuditTrailModel> output = new ArrayList<>();
		UserDetails userDetails = new UserDetails();
		when(service.getAuditTrailEntity(Mockito.isA(String.class))).thenReturn(output);
		assertEquals(output, auditController.getFileUploadDetail(fileId, userDetails));
	}

	@Test
	public void testSaveAuditTrail() {
		AuditTrailModel auditTrail = new AuditTrailModel();
		UserDetails userDetails = new UserDetails();
		doNothing().when(service).saveAuditTrail(Mockito.isA(AuditTrailModel.class));
		// Mockito.when(service.saveAuditTrail(Mockito.isA(AuditTrailModel.class))).thenReturn(auditTrail);
		auditController.saveAuditTrail(auditTrail, userDetails);
	}

	@Test
	public void testUpdateFileStatus() {
		AuditTrailModel auditTrail = new AuditTrailModel();
		doNothing().when(service).updateFileStatus(Mockito.isA(AuditTrailModel.class));
		auditController.updateFileStatus(auditTrail);
	}

	@Test
	public void testUpdateTotalRecords() {
		AuditTrailModel auditTrail = new AuditTrailModel();
		doNothing().when(service).updateTotalRecords(Mockito.isA(AuditTrailModel.class));
		auditController.updateTotalRecords(auditTrail);
	}

	@Test
	public void testUpdateErrorRecords() {
		AuditTrailModel auditTrail = new AuditTrailModel();
		doNothing().when(service).updateErrorRecords(Mockito.isA(AuditTrailModel.class));
		auditController.updateErrorRecords(auditTrail);
	}

}
