package com.tcl.dias.products.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.dto.SLADto;
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
public class ProductSlaConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSlaConsumer.class);

	@Autowired
	IASProductService iasProductService;

	@Autowired
	NPLProductService nplProductService;

	/**
	 * processMailNotification- This method is used for mail notification listener
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.sla.queue}") })
	public String processSlaDetails(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for sla with tier {}", request);
			ProductSlaBean productSlaBean = iasProductService.processProductSla(request);

			response = Utils.convertObjectToJson(productSlaBean);

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
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.sla.city.queue}") })
	public String processSlaDetailsWithCity(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for sla with city {}", request);
			ProductSlaBean productSlaBean = iasProductService.processProductSlaWithCity(request);

			response = Utils.convertObjectToJson(productSlaBean);

		} catch (Exception e) {

			LOGGER.error("error in getting sla detailsa details ", e);
		}
		return response;
	}

	/**
	 * getSlaDetails- This method is used to get sla details
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.npl.sla.queue}") })
	public String getSlaDetails(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for sla with serviceVarient and accessTopology {}", request);
			String[] spliter = (request).split(",");
			String serviceVarient = spliter[0];
			String accessTopology = spliter[1];
			List<SLADto> slaDetails = nplProductService.getSlaValue(serviceVarient, accessTopology);
			response = Utils.convertObjectToJson(slaDetails);

		} catch (Exception e) {

			LOGGER.error("error in getting sla detailsa details ", e);
		}
		return response;
	}
	
	/**
	 * 
	 * Get PacketDrop Details for IAS
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.ias.packetdrop.queue}") })
	public String getPacketDropDetailsIAS(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for sla with serviceVarien", request);
			if(request!=null) {
			response = iasProductService.getPacketDropDetailsForIASBasedOnSltVariant(request);
			}
		} catch (Exception e) {

			LOGGER.error("error in getting sla detailsa details ", e);
		}
		return response;
	}
}
