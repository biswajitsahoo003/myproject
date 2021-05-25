package com.tcl.dias.sfdc.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.sfdc.services.SfdcService;

/**
 * This file contains the ProductServices.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ProductServices {

	@Autowired
	private SfdcService sfdcService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServices.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.opportunity.productservices}") })
	public void saveProductServiceToSfdc(String responseBody) {
		try {
			LOGGER.info("Input payload for product create payload {}", responseBody);
			sfdcService.processSdfcProduct(responseBody);
		} catch (Exception e) {
			LOGGER.error("Error in createSiteOpportunity", e);
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.opportunity.response.updateproductservices}") })
	public void updateProductServiceToSfdc(String responseBody) {
		try {
			LOGGER.info("Input payload for product update payload {}", responseBody);
			sfdcService.processSdfcUpdateProduct(responseBody);
		} catch (Exception e) {
			LOGGER.error("Error in updateSiteOpportunity", e);
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.opportunity.response.deleteproductservices}") })
	public void deleteProductServiceToSfdc(String responseBody) {
		try {
			LOGGER.info("Input payload for product delete payload {}", responseBody);
			sfdcService.processSdfcDeleteProduct(responseBody);
		} catch (Exception e) {
			LOGGER.error("Error in deleteSiteOpportunity", e);
		}
	}

}
