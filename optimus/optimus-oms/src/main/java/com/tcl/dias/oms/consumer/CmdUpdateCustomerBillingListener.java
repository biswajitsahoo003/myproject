package com.tcl.dias.oms.consumer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.MDMOmsRequestBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.service.CustomerManagementService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * CmdUpdateCustomerBillingListener.java class for receive Customer billing Details
 * from CustmerService
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class CmdUpdateCustomerBillingListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CmdUpdateCustomerBillingListener.class);

	@Autowired
	CustomerManagementService customerManagementService;

	
	/**
	 * receive the Customer Billing Details from CustomerService
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${cmd.bill.queue}") })
	public void getCustomerOrderCount(String responseBody) throws TclCommonException {
		
		LOGGER.info("Inside the oms cmd queue");
		try {
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			MDMOmsRequestBean mDMOmsRequestBean = (MDMOmsRequestBean) Utils
					.convertJsonToObject(responseBody, MDMOmsRequestBean.class);
			 customerManagementService.cmdBillingDetailsUpdate(mDMOmsRequestBean);
			
		} catch (Exception e) {
			LOGGER.error("Error in process oms attachment information ", e);
		}
	}
	
}




