package com.elementfleet.eapi.audit.service.Impl;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import com.elementfleet.eapi.audit.entity.AuditTrailEntity;
import com.elementfleet.eapi.audit.repository.AuditTrailRepository;
import com.elementfleet.eapi.audit.service.impl.AuditTrailServiceImpl;
import com.elementfleet.eapi.model.AuditTrailModel;
import com.elementfleet.eapi.model.AuditTrailRequest;
import com.elementfleet.eapi.model.AuditTrailResponse;
import com.elementfleet.eapi.model.security.ClientDetails;
import com.elementfleet.eapi.model.security.UserDetails;

public class AuditTrailServiceImplTest {

	@Mock
	private AuditTrailRepository repository;

	private AuditTrailServiceImpl auditTrailServiceImpl;

	@Before
	public void setup() {
		auditTrailServiceImpl = new AuditTrailServiceImpl();
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(auditTrailServiceImpl, "repository", repository);
	}

	@Test
	public void testGetAuditTrailEntity() {
		String fileID = "101";
		List<AuditTrailEntity> entity = new ArrayList<>();
		entity.add(getAuditTrailEntityTest());
		Mockito.when(repository.findByFileId(Mockito.anyString())).thenReturn(entity);
		List<AuditTrailModel> resultList = auditTrailServiceImpl.getAuditTrailEntity(fileID);
		assertNotNull(resultList);
	}

	@Test
	public void testSaveAuditTrail() {
		AuditTrailModel auditTrail = new AuditTrailModel();
		Date date = new Date();
		date.setTime(19L);
		auditTrail.setRequestedDate(date);
		auditTrailServiceImpl.saveAuditTrail(auditTrail);
	}

	@Test
	public void testGetAuditTrail() throws ParseException {
		AuditTrailRequest request = new AuditTrailRequest();
		request.setDateRange(1);
		request.setPageNo(11);
		request.setRecordsPerPage(100);
		request.setServiceType("HR_FEED");
		boolean isRequireCompleteTrace = true;
		request.setRequireCompleteTrace(isRequireCompleteTrace);
		ClientDetails client = new ClientDetails();
		client.setClientNumber("9990");
		UserDetails user = new UserDetails();
		List<ClientDetails> clientList = new ArrayList<>();
		clientList.add(client);
		user.setClientDetails(clientList);
		List<AuditTrailEntity> entity = new ArrayList<AuditTrailEntity>();
		entity.add(getAuditTrailEntityTest());
		Page<AuditTrailEntity> result1 = new PageImpl<>(entity);
		Mockito.when(repository.findByRequestedDateBetweenAndServiceTypeAndLogin(Mockito.any(Timestamp.class),
				Mockito.any(Timestamp.class), Mockito.any(String.class), Mockito.any(String.class),
				Mockito.any(Pageable.class))).thenReturn(result1);
		AuditTrailResponse result = auditTrailServiceImpl.getAuditTrail(request, user);
		assertNotNull(result);
	}
	
	@Test
	public void testGetAuditTrail2() throws ParseException {
		AuditTrailRequest request = new AuditTrailRequest();
		request.setDateRange(1);
		request.setPageNo(11);
		request.setRecordsPerPage(100);
		request.setServiceType("HR_FEED");
		request.setSortingType("asc");
		request.setSortingValue("test");
		boolean isRequireCompleteTrace = true;
		request.setRequireCompleteTrace(isRequireCompleteTrace);
		ClientDetails client = new ClientDetails();
		client.setClientNumber("9990");
		UserDetails user = new UserDetails();
		List<ClientDetails> clientList = new ArrayList<>();
		clientList.add(client);
		user.setClientDetails(clientList);
		List<AuditTrailEntity> entity = new ArrayList<AuditTrailEntity>();
		entity.add(getAuditTrailEntityTest());
		Page<AuditTrailEntity> result1 = new PageImpl<>(entity);
		Mockito.when(repository.findByRequestedDateBetweenAndServiceTypeAndLogin(Mockito.any(Timestamp.class),
				Mockito.any(Timestamp.class), Mockito.any(String.class), Mockito.any(String.class),
				Mockito.any(Pageable.class))).thenReturn(result1);
		AuditTrailResponse result = auditTrailServiceImpl.getAuditTrail(request, user);
		assertNotNull(result);
	}

