package com.tcl.dias.oms.consumer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.COPFOmsResponse;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the ProcessCopfListener.java class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class ProcessCopfOmsListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessCopfOmsListener.class);

	@Autowired
	OmsSfdcService omsSfdcService;

	
	/**
	 * 
	 * processCopfOmsResponse is used to process the COPFoms response
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${sfdc.process.copf.response}") })
	public void processCopfOmsResponse(String responseBody) throws TclCommonException {
		
		LOGGER.info("Inside the oms processCopfOmsResponse queue");
		try {
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			COPFOmsResponse copfOmsResponse = (COPFOmsResponse) Utils
					.convertJsonToObject(responseBody, COPFOmsResponse.class);
			omsSfdcService.processeCopfResponse(copfOmsResponse);
			
		} catch (Exception e) {
			LOGGER.error("Error in process oms attachment information ", e);
		}
	}

}
