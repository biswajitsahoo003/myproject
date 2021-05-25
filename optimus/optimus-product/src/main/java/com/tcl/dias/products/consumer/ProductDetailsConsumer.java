package com.tcl.dias.products.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ProductInformationBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.service.v1.ProductsService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional
public class ProductDetailsConsumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GscProductSlaConsumer.class);
	
	@Autowired
	ProductsService productsService;
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.get.all}") })
	public String procesDestinationDetails(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.warn("Request: {}",request);
			List<ProductInformationBean> productInformationBeans = productsService.getAllProductDetails();
			LOGGER.warn("ProductInformationBeans {}, Request: {}",productInformationBeans,request);
			response = Utils.convertObjectToJson(productInformationBeans);
		} catch (Exception e) {
			LOGGER.warn("error in getting destination details ", e);
		}
		return response;
	}
	
	/**
	 * Queue to fetch product catalogue by id
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.get.by.id}") })
	public String getProductDetailsById(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Product Id recevied {} ",request);
			ProductInformationBean productInformationBeans = productsService.findProductDetailsById(Integer.valueOf(request));
			response = Utils.convertObjectToJson(productInformationBeans);
			LOGGER.info("getProductDetailsById response {} ",response);
		} catch (Exception e) {
			LOGGER.warn("error in getting getProductDetailsById  ", e);
		}
		return response;
	}
	

}
