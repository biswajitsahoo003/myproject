package com.tcl.dias.sfdc.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.sfdc.bean.CreditCheckQueryResponseBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.services.SfdcService;

/**
 * This class is used as a response bean in credit check
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class CreditCheckListener {
	

	@Autowired
	SfdcService sfdcService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreditCheckListener.class);
	/**
	 * 
	 * processCreditCheck is used to query credit check related fields from opportunity
	 * @param responseBody
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${sfdc.process.creditcheck.query}") })
	public void processCreditCheck(String responseBody) {
		try {
			LOGGER.info("Request in queue {}",responseBody);
			sfdcService.processCreditCheckQueryAPI(responseBody);
		} catch (Exception e) {
			LOGGER.error("Error in processCreditCheck queue", e);
		}
	}
	
	
	
	/**
	 * 
	 * processCreditCheckRetrigger is used to retrigger query for credit check related fields from opportunity
	 * @param responseBody
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${sfdc.process.creditcheck.retrigger.queue}") })
	public String processCreditCheckRetrigger(String responseBody) {
		CreditCheckQueryResponseBean responseBean = null;
		String response = null;
		try {
			responseBean = sfdcService.processRetriggerCreditCheckQueryAPI(responseBody);
			response = Utils.convertObjectToJson(responseBean);
		} catch (Exception e) {
			LOGGER.error("Error in processCreditCheckRetrigger queue", e);
		}
		
		return response;
	}

}
