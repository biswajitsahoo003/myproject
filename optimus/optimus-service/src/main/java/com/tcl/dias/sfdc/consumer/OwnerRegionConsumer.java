package com.tcl.dias.sfdc.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tcl.dias.common.sfdc.bean.OwnerRegionQueryResponseBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.services.SfdcService;

/**
 * This class is used as a response bean in capturing owner region
 * 
 *
 * @author Madhumiethaa Palanisamy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class OwnerRegionConsumer {
	@Autowired
	SfdcService sfdcService;

	private static final Logger LOGGER = LoggerFactory.getLogger(OwnerRegionConsumer.class);

	/**
	 * 
	 * processOwnerRegion is used to query owner region related fields from
	 * opportunity
	 * 
	 * @param responseBody
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${sfdc.process.ownerregion.query}") })
	public String processOwnerRegion(String responseBody) {
		OwnerRegionQueryResponseBean responseBean = null;
		String response = null;
		try {
			responseBean = sfdcService.processOwnerRegionQueryAPI(responseBody);
			response = Utils.convertObjectToJson(responseBean);
		} catch (Exception e) {
			LOGGER.error("Error in processOwnerRegion queue", e);
		}

		return response;
	}
}
