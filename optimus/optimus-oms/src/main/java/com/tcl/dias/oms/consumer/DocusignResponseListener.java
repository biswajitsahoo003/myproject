package com.tcl.dias.oms.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.CommonDocusignResponse;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.docusign.service.DocuSignUtilService;
import com.tcl.dias.oms.docusign.service.DocusignService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the DocusignResponseListener.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class DocusignResponseListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocusignResponseListener.class);

	@Autowired
	DocusignService docuSignService;

	/**
	 * processMailNotification- This method is used for mail notification listener
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${info.docusign.cof.response}") })
	public void processDocusignResponse(String responseBody) throws TclCommonException {
		try {
			LOGGER.info("Response body recieved froom docuSignResponseQueue is : {}", responseBody);
			CommonDocusignResponse commonDocusignResponse = (CommonDocusignResponse) Utils
					.convertJsonToObject(responseBody, CommonDocusignResponse.class);
			docuSignService.processDocuSignResponse(commonDocusignResponse);
		} catch (Exception e) {
			LOGGER.error("Error in process Docusign Repsonse ", e);
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${info.docusign.notification.response}") })
	public String processDocusignNotificationResponse(String responseBody) throws TclCommonException {
		try {
			CommonDocusignResponse commonDocusignResponse = (CommonDocusignResponse) Utils
					.convertJsonToObject(responseBody, CommonDocusignResponse.class);
			return docuSignService.processDocuSignNotificationResponse(commonDocusignResponse);
		} catch (Exception e) {
			LOGGER.error("Error in process Docusign notification Repsonse ", e);
		}
		return "";
	}

	@RabbitListener(queuesToDeclare = { @Queue("${info.docusign.error.response}") })
	public void processDocusignErrorResponse(String responseBody) throws TclCommonException {
		try {
			CommonDocusignResponse commonDocusignResponse = (CommonDocusignResponse) Utils
					.convertJsonToObject(responseBody, CommonDocusignResponse.class);
			docuSignService.processDocuSignNotificationResponse(commonDocusignResponse);
		} catch (Exception e) {
			LOGGER.error("Error in process Error Docusign notification Repsonse ", e);
		}
	}
}
