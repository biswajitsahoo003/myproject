package com.tcl.dias.products.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.DataCenterBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.service.v1.ProductsService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
/**
 * This file contains the ProductDataCenterConsumer.java class.
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class ProductDataCenterConsumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductDataCenterConsumer.class);
	
	@Autowired
	ProductsService productsService;
	/**
	 * getDataCenter- This method is used to get data center
	 * 
	 * @param request
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.dc.queue}") })
	public String getDataCenters(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for getting data center of a product",request);
			String[] dataCenterIds = (request).split(",");

			List<DataCenterBean> dataCenters = productsService.getDataCenter(dataCenterIds);
			response = Utils.convertObjectToJson(dataCenters);

		} catch (Exception e) {

			LOGGER.error("error in getting sla detailsa details ", e);
		}
		return response;
	}
}
