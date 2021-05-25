package com.tcl.dias.oms.consumer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.MDMOmsResponseBean;
import com.tcl.dias.common.beans.ResponseResource;
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
public class ProcessMDMResponseListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessMDMResponseListener.class);

	@Autowired
	OmsSfdcService omsSfdcService;

	
	/**
	 * process Mdm Request.
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${mdm.process.response}") })
	public void UpdateMDMResponse(String responseBody) throws TclCommonException {
		
		LOGGER.info("Inside the MDM response queue");
		try {
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			MDMOmsResponseBean mDMResponseBean = (MDMOmsResponseBean) Utils
					.convertJsonToObject(responseBody, MDMOmsResponseBean.class);
			omsSfdcService.processeMDMResponse(mDMResponseBean);
			
		} catch (Exception e) {
			LOGGER.error("Error in process oms attachment information ", e);
		}
	}
	
}




