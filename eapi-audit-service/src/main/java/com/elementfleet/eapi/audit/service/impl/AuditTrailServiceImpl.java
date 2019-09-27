package com.elementfleet.eapi.audit.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.elementfleet.eapi.audit.entity.AuditTrailEntity;
import com.elementfleet.eapi.audit.repository.AuditTrailRepository;
import com.elementfleet.eapi.audit.service.AuditTrailService;
import com.elementfleet.eapi.model.AuditTrailModel;
import com.elementfleet.eapi.model.AuditTrailRequest;
import com.elementfleet.eapi.model.AuditTrailResponse;
import com.elementfleet.eapi.model.security.UserDetails;

@Service
public class AuditTrailServiceImpl implements AuditTrailService {

	private static final String DEFAULT_SORTING = "requestedDate";

	private static final Direction DEFAULT_DIRECTION = Sort.Direction.DESC;

	@Autowired
	private AuditTrailRepository repository;

	private boolean checkRecordsNullOrEmpty(List<AuditTrailEntity> latestRecord) {
		return null != latestRecord && !latestRecord.isEmpty();
	}

	private Pageable createPageRequest(int pageNumber, int recordPerPage, Sort sort) {
		return new PageRequest(pageNumber, recordPerPage, sort);
	}
	/**
	 * Builds AuditTrailResponse from page
	 * @param entity
	 * @return
	 */
	private AuditTrailResponse getAuditDetailResponse(Page<AuditTrailEntity> entity) {
		AuditTrailResponse response = new AuditTrailResponse();
		List<AuditTrailModel> content = entity.getContent().stream().map(this::getAuditTrailModel)
				.collect(Collectors.toList());
		response.setContent(content);
		response.setFirst(entity.isFirst());
		response.setLast(entity.isLast());
		response.setNumber(entity.getNumber());
		response.setNumberOfElements(entity.getNumberOfElements());
		response.setSize(entity.getSize());
		response.setTotalElements(entity.getTotalElements());
		response.setTotalPages(entity.getTotalPages());
		return response;
	}

	@Override
	public AuditTrailResponse getAuditTrail(AuditTrailRequest request, UserDetails user) throws ParseException {
		String latest = "Y";
		Sort s = populateSortingFilters(request);
		Timestamp endDate = getFormattedDate(+1);
		Timestamp startDate = getFormattedDate(-request.getDateRange());		
		if (request.isRequireCompleteTrace()) {
			//If complete trace required, it is not needed to add the latest flag Y as predicate.
			Page<AuditTrailEntity> entity = repository.findByRequestedDateBetweenAndServiceTypeAndLogin(startDate,
					endDate, request.getServiceType(), user.getLogin(),
					createPageRequest(request.getPageNo(), request.getRecordsPerPage(), s));
			return getAuditDetailResponse(entity);
		}
		Page<AuditTrailEntity> entity = repository.findByRequestedDateBetweenAndServiceTypeAndLatestAndLogin(startDate,
				endDate, request.getServiceType(), latest, user.getLogin(),
				createPageRequest(request.getPageNo(), request.getRecordsPerPage(), s));

		return getAuditDetailResponse(entity);

	}

	/**
	 * Populating sorting filter
	 * @param request
	 * @return
	 */
	private Sort populateSortingFilters(AuditTrailRequest request) {
		String sorting = DEFAULT_SORTING;
		Direction sortingDirection = DEFAULT_DIRECTION;
		if (Strings.isNotBlank(request.getSortingValue())) {
			sorting = request.getSortingValue();
		}
		if ("asc".equalsIgnoreCase(request.getSortingType())) {
			sortingDirection = Sort.Direction.ASC;
		}
		return new Sort(sortingDirection, sorting);
	}

	@Override
	public List<AuditTrailModel> getAuditTrailEntity(String fileId) {
		return repository.findByFileId(fileId).stream().map(this::getAuditTrailModel).collect(Collectors.toList());
	}

