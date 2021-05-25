package com.tcl.dias.oms.consumer;

import java.util.HashMap;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.dias.oms.ipc.service.v1.IPCPricingService;
import com.tcl.dias.oms.npl.service.v1.NplPricingFeasibilityService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the DiscountApprovalListener.java class. Process discount
 * approval request
 *
 * @author Danish
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
@Transactional
public class DiscountApprovalListener {

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	IPCPricingService ipcPricingService;

	@Autowired
	IllPricingFeasibilityService illPricingFeasibilityService;

	@Autowired
	NplPricingFeasibilityService nplPricingFeasibilityService;

	@Autowired
	OmsUtilService omsUtilService;

	private static final Logger LOGGER = LoggerFactory.getLogger(DiscountApprovalListener.class);

	@Async
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.discount.approval}") })
	public void processFeasibility(String responseBody) throws TclCommonException {
		try {
			LOGGER.info("Input Payload received for final approval from Commercial :: {}", responseBody);
			LOGGER.info("Input Payload received for feasibility :: {}", responseBody);
			HashMap<String, Object> inputMap = (HashMap) Utils.convertJsonToObject(responseBody, HashMap.class);
			Optional<Quote> quote = quoteRepository.findById((Integer) inputMap.get("quoteId"));
			if (quote.isPresent()) {
				if (quote.get().getQuoteCode().startsWith("IPC")) {
					ipcPricingService.processFinalApproval(inputMap);
				} else if (quote.get().getQuoteCode().startsWith("NPL")
						|| quote.get().getQuoteCode().startsWith("NDE")) {
					nplPricingFeasibilityService.processFinalApproval(inputMap);
				} else {
					illPricingFeasibilityService.processFinalApproval(inputMap);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in processFeasibility", e);
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.currency.conversion.rate}") })
	public String getCurrencyConversionRate(Message<String> request) throws TclCommonException {
		String response = "1";
		try {
			String currencyRate = omsUtilService.findCurrencyConversionRate(request.getPayload());
			if (currencyRate != null) {
				response = currencyRate;
			}
		} catch (Exception e) {
			LOGGER.error("Error in getCurrencyConversionRate", e);
		}
		return response;
	}
}
