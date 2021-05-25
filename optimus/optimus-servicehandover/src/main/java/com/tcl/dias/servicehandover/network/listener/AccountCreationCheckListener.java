package com.tcl.dias.servicehandover.network.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.service.BillingAccountCreationService;

/**
 * AccountCreationCheckListener for Account Creation is Required or not ?
 * @author yomagesh
 *
 */
@Component
public class AccountCreationCheckListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountCreationCheckListener.class);
	
	@Autowired
	BillingAccountCreationService accountCreationService;
	
	/**
	 * Listener for Account Creation
	 * @param request
	 * @return
	 */

	@RabbitListener(queuesToDeclare = {@Queue("${queue.accountCreateSync.check}") })
	public String accountCreationCheck(String request) {
		LOGGER.info("inside accountCreationCheck");
		String status="";
		try {
			String req = request;
			String orderId = req.split("#")[0];
			String serviceCode= req.split("#")[2];
			String serviceId= req.split("#")[3];
			LOGGER.info("accountCreationCheck orderId{} serviceCode{} serviceType{}",orderId,serviceCode);
			boolean isAccountCreationRequired = accountCreationService.isAccountCreationRequired(orderId,serviceCode,serviceId);
			if(isAccountCreationRequired) {
			   return NetworkConstants.ACC_CREATION_REQUIRED;
			}else {
				return NetworkConstants.ACC_CREATION_NOT_REQUIRED;
			}
		} catch (Exception e) {
			LOGGER.error("Error in accountCreationCheck", e);
		}
		LOGGER.info("accountCreationCheck completed with status:  {} ",status);
		return status;
	}
}
;