package com.tcl.dias.oms.consumer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.DocusignGetDocumentRequest;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.docusign.service.DocusignService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
/**
 * 
 * THis is the Docusign listener for Docusign related part in OMS
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class DocusignListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DocusignListener.class);
	
	@Autowired
	DocusignService docuSignService;
	
	/**
	 * processMailNotification- This method is used for mail notification listener
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${info.docusign.get.document.request}") })
	public String processDocusignResponseGetDocument(String responseBody) throws TclCommonException {
		
		try {
			DocusignGetDocumentRequest docusignGetDocumentRequest=docuSignService.getDocusignGetDocumentRequestBasedOnQuoteCode(Integer.parseInt(responseBody));
			if(docusignGetDocumentRequest!=null) {
				return Utils.convertObjectToJson(docusignGetDocumentRequest);
			}
		} catch (Exception e) {
			LOGGER.error("Error in process Docusign Repsonse ", e);
		}
		return "";
	}
	
	/**
	 * 
	 * getCustomerDetailsForDocuSign - This Method is used for getting customer details
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${info.docusign.get.customer.request}") })
	@Transactional
	public String getCustomerDetailsForDocuSign(String responseBody) throws TclCommonException {
		
		try {
			Map<String, String> customerMapper=docuSignService.getCustomerDetailsForId(responseBody);
			if(customerMapper!=null) {
				return Utils.convertObjectToJson(customerMapper);
			}
		} catch (Exception e) {
			LOGGER.error("Error in process Customer Docusign Repsonse ", e);
		}
		return "";
	}
	
}
