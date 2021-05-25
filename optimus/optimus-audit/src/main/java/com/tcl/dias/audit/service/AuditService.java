package com.tcl.dias.audit.service;

import java.util.Date;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.audit.constants.ExceptionConstants;
import com.tcl.dias.audit.entity.entities.Audit;
import com.tcl.dias.audit.entity.repository.AuditRepository;
import com.tcl.dias.common.beans.AuditRequestBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Audit service is the class of audit service methods
 * 
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class AuditService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

	@Autowired
	AuditRepository auditRepository;

	/**
	 * processAudit method to construct and save Audit data
	 * 
	 * @param auditRequestBean
	 * @throws TclCommonException
	 */
	public void processAudit(AuditRequestBean auditRequestBean) throws TclCommonException {
		try {
			if (Objects.isNull(auditRequestBean)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			LOGGER.info("Entering method to set Audit data processAudit {} :", auditRequestBean);
			Audit audit = new Audit();
			audit.setQuoteCode(auditRequestBean.getQuoteCode());
			audit.setFromValue(auditRequestBean.getFromValue());
			audit.setToValue(auditRequestBean.getToValue());
			audit.setMdcToken(auditRequestBean.getMdcToken());
			audit.setStage(auditRequestBean.getStage());
			audit.setCreatedTime(new Date());
			audit.setCreatedBy(auditRequestBean.getCreatedBy());
			audit.setComments(auditRequestBean.getComments());
			audit.setRequestUrl(auditRequestBean.getRequestUrl());
			audit.setResponse(auditRequestBean.getResponse());
			audit.setHttpMethod(auditRequestBean.getHttpMethod());
			audit.setRoundTripTime(auditRequestBean.getRoundTripTime());
			audit.setSessionStateId(auditRequestBean.getSessionState());
			audit.setRequestPayload(auditRequestBean.getPayloadRequest());
			audit.setResponsePayload(auditRequestBean.getPayloadResponse());
			auditRepository.save(audit);
			LOGGER.info("Request Data captured in Audit table");
		} catch (TclCommonException e) {
			LOGGER.error("Error while saving data to Audit processAudit {} :", e);
		}

	}
}
