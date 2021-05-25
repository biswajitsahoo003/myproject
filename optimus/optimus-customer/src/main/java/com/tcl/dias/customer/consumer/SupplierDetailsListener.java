package com.tcl.dias.customer.consumer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.ServiceProviderLegalBean;
import com.tcl.dias.common.beans.SupplierDetailRequestBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.customer.dto.ServiceProviderLegalEntityDto;
import com.tcl.dias.customer.service.v1.CustomerService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class SupplierDetailsListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SupplierDetailsListener.class);

	@Autowired
	private CustomerService customerService;

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.service.provider.detail}") })
	public String returnSupplierName(String request) throws TclCommonException {
		String serviceProviderName = "";
		try {
			if(StringUtils.isAllBlank(request)) {
				throw new TclCommonException("No request");
			}
			SupplierDetailRequestBean supplierDetailRequestBean = (SupplierDetailRequestBean) Utils.convertJsonToObject(request, SupplierDetailRequestBean.class);
			LOGGER.info("MDC Token in OMS before Rest call : {}",supplierDetailRequestBean.getMddFilterValue());
			serviceProviderName = customerService.getServiceProviderDetails(supplierDetailRequestBean.getSupplierId());
		} catch (Exception e) {
			LOGGER.error("error in returnSupplierName",e);
		}
		return serviceProviderName!=null?serviceProviderName:"";
	}

	/**
	 * Get Supplier related details
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.supplier.le.attr.product.queue}")})
	public String getSupplierDetails(String request) throws TclCommonException {
		String response = "";
		try {
			CustomerLeAttributeRequestBean customerLeAttributeRequestBean = (CustomerLeAttributeRequestBean) Utils
					.convertJsonToObject(request,
							CustomerLeAttributeRequestBean.class);
			CustomerLeDetailsBean customerLeDetailsBean = customerService.getSupplierLeAttributesBasedOnProduct(customerLeAttributeRequestBean);

			response = Utils.convertObjectToJson(customerLeDetailsBean);
		} catch (Exception e) {
			LOGGER.error("error in getting customer le details ", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.supplerle.attributes}") })
	public String processSupplierDetails(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			String req = responseBody.getPayload();
			String customerAttributeValue = customerService.getSpAttributesValue(Integer.valueOf(req.split(",")[0]),Integer.valueOf(req.split(",")[1]));
			response = Utils.convertObjectToJson(customerAttributeValue);

		} catch (Exception e) {

			LOGGER.error("error in getting processSupplierDetails details ", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.gsc.supplerle.attributes}") })
	public String processGscSupplierDetails(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			String req = responseBody.getPayload();
			String customerAttributeValue = customerService.getGscSpAttributesValue(Integer.valueOf(req.split(",")[0]),Integer.valueOf(req.split(",")[1]));
			response = Utils.convertObjectToJson(customerAttributeValue);

		} catch (Exception e) {
			LOGGER.error("error in getting processGscSupplierDetails details ", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.service.provider.izosdwan}") })
	public String returnSupplierNameIzosdwan(String request) throws TclCommonException {
		String response = "";
		try {
			if (StringUtils.isAllBlank(request)) {
				throw new TclCommonException("No request");
			}
			SupplierDetailRequestBean supplierDetailRequestBean = (SupplierDetailRequestBean) Utils
					.convertJsonToObject(request, SupplierDetailRequestBean.class);
			LOGGER.info("MDC Token in OMS before Rest call : {}", supplierDetailRequestBean.getMddFilterValue());
			ServiceProviderLegalBean serviceProviderLegalEntityDto = customerService
					.getServiceProviderDetailsSdwan(supplierDetailRequestBean.getSupplierId());
			if (serviceProviderLegalEntityDto != null) {
				response = Utils.convertObjectToJson(serviceProviderLegalEntityDto);
			}
		} catch (Exception e) {
			LOGGER.error("error in returnSupplierName", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.service.provider.izosdwan.country}") })
	public String getSpleCountry(String request) throws TclCommonException {
		String response = "";
		try {
			if (StringUtils.isAllBlank(request)) {
				throw new TclCommonException("No request");
			}
			Integer spLeId = Integer.valueOf(request);
			LOGGER.info("getSupplierId : {}",spLeId);
			ServiceProviderLegalBean serviceProviderLegalEntityDto = customerService
					.getSpleCountry(spLeId);
			if (serviceProviderLegalEntityDto != null) {
				response = Utils.convertObjectToJson(serviceProviderLegalEntityDto);
			}
		} catch (Exception e) {
			LOGGER.error("error in returnSupplierName", e);
		}
		return response;
	}
}