	private AuditTrailModel getAuditTrailModel(AuditTrailEntity entity) {
		AuditTrailModel output = new AuditTrailModel();
		if (null != entity) {
			output.setActionType(entity.getActionType());
			output.setCliNo(entity.getCliNo());
			output.setErrorFileName(entity.getErrorFileName());
			output.setErrorRecords(entity.getErrorRecords());
			output.setFileId(entity.getFileId());
			output.setFileName(entity.getFileName());
			output.setInputValues(entity.getInputValues());
			output.setLogin(entity.getLogin());
			output.setProcessRecords(entity.getProcessRecords());
			output.setRequestedDate(entity.getRequestedDate());
			output.setRequestType(entity.getRequestType());
			output.setServiceType(entity.getServiceType());
			output.setStatusType(entity.getStatusType());
			output.setTotalRecords(entity.getTotalRecords());
			output.setLatest(entity.getLatest());
		}
		return output;
	}
	/**
	 * Convert the days to future date
	 * @param days
	 * @return
	 * @throws ParseException
	 */
	private Timestamp getFormattedDate(int days) throws ParseException {
		Timestamp time = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, days);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format1.format(cal.getTime());
		time = new Timestamp(format1.parse(formatted).getTime());
		return time;
	}
	/**
	 * Creating new auditTrailEntity 
	 * @param entity
	 * @return
	 */
	private AuditTrailEntity getNewAuditTrailEntity(AuditTrailEntity entity) {
		AuditTrailEntity newEntity = new AuditTrailEntity();
		newEntity.setActionType(entity.getActionType());
		newEntity.setCliNo(entity.getCliNo());
		newEntity.setErrorFileName(entity.getErrorFileName());
		newEntity.setErrorRecords(entity.getErrorRecords());
		newEntity.setFileId(entity.getFileId());
		newEntity.setFileName(entity.getFileName());
		newEntity.setInputValues(entity.getInputValues());
		newEntity.setLatest(entity.getLatest());
		newEntity.setLogin(entity.getLogin());
		newEntity.setProcessRecords(entity.getProcessRecords());
		newEntity.setRequestedDate(entity.getRequestedDate());
		newEntity.setRequestType(entity.getRequestType());
		newEntity.setServiceType(entity.getServiceType());
		newEntity.setStatusType(entity.getStatusType());
		newEntity.setTotalRecords(entity.getTotalRecords());
		return newEntity;
	}
	
	/**
	 * Persisting auditTrailEntity
	 */
	@Override
	public void saveAuditTrail(AuditTrailModel auditTrail) {
		auditTrail.setLatest("Y");
		repository.save(new AuditTrailEntity(auditTrail));
	}

	@Override
	public void updateErrorRecords(AuditTrailModel auditTrail) {
		List<AuditTrailEntity> latestRecord = repository.findByFileIdAndLatest(auditTrail.getFileId(), "Y");
		if (checkRecordsNullOrEmpty(latestRecord)) {
			AuditTrailEntity entity = latestRecord.get(0);
			AuditTrailEntity newEntity = getNewAuditTrailEntity(entity);
			newEntity.setErrorRecords(auditTrail.getErrorRecords());
			newEntity.setErrorFileName(auditTrail.getErrorFileName());
			newEntity.setStatusType(auditTrail.getStatusType());
			entity.setLatest("N");
			latestRecord.add(newEntity);
			repository.save(latestRecord);
		}
	}

	@Override
	@Transactional
	public void updateFileStatus(AuditTrailModel auditTrail) {
		List<AuditTrailEntity> latestRecord = repository.findByFileIdAndLatest(auditTrail.getFileId(), "Y");
		if (checkRecordsNullOrEmpty(latestRecord)) {
			AuditTrailEntity entity = latestRecord.get(0);
			AuditTrailEntity newEntity = getNewAuditTrailEntity(entity);
			latestRecord.add(newEntity);
			newEntity.setStatusType(auditTrail.getStatusType());
			if (Strings.isNotEmpty(auditTrail.getErrorFileName())) {
				newEntity.setErrorFileName(auditTrail.getErrorFileName());
			}
			// Added Code to update records
			newEntity.setErrorRecords(auditTrail.getErrorRecords());
			newEntity.setProcessRecords(auditTrail.getProcessRecords());
			newEntity.setTotalRecords(auditTrail.getTotalRecords());
			entity.setLatest("N");
			repository.save(latestRecord);
		}
	}

	@Override
	public void updateTotalRecords(AuditTrailModel auditTrail) {
		List<AuditTrailEntity> latestRecord = repository.findByFileIdAndLatest(auditTrail.getFileId(), "Y");
		if (checkRecordsNullOrEmpty(latestRecord)) {
			AuditTrailEntity entity = latestRecord.get(0);
			AuditTrailEntity newEntity = getNewAuditTrailEntity(entity);
			latestRecord.add(newEntity);
			newEntity.setTotalRecords(auditTrail.getTotalRecords());
			newEntity.setProcessRecords(auditTrail.getProcessRecords());
			entity.setLatest("N");
			repository.save(latestRecord);
		}
	}

}
