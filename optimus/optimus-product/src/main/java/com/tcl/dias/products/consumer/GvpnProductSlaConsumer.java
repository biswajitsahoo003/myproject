package com.tcl.dias.products.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.gvpn.service.v1.GVPNProductService;
import com.tcl.dias.products.ias.service.v1.IASProductService;
import com.tcl.dias.products.npl.service.v1.NPLProductService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the ProductSlaConsumer.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class GvpnProductSlaConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(GvpnProductSlaConsumer.class);

	@Autowired
	IASProductService iasProductService;

	@Autowired
	GVPNProductService gvpnProductService;

	@Autowired
	NPLProductService nplProductService;

	/**
	 * processMailNotification- This method is used for mail notification listener
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.gvpn.sla.queue}") })
	public String processSlaDetails(String request) throws TclCommonException {
		String response = "";
		try {
			ProductSlaBean productSlaBean = null;
			LOGGER.info("Input Payload received for sla with tier {}", request);
			productSlaBean = gvpnProductService.processProductSla(request);
			response = Utils.convertObjectToJson(productSlaBean);
			LOGGER.info("Response payload received for process sla city:{}", response);
		} catch (Exception e) {

			LOGGER.error("error in getting sla detailsa details ", e);
		}
		return response;
	}

	/**
	 * processMailNotification- This method is used for mail notification listener
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.gvpn.sla.city.queue}") })
	public String processSlaDetailsWithCity(String request) throws TclCommonException {
		String response = "";
		try {
			ProductSlaBean productSlaBean = null;
			LOGGER.info("Input Payload received for sla with city {}", request);
			productSlaBean = gvpnProductService.processProductSlaWithCity(request);
			response = Utils.convertObjectToJson(productSlaBean);
			LOGGER.info("Response payload received for process sla city:{}", response);
		} catch (Exception e) {

			LOGGER.error("error in getting sla detailsa details ", e);
		}
		return response;
	}

	/**
	 * processMailNotification- This method is used for mail notification listener
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.gvpn.citytier.queue}") })
	public String getTierByCity(String cityNm) throws TclCommonException {
		String tierCd = "1";
		try {
			LOGGER.info("Input Payload received for gvpn city queue {}",cityNm);
			tierCd = gvpnProductService.getTierCd(cityNm);
		} catch (Exception e) {

			LOGGER.error("error in getting sla detailsa details ", e);
		}
		return tierCd;
	}

}
