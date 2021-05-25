package com.tcl.dias.products.consumer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.common.webex.beans.SkuDetailsRequestBean;
import com.tcl.dias.common.webex.beans.WebexProductPricesRequest;
import com.tcl.dias.products.constants.ExceptionConstants;
import com.tcl.dias.products.webex.service.WebexProductServiceMatrixService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Consumer class for Webex product related operations
 *
 * @author S.Syed Ali
 * @link http://www.tatacommunications.com/ product.countries.prices.queue
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class WebexProductDetailsConsumer {

	@Autowired
	WebexProductServiceMatrixService webexProductServiceMatrixService;

	private static final Logger LOGGER = LoggerFactory.getLogger(WebexProductDetailsConsumer.class);

	/**
	 * Queue for generating LNS/ITFS/Global outbound price list.
	 *
	 * @param request
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.webex.prices.queue}") })
	public String getProductPricesQueue(String request) {
		String response = null;
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			WebexProductPricesRequest webexProductPricesRequest = Utils.convertJsonToObject(request,
					WebexProductPricesRequest.class);
			response = Utils
					.convertObjectToJson(webexProductServiceMatrixService.getProductPrices(webexProductPricesRequest));
			LOGGER.info("Response {} :", response);
		} catch (Exception e) {
			LOGGER.error("Error in getting product details ", e);
		}
		return response;
	}

	/**
	 * Queue for getting SkuId and description.
	 *
	 * @param request
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.webex.sku.queue}") })
	public String getSku(String request) {
		String response = null;
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			SkuDetailsRequestBean skuDetailsRequestBean = Utils.convertJsonToObject(request,
					SkuDetailsRequestBean.class);
			response = Utils.convertObjectToJson(webexProductServiceMatrixService.getSkuDetails(skuDetailsRequestBean));
			LOGGER.info("Response {} :", response);
		} catch (Exception e) {
			LOGGER.error("Error in getting sku details ", e);
		}
		return response;
	}

	/**
	 * Queue for getting bridge country
	 *
	 * @param request
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.webex.bridge.queue}") })
	public String getBridgeCountries(String request) {
		String response = null;
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			response = webexProductServiceMatrixService.getBridgeCountries(request);
			LOGGER.info("Response {} :", response);
		} catch (Exception e) {
			LOGGER.error("Error in getting bridge country ", e);
		}
		return response;
	}

	/**
	 * Queue for finding whether the requested LNS country is in package or not
	 *
	 * @param request
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.product.webex.package.queue}")})
	public String getIsPackagedCountry(String request) {
		String response = null;
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			response = webexProductServiceMatrixService.getIsPackagedCountry(request);
			LOGGER.info("Response {} :", response);
		} catch (Exception e) {
			LOGGER.error("Error in getting bridge country ", e);
		}
		return response;
	}

	/**
	 * Queue to return HSN code for given endpoint
	 *
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.webex.endpoint.hsn.code}") })
	public String getHsnCodeForEndpoint(String request) {
		String response = null;
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			response = Utils.convertObjectToJson(webexProductServiceMatrixService.getHsnCodeForEndpoint(request));
			LOGGER.info("Response {} :", response);
		} catch (Exception e) {
			LOGGER.error("Error in getting bridge country ", e);
		}
		return response;
	}

	/**
	 * Get list of endpoints
	 * 
	 * @param request
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.webex.endpoint.list}") })
	public String getEndpointList(String request) {
		String response = null;
		try {
			response = Utils.convertObjectToJson(webexProductServiceMatrixService.getEndpointList());
			LOGGER.info("Response {} :", response);
		} catch (Exception e) {
			LOGGER.error("Error in getting bridge country ", e);
		}
		return response;
	}
}
