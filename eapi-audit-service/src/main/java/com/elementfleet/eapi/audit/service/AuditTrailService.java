package com.elementfleet.eapi.audit.service;

import java.text.ParseException;
import java.util.List;

import com.elementfleet.eapi.model.AuditTrailModel;
import com.elementfleet.eapi.model.AuditTrailRequest;
import com.elementfleet.eapi.model.AuditTrailResponse;
import com.elementfleet.eapi.model.security.UserDetails;

public interface AuditTrailService {
	/**
	 * Persist the audit trail details in database
	 * @param auditTrail
	 */
	public void saveAuditTrail(AuditTrailModel auditTrail);
	
	/**
	 * Retrieve the audit trail records from the database as per the request
	 * @param request
	 * @param user
	 * @return
	 * @throws ParseException
	 */
	public AuditTrailResponse getAuditTrail(AuditTrailRequest request, UserDetails user) throws ParseException;
	
	/**
	 * Retrieve audit trail records from database using file id 
	 * @param fileId
	 * @return
	 */
	public List<AuditTrailModel> getAuditTrailEntity(String fileId);
	
	/**
	 * Update file status in audit details.
	 * @param auditTrail
	 */
	public void updateFileStatus(AuditTrailModel auditTrail);
	
	/**
	 * Update total number of records in audit details
	 * @param auditTrail
	 */
	public void updateTotalRecords(AuditTrailModel auditTrail);

	/**
	 * Update error records in audit details
	 * @param auditTrail
	 */
	public void updateErrorRecords(AuditTrailModel auditTrail);

}
