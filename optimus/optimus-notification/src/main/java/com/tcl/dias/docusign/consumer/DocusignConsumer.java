package com.tcl.dias.docusign.consumer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.CommonDocusignRequest;
import com.tcl.dias.common.beans.DocusignGetDocumentRequest;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.docusign.beans.DocuSignNotificationResponse;
import com.tcl.dias.docusign.service.DocuSignService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the DocusignConsumer.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class DocusignConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocusignConsumer.class);

	@Autowired
	DocuSignService docuSignService;

	@RabbitListener(queuesToDeclare = { @Queue("${info.docusign.cof.sign}") })
	public void processDocusign(String responseBody) {
		try {
			docuSignService.processDocusign(
					(CommonDocusignRequest) Utils.convertJsonToObject(responseBody, CommonDocusignRequest.class));
		} catch (Exception e) {
			LOGGER.error("Error in docusign ", e);
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${mq.docusign.notification.queue}") })
	@Async
	public void processDocusignNotificationResponse(String responseBody) throws TclCommonException {
		try {
			LOGGER.info("Entering queue mq.docusign.notification.queue");
			docuSignService.processDocusignNotication((DocuSignNotificationResponse) Utils
					.convertJsonToObject(responseBody, DocuSignNotificationResponse.class));
		} catch (Exception e) {
			LOGGER.error("Error in process Docusign Notification Repsonse ", e);
		}
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${info.docusign.retry}") })
	public void processRetryDocusignNotificationResponse(String responseBody) throws TclCommonException {
		try {
			docuSignService.processDocusignNotication((DocuSignNotificationResponse) Utils
					.convertJsonToObject(responseBody, DocuSignNotificationResponse.class));
		} catch (Exception e) {
			LOGGER.error("Error in process Retry Docusign Notification Repsonse ", e);
		}
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${info.docusign.audit}") })
	public String processDocusignDocumentDelete(String responseBody) throws TclCommonException {
		try {
			if(!StringUtils.isAllBlank(responseBody)) {
				return docuSignService.moveEnvelopeToRecycleBin(responseBody);
			}
		} catch (Exception e) {
			LOGGER.error("Error in process Docusign Notification Repsonse ", e);
		}
		return "";
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${info.docusign.envelope.retry}") })
	public String processDocusignRetry(String responseBody) {
		try {
			if(!StringUtils.isAllBlank(responseBody)) {
				return docuSignService.retryDocusignDocument(responseBody);
			}
		} catch (Exception e) {
			LOGGER.error("Error in process Docusign Notification Repsonse ", e);
		}
		return "";
	}
	
	
	@RabbitListener(queuesToDeclare = { @Queue("${info.docusign.resend}") })
	public String processDocusignDocumentResend(String responseBody) throws TclCommonException {
		try {
			if(!StringUtils.isAllBlank(responseBody)) {
				return docuSignService.resendEnvelope(responseBody);
			}
		} catch (Exception e) {
			LOGGER.error("Error in process Docusign Notification Repsonse ", e);
		}
		return "";
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${info.docusign.getdocument}") })
	public byte[] getEnvelopeDocument(String responseBody) throws TclCommonException {
		try {
			if(!StringUtils.isAllBlank(responseBody)) {
				DocusignGetDocumentRequest docusignGetDocumentRequest = (DocusignGetDocumentRequest) Utils.convertJsonToObject(responseBody, DocusignGetDocumentRequest.class);
				if(docusignGetDocumentRequest!=null && docusignGetDocumentRequest.getDocumentId()!=null && docusignGetDocumentRequest.getEnvelopeId()!=null) {
					return docuSignService.getEnvelopeDocuments(docusignGetDocumentRequest.getEnvelopeId(), docusignGetDocumentRequest.getDocumentId(), 1);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in process Docusign Notification Repsonse ", e);
		}
		return new byte[0];
	}
	

}
