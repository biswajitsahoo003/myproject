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
import com.tcl.dias.common.sfdc.bean.BCROmsResponse;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 *  ProcessBcrListener.java class for process bcr Request
 * 
 *
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class ProcessBcrListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessBcrListener.class);

	@Autowired
	OmsSfdcService omsSfdcService;

	
	/**
	 * process bcr Request.
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${sfdc.process.bcr}") })
	public void getCustomerOrderCount(String responseBody) throws TclCommonException {
		
		LOGGER.info("Inside the oms bcr queue");
		String response = "";
		try {
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			BCROmsResponse bCROmsResponse = (BCROmsResponse) Utils
					.convertJsonToObject(responseBody, BCROmsResponse.class);
			omsSfdcService.processeBcrResponse(bCROmsResponse);
			
		} catch (Exception e) {
			LOGGER.error("Error in process oms attachment information ", e);
		}
	}
	
}




