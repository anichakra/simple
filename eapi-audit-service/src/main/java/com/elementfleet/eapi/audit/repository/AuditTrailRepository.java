package com.elementfleet.eapi.audit.repository;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Nullable;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elementfleet.eapi.audit.entity.AuditTrailEntity;
/**
 * JPA repository to do the all database related operation for the audit details
 *
 */
@Repository
public interface AuditTrailRepository extends JpaRepository<AuditTrailEntity, Long> {
	/**
	 * Retrieve AuditTrailEntity by file id and latest
	 * @param fileId
	 * @param latest
	 * @return AuditTrailEntity object or null
	 */
	List<AuditTrailEntity> findByFileIdAndLatest(String fileId,String latest);
	
	/**
	 * Retrieve AuditTrailEntity by file id
	 * @param fileId
	 * @return AuditTrailEntity object or null
	 */
	List<AuditTrailEntity> findByFileId(String fileId);
	
	/**
	 * Retrieve AuditTrailEntity by 
	 * @param startDate
	 * @param endDate
	 * @param serviceType
	 * @param latest
	 * @param login
	 * @param pageable
	 * @return AuditTrailEntity object or null
	 */
	// @Query("select a from supplier_api_audit a where (a.requested_date between ?1
	// and ?2) and a.cli_no=?3 and a.service_type=?4")
	Page<AuditTrailEntity> findByRequestedDateBetweenAndServiceTypeAndLatestAndLogin(Timestamp startDate, Timestamp endDate,
			 String serviceType,String latest, String login, Pageable pageable);// ORDER BY ?5 ?6 String colName, String colOrder,
	
	Page<AuditTrailEntity> findByRequestedDateBetweenAndServiceTypeAndLogin(Timestamp startDate, Timestamp endDate,
			 String serviceType, String login, Pageable pageable);// ORDER BY ?5 ?6 String colName, String colOrder,

	/**
	 * This method is to be in phase 2
	 * 
	 * @param startDate
	 * @param endDate
	 * @param cliNo
	 * @param serviceType
	 * @param pageable
	 * @return AuditTrailEntity object or null
	 */
	@Nullable
	@Query(nativeQuery = true, value = "select action_type, cli_no, error_file_name, error_records, file_id, file_name, input_values, login, process_records, request_type, service_type, status_type, total_records, audit_id, max(requested_date) as requested_date from supplier_api_audit  where (requested_date between :startDate and :endDate) and cli_no=:cliNo and service_type=:serviceType group by action_type, cli_no, error_file_name, error_records, file_id, file_name, input_values, login, process_records, request_type, requested_date, service_type, status_type, total_records, audit_id limit 10 offset 0 order by ?# (#pageable)")
	Page<AuditTrailEntity> getByRequestedDateBetweenAndCliNoAndServiceType(@Param("startDate") Timestamp startDate,
			@Param("endDate") Timestamp endDate, @Param("cliNo") String cliNo, @Param("serviceType") String serviceType,
			Pageable pageable);// ORDER BY ?5 ?6 String colName, String colOrder,
	/**
	 * Update file status in audit details
	 * @param statusType
	 * @param requestedDate
	 * @param fileId
	 */
	@Modifying
	@Transactional
	@Query("update AuditTrailEntity t set t.statusType = ?1,t.requestedDate=?2 where t.fileId = ?3")
	void updateFileStatus(String statusType, Timestamp requestedDate, String fileId);

	/**
	 * Update error records in audit details
	 * @param errorRecords
	 * @param fieName
	 * @param requestedDate
	 * @param fileId
	 */
	@Modifying
	@Transactional
	@Query("update AuditTrailEntity t set t.errorRecords = ?1,t.errorFileName = ?2 ,t.requestedDate=?3 where t.fileId = ?4")
	void updateErrorRecords(int errorRecords, String fieName, Timestamp requestedDate, String fileId);
	
	/**
	 * Update total number of records in audit details
	 * @param totalRecords
	 * @param requestedDate
	 * @param fileId
	 */
	@Modifying
	@Transactional
	@Query("update AuditTrailEntity t set t.totalRecords = ?1,t.requestedDate=?2 where t.fileId = ?3")
	void updateTotalRecords(int totalRecords, Timestamp requestedDate, String fileId);
	
	/**
	 * Update processed records in audit details 
	 * @param processRecords
	 * @param requestedDate
	 * @param fileId
	 */
	@Modifying
	@Transactional
	@Query("update AuditTrailEntity t set t.processRecords = ?1,t.requestedDate=?2 where t.fileId = ?3")
	void updateProcessRecords(int processRecords, Timestamp requestedDate, String fileId);
	
	/**
	 * Update audit entry in audit details
	 * @param latest
	 * @param fileId
	 */
	@Modifying
	@Query("update AuditTrailEntity t set t.latest = ?1 where t.fileId = ?2")
	void updateAuditEntry(String latest, String fileId);
}