package com.tcl.dias.audit.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.audit.service.ThirdPartyServiceAuditService;
import com.tcl.dias.common.beans.ThirdPartyServiceAuditBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the ThirdPartyServiceAuditConsumer.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class ThirdPartyServiceAuditConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyServiceAuditConsumer.class);

	@Autowired
	ThirdPartyServiceAuditService thirdPartyServiceAuditService;

	@RabbitListener(queuesToDeclare = { @Queue("${optimus.audit.thirdparty.queue}") })
	public void processThirdPartyAudit(String requestBody) {

		try {
			thirdPartyServiceAuditService.processAudit((ThirdPartyServiceAuditBean) Utils
					.convertJsonToObject(requestBody, ThirdPartyServiceAuditBean.class));
			LOGGER.info("Data captured in ThirdParty  Audit table");
		} catch (TclCommonException e) {
			LOGGER.error("Error occured in listener in ThirdParty processAudit {} :", e);

		}
	}
}
