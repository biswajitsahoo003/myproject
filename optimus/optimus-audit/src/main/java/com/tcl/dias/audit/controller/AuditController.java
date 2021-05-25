package com.tcl.dias.audit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.audit.service.AuditService;
import com.tcl.dias.audit.swagger.constants.SwaggerConstants;
import com.tcl.dias.common.beans.AuditRequestBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/audit")
public class AuditController {

	@Autowired
	AuditService auditService;

	@ApiOperation(value = SwaggerConstants.Feedback.SAVE_FEEDBACK_DETAILS)
	@PostMapping()
	public ResponseResource<String> saveAudit(@RequestBody AuditRequestBean auditRequestBean)
			throws TclCommonException {
		auditService.processAudit(auditRequestBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS);

	}

}