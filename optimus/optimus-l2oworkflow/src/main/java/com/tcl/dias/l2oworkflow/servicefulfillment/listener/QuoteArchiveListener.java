package com.tcl.dias.l2oworkflow.servicefulfillment.listener;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.PriceDiscountBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflow.entity.entities.SiteDetail;
import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflow.entity.repository.TaskRepository;
import com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants;

/**
 * 
 * This file contains the QuoteArchiveListener.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class QuoteArchiveListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteArchiveListener.class);
	
	@Autowired
	TaskRepository taskRepository;

	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.quote.arch.request}") })
	public void processActiveDeactivateQuote(String responseBody) {
		try {

			LOGGER.info("Input Payload received for processActiveDeactivateQuote ");
			String request =responseBody;
			if (request == null) {
				LOGGER.error("NO request data processActiveDeactivateQuote");
			} else {
				Map<String,String> requestMap = Utils.convertJsonToObject(request, Map.class);
				String quoteCode=requestMap.get("quoteCode");
				String action =requestMap.get("action");
				if(action.equalsIgnoreCase("DEACTIVATE")) {
					List<Task> tasks=taskRepository.findByQuoteCode(quoteCode);
					for (Task task : tasks) {
						if(task.getIsActive().equals(CommonConstants.BACTIVE)) {
							task.setIsActive(CommonConstants.BDEACTIVATE);
							taskRepository.save(task);
						}
					}
				}else {
					List<Task> tasks=taskRepository.findByQuoteCode(quoteCode);
					for (Task task : tasks) {
						if(task.getIsActive().equals(CommonConstants.BDEACTIVATE)) {
							task.setIsActive(CommonConstants.BACTIVE);
							taskRepository.save(task);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in Disabling task ", e);
		}
	}

}
