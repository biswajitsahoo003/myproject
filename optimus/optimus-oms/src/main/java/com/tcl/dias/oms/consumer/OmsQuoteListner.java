package com.tcl.dias.oms.consumer;

import java.util.ArrayList;
import java.util.List;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.dto.QuoteDto;
import com.tcl.dias.oms.dto.QuoteToLeDto;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
/**
 * Oms Quote Listener
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class OmsQuoteListner {
	private static final Logger LOGGER = LoggerFactory.getLogger(OmsAttachmentListener.class);

	@Autowired
	IllQuoteService illQuoteService;
	/**
	 * getting order count based on customer.
	 * @param customerId
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${info.oms.get.order.count.queue}") })
	public String getCustomerOrderCount(String responseBody) throws TclCommonException {
		LOGGER.info("Inside the oms user queue");
		String response = "";
		try {
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			List<Integer> customerId = new ArrayList<>();
			String[] customerLeIds = (responseBody).split(",");
			for(String customerLeId:customerLeIds) {
				customerId.add(Integer.parseInt(customerLeId));
			}

			Long count= illQuoteService.getCustomerOrderCount(customerId);
			response = Utils.convertObjectToJson(count);
		} catch (Exception e) {
			LOGGER.error("Error in process oms attachment information ", e);
		}
		return response;
	}
	
	/**
	 * getQuoteDataById queue to get quote data for given quoteId
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${oms.get.quote.data.by.id}") })
	public String getQuoteDataById(String quoteId) {
		String response = "";
		try {
			QuoteDto quoteDatas = illQuoteService.getQuoteDataById(quoteId);
			response = Utils.convertObjectToJson(quoteDatas);
		} catch (TclCommonException e) {
			LOGGER.error("Error in fetching quote information ", e);
		}
		return response;
	}
	
	/**
	 * getQuoteDataByQuoteLeId queue to get quoteToLe data for given quoteLeId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${oms.get.quotetole.data.by.id}") })
	public String getQuoteDataByQuoteLeId(String quoteLeId) {
		String response = "";
		try {
			QuoteToLeDto quoteDatas = illQuoteService.getQuoteLeById(quoteLeId);
			response = Utils.convertObjectToJson(quoteDatas);
		} catch (TclCommonException e) {
			LOGGER.error("Error in fetching QuoteToLe information ", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${oms.get.quotetole.data.by.id}") })
	public String getQuoteDataByQuoteCode(String request) {
		String response = "";
		try {
			QuoteToLeDto quoteDatas = illQuoteService.getQuoteLeById(request);
			response = Utils.convertObjectToJson(quoteDatas);
		} catch (TclCommonException e) {
			LOGGER.error("Error in fetching QuoteToLe information ", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${oms.get.optid.data.by.quotecode}") })
	public String getOpportunity(String request) {
		String response = "";
		try {
			LOGGER.info("Enter getOpportunity method "+request);
			response = illQuoteService.getOpporunityId(request);
			LOGGER.info("Opportunity id%%%%%%%%%%"+response);
		} catch (TclCommonException e) {
			LOGGER.error("Error in fetching QuoteToLe information ", e);
		}
		return response;
	}

}
