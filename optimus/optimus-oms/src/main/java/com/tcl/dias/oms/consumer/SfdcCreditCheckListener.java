package com.tcl.dias.oms.consumer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.sfdc.bean.CreditCheckQueryResponseBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the listener for the sfdc credit check
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class SfdcCreditCheckListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SfdcCreditCheckListener.class);

	@Autowired
	OmsSfdcService omsSfdcService;
	
	
	@RabbitListener(queuesToDeclare = { @Queue("${sfdc.credit.check.response}") })
	public void processCreditCheckResponse(String responseBody) throws TclCommonException {
		
		LOGGER.info("Inside the oms processCreditCheckResponse queue, response {}", responseBody);
		try {
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			CreditCheckQueryResponseBean creditCheckResponse = (CreditCheckQueryResponseBean) Utils
					.convertJsonToObject(responseBody, CreditCheckQueryResponseBean.class);
			omsSfdcService.processCreditCheckResponse(creditCheckResponse);
			
		} catch (Exception e) {
			LOGGER.error("Error in process credir check information ", e);
		}
	}


}
