package com.tcl.dias.oms.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.common.webex.beans.OutboundPriceConversionBean;
import com.tcl.dias.common.webex.beans.WebexPriceConversionBean;
import com.tcl.dias.oms.webex.service.WebexQuoteService;

/**
 * Listener for webex quote
 * 
 * @author Srinivasa Raghavan
 *
 */
@Service
@Transactional
public class WebexQuoteListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebexQuoteListener.class);

	@Autowired
	private WebexQuoteService webexQuoteService;

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.webex.convert.price.currency}") })
	public String convertWebexVoicePrices(String request) {
		String response = "";
		try {
			List<WebexPriceConversionBean> webexPriceConversion = Utils.fromJson(request,
					new TypeReference<List<WebexPriceConversionBean>>() {
					});
			LOGGER.info("Received {} conversion request", webexPriceConversion);
			response = Utils.convertObjectToJson(webexQuoteService.convertWebexVoicePrices(webexPriceConversion));
			LOGGER.info("Sending {} conversion response", response);
		} catch (Exception e) {
			LOGGER.warn("Error in converting prices", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.outbound.convert.price.currency}") })
	public String convertOutboundPrices(String request) {
		String response = "";
		try {
			List<OutboundPriceConversionBean> outboundPriceConversion = Utils.fromJson(request,
					new TypeReference<List<OutboundPriceConversionBean>>() {
					});
			LOGGER.info("Received conversion request {}", !outboundPriceConversion.isEmpty());
			response = Utils.convertObjectToJson(webexQuoteService.convertOutboundPrices(outboundPriceConversion));
			LOGGER.info("Sending conversion response {}", !response.isEmpty());
		} catch (Exception e) {
			LOGGER.warn("Error in converting prices", e);
		}
		return response;
	}

}
