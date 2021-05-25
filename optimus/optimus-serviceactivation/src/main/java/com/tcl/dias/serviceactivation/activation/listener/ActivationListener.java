package com.tcl.dias.serviceactivation.activation.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.tcl.dias.common.serviceinventory.bean.ScServiceDetailBean;
import com.tcl.dias.serviceactivation.service.ServiceActivationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.beans.ServiceDetailBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.activation.services.IPDetailsService;
import com.tcl.dias.serviceactivation.activation.services.ProductActivationConfigurationService;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the ActivationListener.java class.
 *
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class ActivationListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivationListener.class);

	@Autowired
	ProductActivationConfigurationService productActivationConfigurationService;

	@Autowired
	IPDetailsService ipDetailsService;

	@Autowired
	ServiceActivationService serviceActivationService;

	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${activation.ip.configuration}") })
	@Transactional
	public String processIPConfiguration(String responseBody) throws TclCommonException {

		try {
			Map<String, String> request = Utils.convertJsonToObject(responseBody,
					Map.class);
			LOGGER.info("Request received for IP Configuration is {}", request);
			return Utils.convertObjectToJson(productActivationConfigurationService.processIpConfigurationXml(request.get("SERVICE_ID"),
					request.get("ACTION_TYPE"), request.get("REQUEST_ID")));
		} catch (Exception e) {
			LOGGER.error("Error in process Ip Configuration ", e);
			return Utils.convertObjectToJson(new Response(false,e.getCause().getMessage(),e.getMessage(),null));
		}
	}

	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${activation.tx.configuration}") })
	@Transactional
	public String processTxConfiguration(String responseBody) throws TclCommonException {

		try {
			Map<String, String> request = Utils.convertJsonToObject(responseBody,
					Map.class);
			LOGGER.info("Request received for Tx Configuration is {}", request);
			return Utils.convertObjectToJson(productActivationConfigurationService.processTxConfigurationXml(request.get("SERVICE_ID"),
					request.get("ACTION_TYPE"), request.get("REQUEST_ID"),request.get("TX_TYPE")));
		} catch (Exception e) {
			LOGGER.error("Error in process Tx Configuration ", e);
			return Utils.convertObjectToJson(new Response(false,e.getCause().getMessage(),e.getMessage(),null));
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${activation.rf.configuration}") })
	@Transactional
	@SuppressWarnings("unchecked")
	public String processRfConfiguration(String responseBody) throws TclCommonException {

		try {
			Map<String, String> request = Utils.convertJsonToObject(responseBody,
					Map.class);
			LOGGER.info("Request received for Rf Configuration is {}", request);
			return Utils.convertObjectToJson(productActivationConfigurationService.processRfConfigurationXml(request.get("SERVICE_ID"),
					request.get("ACTION_TYPE"), request.get("REQUEST_ID")));
		} catch (Exception e) {
			LOGGER.error("Error in process Rf Configuration ", e);
			return Utils.convertObjectToJson(new Response(false,e.getCause().getMessage(),e.getMessage(),null));
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${activation.service.detail}") })
	@Transactional
	@SuppressWarnings("unchecked")
	public String getCurrentServiceDetail(String responseBody) throws TclCommonException {

		try {
			LOGGER.info("Request received for getCurrentServiceDetail is {}", responseBody);
			return Utils.convertObjectToJson(ipDetailsService.getCurrentServiceDetailsByUuid(responseBody));
		} catch (Exception e) {
			LOGGER.error("Error in getting current service details ", e);
			return Utils.convertObjectToJson(new Response(false,e.getCause().getMessage(),e.getMessage(),null));
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${activation.service.details}") })
	@Transactional
	@SuppressWarnings("unchecked")
	public String getCurrentServiceDetails(String responseBody) throws TclCommonException {
		List<ServiceDetailBean> serviceDetailBeans = new ArrayList<ServiceDetailBean>();
		try {
			LOGGER.info("Request received for getCurrentServiceDetail is {}", responseBody);
			List<String> request = Utils.convertJsonToObject(responseBody,
					List.class);
			
			request.forEach(serviceid->{
				try {
					serviceDetailBeans.add(ipDetailsService.getCurrentServiceDetailsByUuid(responseBody));
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
			});
			
			return Utils.convertObjectToJson(serviceDetailBeans);
		} catch (Exception e) {
			LOGGER.error("Error in getting current service details ", e);
			return Utils.convertObjectToJson(new Response(false,e.getCause().getMessage(),e.getMessage(),null));
		}
	}
	
	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${activation.dummy.ip.configuration}") })
	@Transactional
	public String processAssignDummyIPConfiguration(String responseBody) throws TclCommonException {

		try {
			Map<String, String> request = Utils.convertJsonToObject(responseBody,
					Map.class);
			LOGGER.info("Request received for processAssignDummyIPConfiguration is {}", request);
			return Utils.convertObjectToJson(productActivationConfigurationService.processDummyIpConfigurationXml(request.get("SERVICE_ID"),
					request.get("ACTION_TYPE"), request.get("REQUEST_ID")));
		} catch (Exception e) {
			LOGGER.error("Error in processAssignDummyIPConfiguration ", e);
			return Utils.convertObjectToJson(new Response(false,e.getCause().getMessage(),e.getMessage(),null));
		}
	}
	
	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${activation.dummy.rf.configuration}") })
	@Transactional
	public String processAssignDummyRfConfiguration(String responseBody) throws TclCommonException {

		try {
			Map<String, String> request = Utils.convertJsonToObject(responseBody,
					Map.class);
			LOGGER.info("Request received for processAssignDummyRfConfiguration is {}", request);
			return Utils.convertObjectToJson(productActivationConfigurationService.processDummyRfConfigurationXml(request.get("SERVICE_ID"),
					request.get("ACTION_TYPE"), request.get("REQUEST_ID")));
		} catch (Exception e) {
			LOGGER.error("Error in processAssignDummyRfConfiguration ", e);
			return Utils.convertObjectToJson(new Response(false,e.getCause().getMessage(),e.getMessage(),null));
		}
	}
	
	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${activation.release.ip.configuration}") })
	@Transactional
	public String processReleaseDummyIPConfiguration(String responseBody) throws TclCommonException {

		try {
			Map<String, String> request = Utils.convertJsonToObject(responseBody,
					Map.class);
			LOGGER.info("Request received for processReleaseDummyIPConfiguration is {}", request);
			return Utils.convertObjectToJson(productActivationConfigurationService.processDummyIpConfigurationXml(request.get("SERVICE_ID"),
					request.get("ACTION_TYPE"), request.get("REQUEST_ID")));
		} catch (Exception e) {
			LOGGER.error("Error in processReleaseDummyIPConfiguration ", e);
			return Utils.convertObjectToJson(new Response(false,e.getCause().getMessage(),e.getMessage(),null));
		}
	}
	
	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${activation.ip.terminate.configuration}") })
	@Transactional
	public String processIPTerminateConfiguration(String responseBody) throws TclCommonException {

		try {
			Map<String, String> request = Utils.convertJsonToObject(responseBody,
					Map.class);
			LOGGER.info("Request received for processIPTerminateConfiguration is {}", request);
			return Utils.convertObjectToJson(productActivationConfigurationService.processIpTerminateConfigurationXml(request.get("SERVICE_ID"),
					request.get("ACTION_TYPE"), request.get("REQUEST_ID")));
		} catch (Exception e) {
			LOGGER.error("Error in process processIPTerminateConfiguration ", e);
			return Utils.convertObjectToJson(new Response(false,e.getCause().getMessage(),e.getMessage(),null));
		}
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${activation.rf.terminate.configuration}") })
	@Transactional
	@SuppressWarnings("unchecked")
	public String processRfTerminateConfiguration(String responseBody) throws TclCommonException {

		try {
			Map<String, String> request = Utils.convertJsonToObject(responseBody,
					Map.class);
			LOGGER.info("Request received for processRfTerminateConfiguration is {}", request);
			return Utils.convertObjectToJson(productActivationConfigurationService.processRfConfigurationXml(request.get("SERVICE_ID"),
					request.get("ACTION_TYPE"), request.get("REQUEST_ID")));
		} catch (Exception e) {
			LOGGER.error("Error in process processRfTerminateConfiguration ", e);
			return Utils.convertObjectToJson(new Response(false,e.getCause().getMessage(),e.getMessage(),null));
		}
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED,readOnly=false)
	@RabbitListener(queuesToDeclare = { @Queue("${queue.rf.e2e.inventory}") })
	public void processRfE2EtoInventory(String responseBody) throws TclCommonException{
		try {
			LOGGER.info("processRfE2EtoInventory",responseBody);
			if((Objects.nonNull(responseBody)) && !org.springframework.util.StringUtils.isEmpty(responseBody)) {
				Map<String, String> request = Utils.convertJsonToObject(responseBody,
						Map.class);
				LOGGER.info("Request received for RF E2E to Inventory {}", request);
				 serviceActivationService.RFInventoryRefresh(request.get("SERVICE_ID"));
				LOGGER.info("RFInventoryRefresh for Process RF E2E Inventory: {}", request.get("SERVICE_ID"));
			}
		} catch (Exception e) {
			LOGGER.error("Error in Save to RF inventory Queue ", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${queue.persist.migrationData}") })
	@Transactional(readOnly = false)
	public void persistMigration(String request) {
		try {
			LOGGER.info("Request received for persistMigration : {}", request);
			Map<String, String> serviceAttributes =Utils.convertJsonToObject(request, Map.class); 
			if (serviceAttributes != null && !CollectionUtils.isEmpty(serviceAttributes)) {
				String serviceId = serviceAttributes.get("serviceId");
				Integer scServiceDetailsId = Integer.parseInt(serviceAttributes.get("scServiceDetailsId"));
				ipDetailsService.persistMigrationData(serviceId, scServiceDetailsId);
			}
		} catch (TclCommonException e) {
			LOGGER.error("Error in persistMigrationData ", e);
		}
	}
}
