package com.elementfleet.eapi.audit.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.elementfleet.eapi.audit.service.AuditTrailService;
import com.elementfleet.eapi.framework.security.common.annotation.GetUserDetailsInfo;
import com.elementfleet.eapi.model.AuditTrailModel;
import com.elementfleet.eapi.model.AuditTrailRequest;
import com.elementfleet.eapi.model.AuditTrailResponse;
import com.elementfleet.eapi.model.security.UserDetails;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(value = "Audit-Service-App", description = "This is audit controller for audit service app", hidden = true)
@RefreshScope
public class AuditController {

	@Autowired
	@Qualifier("auditTrailServiceImpl")
	private AuditTrailService service;

	@PreAuthorize("#oauth2.hasScope('ENTERPRISEAPI_HISTORY_HR') or #oauth2.hasScope('ENTERPRISEAPI_MAINT_HISTORY') or #oauth2.hasScope('ENTERPRISEAPI_AUDIT')")
	@PostMapping(value = "/getAuditRecords")
	@ApiOperation(value = "Getting audit trail records by passing user values")
	@ResponseBody
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success Response"),
			@ApiResponse(code = 500, message = "Audit record not found for given input") })
	public AuditTrailResponse getAuditRecords(@RequestBody AuditTrailRequest request,
			@ApiIgnore @GetUserDetailsInfo() UserDetails user) throws ParseException {
		return service.getAuditTrail(request, user);
	}

	@PreAuthorize("#oauth2.hasScope('ENTERPRISEAPI_HISTORY_HR') or #oauth2.hasScope('ENTERPRISEAPI_MAINT_HISTORY') or #oauth2.hasScope('ENTERPRISEAPI_AUDIT')")
	@PostMapping(value = "/getFileUploadDetail")
	@ApiOperation(value = "Getting audit trail records by passing fileID")
	@ResponseBody
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success Response"),
			@ApiResponse(code = 500, message = "Audit record not found for given fileId") })
	public List<AuditTrailModel> getFileUploadDetail(@RequestBody String fileId,
			@ApiIgnore @GetUserDetailsInfo() UserDetails user) {
		return service.getAuditTrailEntity(fileId);
	}

	@PostMapping(value = "/saveAuditRecord")
	@ApiOperation(value = "saves single audit record")
	@ResponseBody
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success Response"),
			@ApiResponse(code = 500, message = "Record could not be Saved") })
	public void saveAuditTrail(@RequestBody AuditTrailModel auditTrail,
			@ApiIgnore @GetUserDetailsInfo() UserDetails user) {
		service.saveAuditTrail(auditTrail);
	}

	@PostMapping(value = "/updateFileStatus")
	@ApiOperation(value = "updates status of the file")
	@ResponseBody
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success Response"),
			@ApiResponse(code = 500, message = "Record could not be Saved") })
	public void updateFileStatus(@RequestBody AuditTrailModel auditTrail) {
		service.updateFileStatus(auditTrail);
	}

	@PostMapping(value = "/updateTotalRecords")
	@ApiOperation(value = "update total records of the file")
	@ResponseBody
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success Response"),
			@ApiResponse(code = 500, message = "Record could not be Saved") })
	public void updateTotalRecords(@RequestBody AuditTrailModel auditTrail) {
		service.updateTotalRecords(auditTrail);
	}

	@PostMapping(value = "/updateErrorRecords")
	@ApiOperation(value = "update error records of the file")
	@ResponseBody
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success Response"),
			@ApiResponse(code = 500, message = "Record could not be Saved") })
	public void updateErrorRecords(@RequestBody AuditTrailModel auditTrail) {
		service.updateErrorRecords(auditTrail);
	}

}
