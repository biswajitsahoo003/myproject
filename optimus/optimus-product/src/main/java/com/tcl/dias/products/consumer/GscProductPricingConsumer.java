package com.tcl.dias.products.consumer;

import java.util.List;

import com.tcl.dias.products.gsc.service.v1.GscProductServiceMatrixService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.gsc.beans.GscOutboundPricingBean;
import com.tcl.dias.products.gsc.service.v1.GscOutboundPricingService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional
public class GscProductPricingConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(GscProductSlaConsumer.class);

	@Autowired
	GscOutboundPricingService gscOutboundPricingService;

	@Autowired
	GscProductServiceMatrixService gscProductServiceMatrixService;
	
	/**
	 * Process Destionation
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.gsc.outbound.pricing.queue}") })
	public String procesDestinationDetails(String request) throws TclCommonException {
		String response = "";
		try {
			List<GscOutboundPricingBean> gscOutboundPricingBean = null;
			gscOutboundPricingBean = gscOutboundPricingService.getDestinationIdAndComments(request);
			response = Utils.convertObjectToJson(gscOutboundPricingBean);
		} catch (Exception e) {
			LOGGER.warn("error in getting destination details ", e);
		}
		return response;
	}
	
	/**
	 * Process Outbound details
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.gsc.outbound.queue}") })
	public String procesOutboundDetails(String request) throws TclCommonException {
		String response = "";
		try {
			List<GscOutboundPricingBean> gscOutboundPricingBean = null;
			gscOutboundPricingBean = gscOutboundPricingService.getOutboundDetails(request);
			response = Utils.convertObjectToJson(gscOutboundPricingBean);
		} catch (Exception e) {
			LOGGER.warn("error in getting destination details ", e);
		}
		return response;
	}

	/**
	 * Queue call to get distinct outbound countries
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.gsc.distinct.countries}") })
	public String getDistinctCountriesByProduct(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("In consumer to get distinct countries by product");
			List<String> distinctOutboundCountries = gscProductServiceMatrixService.getDistinctCountriesByProduct(request);
			response = Utils.convertObjectToJson(distinctOutboundCountries);
		} catch (Exception e) {
			LOGGER.warn("error in getting outbound country details ", e);
		}
		return response;
	}
}