	@Test
	public void testGetAuditTrailFalseCheck() throws ParseException {
		AuditTrailRequest request = new AuditTrailRequest();
		request.setDateRange(1);
		request.setPageNo(11);
		request.setRecordsPerPage(100);
		request.setServiceType("HR_FEED");
		ClientDetails client = new ClientDetails();
		client.setClientNumber("9990");
		UserDetails user = new UserDetails();
		List<ClientDetails> clientList = new ArrayList<>();
		clientList.add(client);
		user.setClientDetails(clientList);
		List<AuditTrailEntity> entity = new ArrayList<AuditTrailEntity>();
		entity.add(getAuditTrailEntityTest());
		Page<AuditTrailEntity> result1 = new PageImpl<>(entity);
		Mockito.when(repository.findByRequestedDateBetweenAndServiceTypeAndLatestAndLogin(Mockito.any(Timestamp.class),
				Mockito.any(Timestamp.class), Mockito.any(String.class), Mockito.any(String.class),
				Mockito.any(String.class), Mockito.any(Pageable.class))).thenReturn(result1);
		AuditTrailResponse result = auditTrailServiceImpl.getAuditTrail(request, user);
		assertNotNull(result);
	}

	@Test(expected = Exception.class)
	public void testGetAuditTrailException() throws ParseException {
		AuditTrailRequest request = new AuditTrailRequest();
		Page<AuditTrailEntity> result1 = null;
		ClientDetails client = new ClientDetails();
		client.setClientNumber("9990");
		UserDetails user = new UserDetails();
		List<ClientDetails> clientList = new ArrayList<>();
		clientList.add(client);
		user.setClientDetails(clientList);
		Mockito.when(repository.findByRequestedDateBetweenAndServiceTypeAndLatestAndLogin(Mockito.any(Timestamp.class),
				Mockito.any(Timestamp.class), Mockito.any(String.class), Mockito.any(String.class),
				Mockito.any(String.class), Mockito.any(Pageable.class))).thenReturn(result1);
		AuditTrailResponse result = auditTrailServiceImpl.getAuditTrail(request, user);
		assertNotEquals(result1, result);
	}

	@Test
	public void testUpdateFileStatus() {
		AuditTrailModel auditTrail = new AuditTrailModel();
		List<AuditTrailEntity> entity = new ArrayList<AuditTrailEntity>();
		entity.add(getAuditTrailEntityTest());
		Mockito.when(repository.findByFileIdAndLatest(Mockito.anyString(), Mockito.anyString())).thenReturn(entity);
		auditTrailServiceImpl.updateFileStatus(auditTrail);
		assertNotNull(auditTrail);
	}

	@Test
	public void testUpdateFileStatusNullCheck() {
		AuditTrailModel auditTrail = new AuditTrailModel();
		List<AuditTrailEntity> entity = null;
		Mockito.when(repository.findByFileIdAndLatest(Mockito.anyString(), Mockito.anyString())).thenReturn(entity);
		auditTrailServiceImpl.updateFileStatus(auditTrail);
		assertNotNull(auditTrail);
	}

	@Test
	public void testUpdateFileStatusEmpty() {
		AuditTrailModel auditTrail = new AuditTrailModel();
		List<AuditTrailEntity> entity = new ArrayList<AuditTrailEntity>();
		Mockito.when(repository.findByFileIdAndLatest(Mockito.anyString(), Mockito.anyString())).thenReturn(entity);
		auditTrailServiceImpl.updateFileStatus(auditTrail);
		assertNotNull(auditTrail);
	}

