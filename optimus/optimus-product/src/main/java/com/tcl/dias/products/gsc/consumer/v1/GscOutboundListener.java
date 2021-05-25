package com.tcl.dias.products.gsc.consumer.v1;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.gsc.beans.GscOutboundPricingBean;
import com.tcl.dias.products.gsc.beans.OutboundSurchargePricingBean;
import com.tcl.dias.products.gsc.service.v1.GscOutboundPricingService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Listeneres related to Outbound pricing
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscOutboundListener {

	@Autowired
	GscOutboundPricingService gscOutboundPricingService;

	private static final Logger LOGGER = LoggerFactory.getLogger(GscOutboundListener.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.outbound.surcharge.pricing}") })
	public String getOutboundSurchargePrices(String request) throws TclCommonException {
		List<OutboundSurchargePricingBean> gscOutboundSurchargePricingBeans = new ArrayList<>();
		String response = "";
		try {
			gscOutboundSurchargePricingBeans = gscOutboundPricingService.getAllSurchargeCharges();
			response = Utils.convertObjectToJson(gscOutboundSurchargePricingBeans);
		} catch (Exception e) {
			LOGGER.warn("Error in fetching surcharge prices ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.outbound.pricing}") })
	public String getOutboundPrices(String request) throws TclCommonException {
		List<GscOutboundPricingBean> gscOutboundPricingBeans = new ArrayList<>();
		String response = "";
		try {
			gscOutboundPricingBeans = gscOutboundPricingService.getOutboundPrices();
			response = Utils.convertObjectToJson(gscOutboundPricingBeans);
		} catch (Exception e) {
			LOGGER.warn("Error in fetching outbound prices ", e);
		}
		return response;
	}
}
