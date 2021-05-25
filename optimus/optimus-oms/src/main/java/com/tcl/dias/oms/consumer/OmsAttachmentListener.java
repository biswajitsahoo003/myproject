package com.tcl.dias.oms.consumer;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.BulkSiteBean;
import com.tcl.dias.common.beans.OmsListenerBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.gsc.beans.GscOutboundAttachmentBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.dias.oms.npl.service.v1.NplPricingFeasibilityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanQuoteService;
import com.tcl.dias.oms.service.OmsAttachmentService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the OmsAttachmentListener.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class OmsAttachmentListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(OmsAttachmentListener.class);

	@Autowired
	OmsAttachmentService omsAttachmentService;

	@Autowired
	GscOrderService gscOrderService;
	
	@Autowired
	IllPricingFeasibilityService illPricingFeasibilityService;
	
	@Autowired
	NplPricingFeasibilityService nplPricingFeasibilityService;

	@Autowired
	IzosdwanQuoteService izosdwanQuoteService;


	/**
	 * processMailNotification- This method is used for mail notification listener
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${oms.attatchment.queue}") })
	public void processOmsAttachmentInformation(String responseBody) throws TclCommonException {
		try {
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			LOGGER.info("file upload response from customer{}",responseBody.toString());
			OmsListenerBean omsAttachmentRequest = (OmsListenerBean) Utils
					.convertJsonToObject(responseBody, OmsListenerBean.class);
			omsAttachmentService.processOmsAttachment(omsAttachmentRequest);
			
		} catch (Exception e) {
			LOGGER.error("Error in process oms attachment information ", e);
		}
	}

	/**
	 * process Attachment Information
	 * 
	 * @param responseBody
	 * @return String
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${oms.gsc.attachment.queue}") })
	public String processOmsGscAttachmentInformation(String responseBody) throws TclCommonException {
		try {
			Objects.requireNonNull(responseBody, GscConstants.ATTACHMENT_IS_NULL);
			OmsAttachment omsAttachment = omsAttachmentService
					.processOmsGscAttachment(responseBody);
			if (Objects.isNull(omsAttachment.getId())) {
				return "";
			}
			return omsAttachment.getErfCusAttachmentId().toString();
		} catch (Exception e) {
			LOGGER.error("Error in process oms attachment information related to GSC ", e);
		}
		return "";
	}

	/**
	 * process Attachment and Quote Information
	 *
	 * @param responseBody
	 * @return String
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${oms.quote.attachment.queue}") })
	public String processOmsAttachmentAndQuote(String responseBody) throws TclCommonException {
		try {
			Objects.requireNonNull(responseBody, "Quote Code is null");
			GscOutboundAttachmentBean attachmentrequest = (GscOutboundAttachmentBean) Utils
					.convertJsonToObject(responseBody, GscOutboundAttachmentBean.class);
			Integer erfCustomerAttachmentId = gscOrderService.downloadOutboundPrices(attachmentrequest);
			return String.valueOf(erfCustomerAttachmentId);
		} catch (Exception e) {
			LOGGER.error("Error in process oms attachment information related to GSC ", e);
		}
		return "";
	}

	
	/**
	 * processMailNotification- This method is used for mail notification listener
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${oms.mf.attatchment.queue}") })
	public String processOmsAttachmentForMF(String responseBody) throws TclCommonException {
		try {
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			
			OmsListenerBean omsAttachmentRequest = (OmsListenerBean) Utils
					.convertJsonToObject(responseBody, OmsListenerBean.class);
			
			if(omsAttachmentRequest.isDeleteAttachmentReference()) {
				omsAttachmentService.deleteOmsAttachmentReferenceForMF(omsAttachmentRequest);
			}else {
				omsAttachmentService.processOmsAttachmentForMF(omsAttachmentRequest);
			}
		} catch (Exception e) {
			LOGGER.error("Error in process oms attachment information ", e);
		}
		return "";

	}
	
	/**
	 * processBulkSiteUploadExcel- This method is used for process BulkSiteUploadExcel listener
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${oms.bulk.site.process.queue}") })
	public void processBulkSiteUploadExcel(String responseBody) throws TclCommonException {
		try {
			LOGGER.info("Entered into processBulkSiteUploadExcel");
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			BulkSiteBean bulkSiteBean = (BulkSiteBean) Utils
					.convertJsonToObject(responseBody, BulkSiteBean.class);
			if(bulkSiteBean.getQuoteCode().startsWith("IAS") || bulkSiteBean.getQuoteCode().startsWith("GVPN") ) {
				illPricingFeasibilityService.updatePriceFromExcel(bulkSiteBean);
			}
			if(bulkSiteBean.getQuoteCode().startsWith("NPL") || bulkSiteBean.getQuoteCode().startsWith("NDE") ) {
				nplPricingFeasibilityService.updatePriceFromExcel(bulkSiteBean);
			}
			
		} catch (Exception e) {
			LOGGER.error("Error in process oms attachment information ", e);
		}
		

	}

	/**
	 * processMailNotification- This method is used for mail notification listener
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${oms.attachment.queue.sdd}") })
	public void processOmsAttachmentInformationSDD(String responseBody) throws TclCommonException {
		try {
			LOGGER.info("Entering into sdd attachment queue");
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			OmsListenerBean omsAttachmentRequest = (OmsListenerBean) Utils
					.convertJsonToObject(responseBody, OmsListenerBean.class);

			izosdwanQuoteService.processOmsAttachmentSDD(omsAttachmentRequest);

		} catch (Exception e) {
			LOGGER.error("Error in process oms attachment information ", e);
		}
	}


}