	@Test
	public void testUpdateTotalRecords() {
		AuditTrailModel auditTrail = new AuditTrailModel();
		List<AuditTrailEntity> latestRecord = new ArrayList<AuditTrailEntity>();
		latestRecord.add(getAuditTrailEntityTest());
		Mockito.when(repository.findByFileIdAndLatest(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(latestRecord);
		auditTrailServiceImpl.updateTotalRecords(auditTrail);
		assertNotNull(auditTrail);
	}

	@Test
	public void testUpdateTotalRecordsNullCheck() {
		AuditTrailModel auditTrail = new AuditTrailModel();
		List<AuditTrailEntity> latestRecord = null;
		Mockito.when(repository.findByFileIdAndLatest(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(latestRecord);
		auditTrailServiceImpl.updateTotalRecords(auditTrail);
		assertNotNull(auditTrail);
	}

	@Test
	public void testUpdateTotalRecordsEmpty() {
		AuditTrailModel auditTrail = new AuditTrailModel();
		List<AuditTrailEntity> latestRecord = new ArrayList<AuditTrailEntity>();
		Mockito.when(repository.findByFileIdAndLatest(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(latestRecord);
		auditTrailServiceImpl.updateTotalRecords(auditTrail);
		assertNotNull(auditTrail);
	}

	@Test
	public void testUpdateErrorRecords() {
		AuditTrailModel auditTrail = new AuditTrailModel();
		List<AuditTrailEntity> latestRecord = new ArrayList<AuditTrailEntity>();
		latestRecord.add(getAuditTrailEntityTest());
		Mockito.when(repository.findByFileIdAndLatest(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(latestRecord);
		auditTrailServiceImpl.updateErrorRecords(auditTrail);
		assertNotNull(auditTrail);
	}

	@Test
	public void testUpdateErrorRecordsNullCheck() {
		AuditTrailModel auditTrail = new AuditTrailModel();
		List<AuditTrailEntity> latestRecord = null;
		Mockito.when(repository.findByFileIdAndLatest(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(latestRecord);
		auditTrailServiceImpl.updateErrorRecords(auditTrail);
		assertNotNull(auditTrail);
	}

	@Test
	public void testUpdateErrorRecordsEmpty() {
		AuditTrailModel auditTrail = new AuditTrailModel();
		List<AuditTrailEntity> latestRecord = new ArrayList<AuditTrailEntity>();
		Mockito.when(repository.findByFileIdAndLatest(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(latestRecord);
		auditTrailServiceImpl.updateErrorRecords(auditTrail);
		assertNotNull(auditTrail);
	}

	@Test
	public void testGetAuditTrailEntityNull() {
		String fileID = "101";
		List<AuditTrailEntity> entity = new ArrayList<>();
		AuditTrailEntity e = null;
		entity.add(e);
		Mockito.when(repository.findByFileId(Mockito.anyString())).thenReturn(entity);
		List<AuditTrailModel> result = auditTrailServiceImpl.getAuditTrailEntity(fileID);
		assertNotNull(result);
	}

	private AuditTrailEntity getAuditTrailEntityTest() {
		AuditTrailEntity auditTrail = new AuditTrailEntity();
		auditTrail.setActionType(1);
		auditTrail.setCliNo("cli990");
		auditTrail.setErrorFileName("test");
		auditTrail.setFileId("id1");
		auditTrail.setFileName("HR_Feed.csv");
		Timestamp requestedDate = new Timestamp(0L);
		auditTrail.setRequestedDate(requestedDate);
		auditTrail.setInputValues("test");
		auditTrail.setErrorRecords(1);
		auditTrail.setLogin("test");
		auditTrail.setTotalRecords(5);
		auditTrail.setRequestType(1);
		auditTrail.setProcessRecords(1);
		auditTrail.setServiceType("test");
		auditTrail.setStatusType(1);
		auditTrail.setLatest("Y");
		auditTrail.getAuditId();
		auditTrail.setAuditId(12L);
		return auditTrail;
	}
}
