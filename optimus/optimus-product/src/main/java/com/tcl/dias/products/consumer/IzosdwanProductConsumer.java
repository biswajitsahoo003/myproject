package com.tcl.dias.products.consumer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.CgwServiceAreaMatricBean;
import com.tcl.dias.common.beans.ProductLocationBean;
import com.tcl.dias.common.beans.VendorProfileDetailsBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.productcatelog.entity.entities.Location;
import com.tcl.dias.products.izosdwan.service.v1.IzosdwanProductService;
import com.tcl.dias.products.service.v1.ProductsService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the IzosdwanProductConsumer.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class IzosdwanProductConsumer {
	@Autowired
	IzosdwanProductService izoSdwanProductService;
	
	@Autowired
	ProductsService productFamilyService;

	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanProfileDetailsConsumer.class);
	
	/**
	 * 
	 * Get Service Area Matrix for CGW by city
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.cgw.sam.city}") })
	public String getServiceAreaMatrixForCGWByCity(String request) {
		String response = "";
		try {
			LOGGER.info("Data sent to  getServiceAreaMatrixForCGWByCity queue call is {}", request);
			CgwServiceAreaMatricBean cgwServiceAreaMatricBean = izoSdwanProductService
					.getServiceAreaMatrixByLocationForCgw(request);
			LOGGER.info("response of the getServiceAreaMatrixForCGWByCity queue call is {}", cgwServiceAreaMatricBean);
			response = Utils.convertObjectToJson(cgwServiceAreaMatricBean);
		} catch (TclCommonException e) {
			// TODO Auto-generated catch block
			LOGGER.info("Error in the profile fetching queue call{}", e);
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 
	 * Get Service Area Matrix for CGW
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.cgw.sam}") })
	public String getServiceAreaMatrixForCGW(String request) {
		String response = "";
		try {
			List<CgwServiceAreaMatricBean> cgwServiceAreaMatricBeans = izoSdwanProductService
					.getServiceAreaMatrixForCgw();
			LOGGER.info("response of the getServiceAreaMatrixForCGW queue call is {}", cgwServiceAreaMatricBeans);
			response = Utils.convertObjectToJson(cgwServiceAreaMatricBeans);
		} catch (TclCommonException e) {
			// TODO Auto-generated catch block
			LOGGER.info("Error in the profile fetching queue call{}", e);
			e.printStackTrace();
		}

		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.location.county}") })
	public String getLocationByCounty(String request) {
		String response = "";
		try {
			ProductLocationBean productLocationBean = izoSdwanProductService.getLocationByCountryName(request);
			LOGGER.info("response of the getLocationByCounty queue call is {}", productLocationBean);
			response = Utils.convertObjectToJson(productLocationBean);
		} catch (TclCommonException e) {
			// TODO Auto-generated catch block
			LOGGER.info("Error in the profile fetching queue call{}", e);
			e.printStackTrace();
		}

		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.country}") })
	public String getCountryByProductName(String request) {
		String response = "";
		try {
			List<String> country = productFamilyService.getProductLocations(request);
			LOGGER.info("response of the getCountryByProductName queue call is {}", country);
			response = Utils.convertObjectToJson(country);
		} catch (TclCommonException e) {
			// TODO Auto-generated catch block
			LOGGER.info("Error in the profile fetching queue call{}", e);
			e.printStackTrace();
		}

		return response;
	}
}
