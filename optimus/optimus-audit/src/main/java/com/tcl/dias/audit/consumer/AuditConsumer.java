package com.tcl.dias.audit.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.audit.service.AuditService;
import com.tcl.dias.common.beans.AuditRequestBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * AuditConsumer is the class for Audit API consumer methods
 * 
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class AuditConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuditConsumer.class);

	@Autowired
	AuditService auditService;

	@RabbitListener(queuesToDeclare = { @Queue("${optimus.audit.save.queue}") })
	public void processAudit(String requestBody) {

		try {
			auditService
					.processAudit((AuditRequestBean) Utils.convertJsonToObject(requestBody, AuditRequestBean.class));
			LOGGER.info("Data captured in Audit table");
		} catch (TclCommonException e) {
			LOGGER.error("Error occured in listener processAudit {} :", e);

		}
	}
}
