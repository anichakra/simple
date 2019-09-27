package com.elementfleet.eapi.audit.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.elementfleet.eapi.model.AuditTrailModel;

@Entity
@Table(name = "AUDIT_DETAILS", schema = "eapi")
public class AuditTrailEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public AuditTrailEntity(AuditTrailModel model) {
		this.actionType = model.getActionType();
		this.cliNo = model.getCliNo();
		this.errorFileName = model.getErrorFileName();
		this.errorRecords = model.getErrorRecords();
		this.fileId = model.getFileId();
		this.fileName = model.getFileName();
		this.inputValues = model.getInputValues();
		this.login = model.getLogin();
		this.processRecords = model.getProcessRecords();
		this.requestedDate = new Timestamp(model.getRequestedDate().getTime());
		this.requestType = model.getRequestType();
		this.serviceType = model.getServiceType();
		this.statusType = model.getStatusType();
		this.totalRecords = model.getTotalRecords();
		this.latest = model.getLatest();

	}

	public AuditTrailEntity() {
	}

	private Long auditId;

	private Timestamp requestedDate;

	private String serviceType;

	private int requestType;

	private String fileName;

	private int statusType;

	private int actionType;

	private int totalRecords;

	private int errorRecords;

	private int processRecords;

	private String fileId;

	private String login;

	private String cliNo;

	private String inputValues;

	private String errorFileName;

	private String latest;

	@Id
	@Column(name = "AUDIT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	/**
	 * @return the requestedDate
	 */
	@Column(name = "REQUESTED_DATE")
	public Timestamp getRequestedDate() {
		return requestedDate;
	}

	/**
	 * @param requestedDate
	 *            the requestedDate to set
	 */
	public void setRequestedDate(Timestamp requestedDate) {
		this.requestedDate = requestedDate;
	}

	/**
	 * @return the requestType
	 */
	@Column(name = "REQUEST_TYPE")
	public int getRequestType() {
		return requestType;
	}

	/**
	 * @param requestType
	 *            the requestType to set
	 */
	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}

	/**
	 * @return the fileName
	 */
	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the statusType
	 */
	@Column(name = "STATUS_TYPE")
	public int getStatusType() {
		return statusType;
	}

	/**
	 * @param statusType
	 *            the statusType to set
	 */
	public void setStatusType(int statusType) {
		this.statusType = statusType;
	}

	/**
	 * @return the actionType
	 */
	@Column(name = "ACTION_TYPE")
	public int getActionType() {
		return actionType;
	}

	/**
	 * @param actionType
	 *            the actionType to set
	 */
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	/**
	 * @return the totalRecords
	 */
	@Column(name = "TOTAL_RECORDS")
	public int getTotalRecords() {
		return totalRecords;
	}

	/**
	 * @param totalRecords
	 *            the totalRecords to set
	 */
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	/**
	 * @return the errorRecords
	 */
	@Column(name = "ERROR_RECORDS")
	public int getErrorRecords() {
		return errorRecords;
	}

	/**
	 * @param errorRecords
	 *            the errorRecords to set
	 */
	public void setErrorRecords(int errorRecords) {
		this.errorRecords = errorRecords;
	}

	/**
	 * @return the processRecords
	 */
	@Column(name = "PROCESS_RECORDS")
	public int getProcessRecords() {
		return processRecords;
	}

	/**
	 * @param processRecords
	 *            the processRecords to set
	 */
	public void setProcessRecords(int processRecords) {
		this.processRecords = processRecords;
	}

	/**
	 * @return the fileId
	 */
	@Column(name = "FILE_ID")
	public String getFileId() {
		return fileId;
	}

	/**
	 * @param fileId
	 *            the fileId to set
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * @return the login
	 */
	@Column(name = "LOGIN")
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the cliNo
	 */
	@Column(name = "CLI_NO")
	public String getCliNo() {
		return cliNo;
	}

	/**
	 * @param cliNo
	 *            the cliNo to set
	 */
	public void setCliNo(String cliNo) {
		this.cliNo = cliNo;
	}

	/**
	 * @return the inputValues
	 */
	@Column(name = "INPUT_VALUES")
	public String getInputValues() {
		return inputValues;
	}

	/**
	 * @param inputValues
	 *            the inputValues to set
	 */
	public void setInputValues(String inputValues) {
		this.inputValues = inputValues;
	}

	/**
	 * @return the errorFileName
	 */
	@Column(name = "ERROR_FILE_NAME")
	public String getErrorFileName() {
		return errorFileName;
	}

	/**
	 * @param errorFileName
	 *            the errorFileName to set
	 */
	public void setErrorFileName(String errorFileName) {
		this.errorFileName = errorFileName;
	}

	/**
	 * @return the serviceType
	 */
	@Column(name = "SERVICE_TYPE")
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType
	 *            the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	@Column(name = "AUDIT_LATEST")
	public String getLatest() {
		return latest;
	}

	public void setLatest(String latest) {
		this.latest = latest;
	}

}
