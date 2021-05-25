package com.tcl.dias.audit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.audit.entity.entities.ThirdPartyServiceAudit;
import com.tcl.dias.audit.entity.repository.ThirdPartyServiceAuditRepository;
import com.tcl.dias.common.beans.ThirdPartyServiceAuditBean;

/**
 * This file contains the ThirdPartyServiceAuditService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class ThirdPartyServiceAuditService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyServiceAuditService.class);

	@Autowired
	ThirdPartyServiceAuditRepository thirdPartyServiceAuditRepository;

	/**
	 * 
	 * processAudit
	 * 
	 * @param thirdPartyServiceAuditBean
	 */
	public void processAudit(ThirdPartyServiceAuditBean thirdPartyServiceAuditBean) {
		LOGGER.info("Entering the audit to save the thirdParty Service");
		try {
			ThirdPartyServiceAudit thirdPartyServiceAudit = new ThirdPartyServiceAudit();
			thirdPartyServiceAudit.setCreatedBy(thirdPartyServiceAuditBean.getCreatedBy());
			thirdPartyServiceAudit.setCreatedTime(thirdPartyServiceAuditBean.getCreatedTime());
			thirdPartyServiceAudit.setHttpMethod(thirdPartyServiceAuditBean.getHttpMethod());
			thirdPartyServiceAudit.setMdcToken(thirdPartyServiceAuditBean.getMdcToken());
			thirdPartyServiceAudit.setRequestHeader(thirdPartyServiceAuditBean.getRequestHeader());
			thirdPartyServiceAudit.setRequestPayload(thirdPartyServiceAuditBean.getRequestPayload());
			thirdPartyServiceAudit.setResponsePayload(thirdPartyServiceAuditBean.getResponsePayload());
			if (thirdPartyServiceAuditBean.getRequestUrl() != null
					&& thirdPartyServiceAuditBean.getRequestUrl().length() > 250) {
				LOGGER.info("Request Url is long so triming the last 250 characters");
				thirdPartyServiceAudit.setUrl(thirdPartyServiceAuditBean.getRequestUrl().substring(0, 250));
			} else {
				thirdPartyServiceAudit.setUrl(thirdPartyServiceAuditBean.getRequestUrl());
			}
			thirdPartyServiceAudit.setSessionStateId(thirdPartyServiceAuditBean.getSessionStateId());
			thirdPartyServiceAudit.setStatusCode(thirdPartyServiceAuditBean.getStatusCode());
			thirdPartyServiceAuditRepository.save(thirdPartyServiceAudit);
		} catch (Exception e) {
			LOGGER.error("Error in saving the thirdpartyservice audit", e);
		}
	}

}
