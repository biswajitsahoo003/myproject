package com.tcl.dias.oms.ipc.listener;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.ipc.beans.CatalystVdomWrapperAPIResponse;
import com.tcl.dias.oms.ipc.beans.SecurityGroupCatalystBean;
import com.tcl.dias.oms.ipc.beans.SecurityGroupResponse;
import com.tcl.dias.oms.ipc.beans.pricebean.CrossBorderBean;
import com.tcl.dias.oms.ipc.service.v1.IPCOrderService;
import com.tcl.dias.oms.ipc.service.v1.IPCQuoteService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the OmsIPCListener.java class.
 * Process itsm,order,cross border requests
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class OmsIPCListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(OmsIPCListener.class);

	@Autowired
	IPCOrderService ipcOrderService;
	
	@Autowired
	IPCQuoteService ipcQuoteService;

	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.o2c.ipcfulfillment}") })
	public void processIPCOrderFulfillment(String responseBody) throws TclCommonException {
		try {
			LOGGER.info("Input Payload received for IPC Fulfillment");
			String fulfillmentData = responseBody;
			Map<String,String> orderDetail = (Map<String,String>) Utils.convertJsonToObject(fulfillmentData, Map.class);
			ipcOrderService.updateServiceIdAndPostITSM(orderDetail);
		} catch (Exception e) {
			LOGGER.error("error in processing IPC Fulfillment", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.o2c.ipcquote.crossbordertax}") })
	public String processIPCQuoteCrossBorderTax(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for IPC Cross Border Tax process: {}", request);
			Map<String, Object> input = (Map<String, Object>) Utils.convertJsonToObject(request, Map.class);
			Integer quoteLeId = (Integer) input.get("QUOTE_LE_ID");
			Integer customerLeId = (Integer) input.get("CUSTOMER_LE_ID");
			response = ipcQuoteService.processIPCQuoteCrossBorderTax(quoteLeId, customerLeId);
			LOGGER.info("processing IPC Quote CrossBorder Tax response: {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in processing IPC Quote Location", e);
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.o2c.ipc.order.stage.queue}") })
	public Boolean processIpcOrderStages(String request) throws TclCommonException {
		try {
			LOGGER.info("Input Payload received for IPC order stage process: {}", request);
			Map<String, String> req = (Map<String, String>) Utils.convertJsonToObject(request, Map.class);
			ipcOrderService.processIpcOrderStages(req);
			LOGGER.info("processed IPC order stage response");
			return true;
		} catch (Exception e) {
			LOGGER.error("Error in processing IPC Quote Location", e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.o2c.ipc.autoProvision}") })
	public String autoProvisionInCatalyst(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for IPC Auto Provision in Catalyst: {}", request);
			Map<String, String> requestMap = (Map<String, String>) Utils.convertJsonToObject(request, Map.class);
			ipcOrderService.autoProvisionInCatalyst(requestMap.get(CommonConstants.ORDERCODE),
					requestMap.get(CommonConstants.PROVISION_TYPE), Boolean.parseBoolean(
							requestMap.getOrDefault(CommonConstants.IS_ORDER_ALREADY_IMPLEMENTED, "false")));
			LOGGER.info("Process Completed for IPC Auto Provision");
			response = "SUCCESS";
		} catch (Exception e) {
			LOGGER.error("Error in processing IPC Auto Provision", e);
			response = "FAILURE";
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.o2c.ipc.catalyst.vdom}") })
	public String processCatalystVdomDetails(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for IPC Catalyst Vdom process: {}", request);
			Map<String, String> input = (Map<String, String>) Utils.convertJsonToObject(request, Map.class);
			String customer = (String) input.get("CUSTOMER");
			String location = (String) input.get("LOCATION");
			CatalystVdomWrapperAPIResponse catalyBean = ipcOrderService.fetchCatalystVdomDetails(customer, location);
			response = Utils.convertObjectToJson(catalyBean);
			LOGGER.info("processing IPC Catalyst Vdom details {}" , response);
		} catch (Exception e) {
			LOGGER.error("Error in processing IPC Catalyst Vdom details", e);
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.o2c.ipc.update.product.attributes}") })
	public String processProductAttributes(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for IPC Product Attribute Update : {}", request);
			List<Map<String, String>> input = (List<Map<String, String>>) Utils.convertJsonToObject(request, List.class);
			response = ipcOrderService.processProductAttributesInOms(input);
			LOGGER.info("processed Product Attribute Update {}" , response);
		} catch (Exception e) {
			response = "FAILURE";
			LOGGER.error("Error in processing IPC Product Attribute Update", e);
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.o2c.ipc.catalyst.security.group}") })
	public String processCatalystSecurityGroupDetails(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for IPC Catalyst securityGrp process: {}", request);
			Map<String, Object> input = (Map<String, Object>) Utils.convertJsonToObject(request, Map.class);
			String catalystBeanStr = Utils.convertObjectToJson(input.get("catalystBean"));
			SecurityGroupCatalystBean catalystBean = Utils.convertJsonToObject(catalystBeanStr, SecurityGroupCatalystBean.class);
			String vDomName = (String) input.get("vDomName");
			SecurityGroupResponse securityGrpResponse = ipcOrderService.fetchSecurityGroupCatalystDetails(catalystBean, vDomName);
			response = Utils.convertObjectToJson(securityGrpResponse);
			LOGGER.info("processing IPC Catalyst securityGrp details {}" , response);
		} catch (Exception e) {
			LOGGER.error("Error in processing IPC Catalyst securityGrp details", e);
		}
		return response;
	}
}
