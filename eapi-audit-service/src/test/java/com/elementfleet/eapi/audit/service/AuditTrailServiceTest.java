package com.elementfleet.eapi.audit.service;

import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.elementfleet.eapi.audit.entity.AuditTrailEntity;
import com.elementfleet.eapi.audit.repository.AuditTrailRepository;
import com.elementfleet.eapi.audit.service.impl.AuditTrailServiceImpl;
import com.elementfleet.eapi.model.AuditTrailModel;
import com.elementfleet.eapi.model.AuditTrailRequest;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class AuditTrailServiceTest {

	@Mock
	private AuditTrailRepository repository;

	private AuditTrailServiceImpl auditTrailService;
	
	private AuditTrailRequest request;

	@Before
	public void setUp() throws Exception {

		auditTrailService = new AuditTrailServiceImpl();
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(auditTrailService, "repository", repository);
	}

	@Test
	public void testSaveAuditTrail() {
		AuditTrailModel auditTrail = getAuditTrailModelData();
		List<AuditTrailEntity> entities = new ArrayList<>();
		when(repository.save(anyCollectionOf(AuditTrailEntity.class))).thenReturn(entities);
		auditTrailService.saveAuditTrail(auditTrail);
	}

	/*@Test
	public void TestGetAuditTrail() {
		List<Pageable> content = new ArrayList<Pageable>();
		Page<AuditTrailEntity> page = null;
		request = new AuditTrailRequest();
		request.setRecordsPerPage(10);
		when(repository.findByRequestedDateBetweenAndCliNoAndServiceType(isA(Timestamp.class), isA(Timestamp.class), isA(String.class), isA(String.class), isA(Pageable.class))).thenReturn(page);
		AuditTrailResponse result = auditTrailService.getAuditTrail(request);
		assertEquals(page, result);
	}*/

	private AuditTrailModel getAuditTrailModelData() {
		AuditTrailModel model = new AuditTrailModel();
		model.setActionType(1);
		model.setCliNo("9990");
		model.setErrorFileName("");
		model.setFileId("111");
		model.setFileName("");
		model.setRequestedDate(Calendar.getInstance().getTime());
		return model;
	}

}
