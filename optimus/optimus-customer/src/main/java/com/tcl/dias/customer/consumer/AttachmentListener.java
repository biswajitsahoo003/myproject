package com.tcl.dias.customer.consumer;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.ObjectStorageListenerBean;
import com.tcl.dias.common.beans.PartnerDocumentBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.customer.service.v1.AttachmentService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class contains listener method for attachment
 * 
 * @author SEKHAR ER
 * 
 * @link http://www.tatacommunications.com
 * @copyright 2018 Tata Communications Limited
 *
 */
@Service
public class AttachmentListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentListener.class);

	@Autowired
	AttachmentService attachmentService;

	/**
	 * processMailNotification- This method is used for mail notification listener
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${attatchment.queue}") })
	public Integer processAttachmentInformation(String responseBody) throws TclCommonException {
		Integer attachmentId = -1;
		try {
			AttachmentBean attachmentRequest = (AttachmentBean) Utils
					.convertJsonToObject(responseBody, AttachmentBean.class);
			attachmentId = attachmentService.processAttachment(attachmentRequest);
		} catch (Exception e) {
			LOGGER.error("Error in process Attachment information ", e);
		}
		LOGGER.info("Returning attachment Id {}",attachmentId);
		return attachmentId;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${file.download.queue}") })
	public String processDownloadInformation(String responseBody) throws TclCommonException {
		String url = "";
		try {
			Integer attachmentId = Integer.valueOf(responseBody);
			url = attachmentService.getDownloadPath(attachmentId);
		} catch (Exception e) {
			LOGGER.error("Error in process Attachment information ", e);
		}
		return url;
	}

	/**
	 * processRequestId - this method is used to return the name of the attachment
	 * that is used as the request Id for generating temporary url for documents
	 * stored in storage container
	 * 
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */

	@RabbitListener(queuesToDeclare = { @Queue("${attachment.requestId.queue}") })
	public String processRequestId(String responseBody) throws TclCommonException {
		String response = "";
		AttachmentBean attachmentBean = null;
		try {
			Integer attachmentId = Integer.valueOf(responseBody);
			attachmentBean = attachmentService.getRequestId(attachmentId);
			response = Utils.convertObjectToJson(attachmentBean);
		} catch (Exception e) {
			LOGGER.error("Error in process Attachment information ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${odr.attachment.details}") })
	public String getAttachmentDetailsForOrderFlatTable(String responseBody) throws TclCommonException {
		String response = "";
		Map<String, Object> attachmentBean = null;
		try {
			Integer attachmentId = Integer.valueOf(responseBody);
			attachmentBean = attachmentService.getAttachmentDetails(attachmentId);
			response = Utils.convertObjectToJson(attachmentBean);
		} catch (Exception e) {
			LOGGER.error("Error in process Attachment information ", e);
		}
		return response;
	}

	/**
	 * process Attachment Information
	 * 
	 * @param responseBody
	 * @return String
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${object.storage.file.move.queue}") })
	public String processOmsAttachments(String responseBody) throws TclCommonException {
		try {
			ObjectStorageListenerBean[] omsAttachmentRequest = (ObjectStorageListenerBean[]) Utils
					.convertJsonToObject(responseBody, ObjectStorageListenerBean[].class);
			attachmentService.updateFilePathForFiles(omsAttachmentRequest);
		} catch (Exception e) {
			LOGGER.error("error in getting processOmsAttachments ", e);
		}
		return "";

	}

	@RabbitListener(queuesToDeclare = { @Queue("${object.storage.gsc.country.files.move.queue}") })
	public String getCountryDocuments(String request) {
		try {
			ObjectStorageListenerBean omsAttachmentRequest = (ObjectStorageListenerBean) Utils.convertJsonToObject(request, ObjectStorageListenerBean.class);
			ObjectStorageListenerBean objListenerBean = attachmentService.updateFilePathForCountryDocuments(omsAttachmentRequest);
		} catch (Exception e) {
			LOGGER.error("error in getting country files ", e);
		}
		return "";
	}

	/**
	 *	Get Attachment Details from attachmentId's
	 *
	 * @param request
	 * @return {@link String}
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.get.partner.documents.details}") })
	public String getAttachmentDetails(String request) {
		String response = "";
		try {
			List<Integer> attachmentIds = Utils.fromJson(request, new TypeReference<List<Integer>>() {
			});

			List<PartnerDocumentBean> partnerDocumentBeans = attachmentService.getAttachmentDetails(attachmentIds);
			response = Utils.convertObjectToJson(partnerDocumentBeans);
		} catch (TclCommonException e) {
			LOGGER.error("error in getting attachment details ", e);
		}
		return response!=null?response:"";
	}
}
