package com.tcl.dias.customer.consumer;

import java.util.List;

import com.tcl.dias.customer.service.v1.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.ClassificationBean;
import com.tcl.dias.common.beans.LeSapCodeResponse;
import com.tcl.dias.common.beans.SapCodeRequest;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.customer.service.v1.CustomerService;
import com.tcl.dias.customer.service.v1.PartnerCustomerService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class contains listener method for sap code Related Details
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class SapCodeConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(SapCodeConsumer.class);
	
	@Autowired
	CustomerService customerService;

	@Autowired
	PartnerCustomerService partnerCustomerService;

	@Autowired
	SupplierService supplierService;
	
	
	/**
	 * processMailNotification- This method is used for mail notification listener
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.le.sap.queue}") })
	public String processSapCodeDetails(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			SapCodeRequest req = ((SapCodeRequest) Utils.convertJsonToObject(responseBody.getPayload(), SapCodeRequest.class));
			LeSapCodeResponse resp = customerService.getSapCodeBasedonCustLe(req.getCustomerLeIds(),req.getType());

			response = Utils.convertObjectToJson(resp);

		} catch (Exception e) {
			LOGGER.warn("error in getting customer le sap code :: {}", e);
		}
		return response;
	}

	/**
	 * processSapCodeByOrgAndCpnyCodes- This method is used get the concatenated value of Company code and Sap code.
	 * 
	 * @param List<String>
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.le.cpny.sap.queue}") })
	public String processSapCodeByOrgAndCpnyCodes(Message<String> responseBody) throws TclCommonException {
		try {
			SapCodeRequest req = ((SapCodeRequest) Utils.convertJsonToObject(responseBody.getPayload(), SapCodeRequest.class));
			return Utils.convertObjectToJson(customerService.getCpnyAndSapCodesBasedonCustLe(req.getCustomerLeIds(), req.getType()));
		} catch (Exception e) {
			LOGGER.warn("error in getting customer cpny and sap codes :: {}", e);
		}
		return "";
	}
	
	/**
	 * Get customer legal name based on sap code
	 *
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.partner.sap.code.name}")})
	public String getPartnerLeNameBySapCode(String responseBody) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Queue Listener Received for process : : {} ", responseBody);
			List<String> request = Utils.fromJson(responseBody, new TypeReference<List<String>>() {
			});
			LOGGER.info("Queue Request for Sap Code :: {}", request.toString());
			List<ClassificationBean> resp = partnerCustomerService.getPartnerLeNameBySapCode(request);
			LOGGER.info("Converted Request for Sap Code :: {}", resp.toString());
			response = Utils.convertObjectToJson(resp);

		} catch (Exception e) {
			LOGGER.warn("error in getting partner le sap name :: {}", e);
		}
		return response;
	}

	/**
	 * Get partner legal name based on billing account id
	 *
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.partner.billing.code.name}")})
	public String getPartnerLeNameByBillingAccountIds(String responseBody) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Queue Listener Received for process : : {} ", responseBody);
			List<String> request = Utils.fromJson(responseBody, new TypeReference<List<String>>() {
			});
			LOGGER.info("Queue Request for Billing Account :: {}", request.toString());
			List<ClassificationBean> resp = partnerCustomerService.getPartnerLeNameByBillingAccountIds(request);
			LOGGER.info("Converted Request for Billing Account :: {}", resp.toString());
			response = Utils.convertObjectToJson(resp);

		} catch (Exception e) {
			LOGGER.warn("error in getting partner le billing code name :: {}", e);
		}
		return response;
	}

	/**
	 * Get customer legal name based on sap code
	 *
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.customer.sap.code.name}")})
	public String getCustomerLeNameBySapCode(String responseBody) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Queue Listener Received for process : : {} ", responseBody);
			List<String> request = Utils.fromJson(responseBody, new TypeReference<List<String>>() {
			});
			LOGGER.info("Queue Request for Sap Code :: {}", request.toString());
			List<ClassificationBean> resp = customerService.getCustomerLeNameBySapCode(request);
			LOGGER.info("Converted Request for Sap Code :: {}", resp.toString());
			response = Utils.convertObjectToJson(resp);

		} catch (Exception e) {
			LOGGER.warn("error in getting customer le sap name :: {}", e);
		}
		return response;
	}

	/**
	 * Get customer legal name based on billing account id
	 *
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.customer.billing.code.name}")})
	public String getCustomerLeNameByBillingAccountIds(String responseBody) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Queue Listener Received for process : : {} ", responseBody);
			List<String> request = Utils.fromJson(responseBody, new TypeReference<List<String>>() {
			});
			LOGGER.info("Queue Request for Billing Account :: {}", request.toString());
			List<ClassificationBean> resp = customerService.getCustomerLeNameByBillingAccountIds(request);
			LOGGER.info("Converted Request for Billing Account :: {}", resp.toString());
			response = Utils.convertObjectToJson(resp);

		} catch (Exception e) {
			LOGGER.warn("error in getting customer le billing code name :: {}", e);
		}
		return response;
	}

	/**
	 * Get Supplier Le Sap Code
	 *
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.supplier.le.sap.queue}") })
	public String processSapCodeDetailsForSupplier(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			SapCodeRequest req = ((SapCodeRequest) Utils.convertJsonToObject(responseBody.getPayload(), SapCodeRequest.class));
			LeSapCodeResponse resp = supplierService.getSapCodeBasedOnSupplierLe(req.getCustomerLeIds(), req.getType(), req.getLeType());

			response = Utils.convertObjectToJson(resp);

		} catch (Exception e) {
			LOGGER.warn("error in getting suuplier le sap code :: {}", e);
		}
		return response;
	}
}
