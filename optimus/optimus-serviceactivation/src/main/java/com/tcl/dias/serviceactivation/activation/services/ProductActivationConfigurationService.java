package com.tcl.dias.serviceactivation.activation.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

import com.tcl.dias.serviceactivation.service.ServiceActivationService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.serviceactivation.activation.abs.base.AbstractActivationFactory;
import com.tcl.dias.serviceactivation.activation.core.ActivationConfigProducer;
import com.tcl.dias.serviceactivation.activation.factory.base.AbstractConfiguration;
import com.tcl.dias.serviceactivation.activation.utils.Product;
import com.tcl.dias.serviceactivation.activation.utils.RouterType;
import com.tcl.dias.serviceactivation.activemq.creator.MessageCreator;
import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;

/**
 * This class is used for creating the all product based netp configuration
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class ProductActivationConfigurationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductActivationConfigurationService.class);

	@Autowired
	ActivationConfigProducer activationConfigProducer;

	@Autowired
	ServiceDetailRepository serviceDetailRepository;

	@Value("${activemq.netp.create.queue}")
	String netpCreateQueue;

	@Autowired
	MessageCreator messageCreator;

	@Autowired
	NetworkInventoryRepository networkInventoryRepository;

	@Autowired
	ServiceActivationService serviceActivationService;

	/**
	 * 
	 * processIpConfigurationXml - This method is used to process the IP
	 * Configuration XML
	 *
	 * @param serviceId
	 * @return
	 */
	@Transactional
	public Response processIpConfigurationXml(String serviceId, String actionType, String requestId) {
//		Boolean status = true;
		Response response = new Response();
		try {
			LOGGER.info("Process Starting to generate the ip config xml with service Id {}", serviceId);
			ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceId,TaskStatusConstants.ISSUED);
			if (serviceDetail != null) {
				response = processIpServiceConfiguration(serviceDetail, actionType, requestId);
			} else {
//				status = false;
				LOGGER.warn("service detail is not found for service id {}", serviceId);
			}

		} catch (Exception e) {
			response.setStatus(false);
			response.setErrorMessage(e.getMessage());
			LOGGER.error("Exception in processing IP Configuration XML", e);
		}

		return response;
	}

	/**
	 * processServiceConfiguration - This method process the Service Configuration
	 * 
	 * @param serviceId
	 * @param status
	 * @param serviceDetail
	 * @return
	 */
	private Response processIpServiceConfiguration(ServiceDetail serviceDetail, String actionType, String requestId) {
//		Boolean status = true;
		Response response = new Response();
		RouterType routerType = RouterType.ALU;
		if (serviceDetail.getRouterMake() != null)
			routerType = (serviceDetail.getRouterMake().contains("ALCATEL")) ? RouterType.ALU
					: (serviceDetail.getRouterMake().contains("CISCO") ? RouterType.CISCO : RouterType.JUNIPER);
		Product prod = Enum.valueOf(Product.class, serviceDetail.getServiceOrderType());
		OrderDetail orderDetail=serviceDetail.getOrderDetail();
		String orderCategory=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderCategory(serviceDetail, orderDetail);
		String orderSubCategory=serviceDetail.getOrderSubCategory();
		if((Product.GVPN_MACD.equals(prod) 
				&& (Objects.nonNull(orderCategory) && "ADD_SITE".equals(orderCategory)))){
			prod= Enum.valueOf(Product.class, "GVPN");
		}
		if(Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("parallel")){
			LOGGER.info("Parallel");
			if(serviceDetail.getServiceOrderType().contains("ILL") || serviceDetail.getServiceOrderType().contains("IAS")){
				LOGGER.info("Parallel for ILL");
				prod= Enum.valueOf(Product.class, "ILL");
			}else if(serviceDetail.getServiceOrderType().contains("GVPN")){
				LOGGER.info("Parallel for GVPN");
				prod= Enum.valueOf(Product.class, "GVPN");
			}
		}
		AbstractActivationFactory productConfig = activationConfigProducer.getProductConfig(prod);
		if (productConfig != null) {
			response = processProductConfiguration(serviceDetail, routerType, prod, productConfig, "ip", actionType,
					requestId, null);
		} else {
//			status = false;
			LOGGER.warn("Product Config is not found for service id {} and product {}", serviceDetail.getServiceId(),
					prod);
		}
		return response;
	}

	private Response processTxServiceConfiguration(ServiceDetail serviceDetail, String actionType, String requestId,
			String txType) {
//		Boolean status = true;
		Response response = new Response();
		RouterType routerType = RouterType.ALU;
		if (serviceDetail.getRouterMake() != null)
			routerType = (serviceDetail.getRouterMake().contains("ALCATEL")) ? RouterType.ALU
					: (serviceDetail.getRouterMake().contains("CISCO") ? RouterType.CISCO : RouterType.JUNIPER);
		Product prod = Enum.valueOf(Product.class, serviceDetail.getServiceOrderType());
		OrderDetail orderDetail=serviceDetail.getOrderDetail();
		String orderCategory=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderCategory(serviceDetail, orderDetail);
		String orderSubCategory=serviceDetail.getOrderSubCategory();
		if(Product.GVPN_MACD.equals(prod) 
				&& (Objects.nonNull(orderCategory) && "ADD_SITE".equals(orderCategory))){
			prod= Enum.valueOf(Product.class, "GVPN");
		}
		if(Product.NPL_MACD.equals(prod)){
			prod= Enum.valueOf(Product.class, "ILL_MACD");
		}
		if(Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("parallel")){
			LOGGER.info("Parallel");
			if(serviceDetail.getServiceOrderType().contains("ILL") || serviceDetail.getServiceOrderType().contains("IAS")){
				LOGGER.info("Parallel for ILL");
				prod= Enum.valueOf(Product.class, "ILL");
			}else if(serviceDetail.getServiceOrderType().contains("GVPN")){
				LOGGER.info("Parallel for GVPN");
				prod= Enum.valueOf(Product.class, "GVPN");
			}else{
				LOGGER.info("Parallel for NPL");
				prod= Enum.valueOf(Product.class, "ILL");
			}
		}
		AbstractActivationFactory productConfig = activationConfigProducer.getProductConfig(prod);
		if (productConfig != null) {
			response = processProductConfiguration(serviceDetail, routerType, prod, productConfig, "tx", actionType,
					requestId, txType);
		} else {
//			status = false;
			LOGGER.warn("Product Config is not found for service id {} and product {}", serviceDetail.getServiceId(),
					prod);
		}
		return response;
	}

	private Response processRfServiceConfiguration(ServiceDetail serviceDetail, String actionType, String requestId) {
//		Boolean status = true;
		Response response = new Response();
		RouterType routerType = RouterType.ALU;
		if (serviceDetail.getRouterMake() != null)
			routerType = (serviceDetail.getRouterMake().contains("ALCATEL")) ? RouterType.ALU
					: (serviceDetail.getRouterMake().contains("CISCO") ? RouterType.CISCO : RouterType.JUNIPER);
		Product prod = Enum.valueOf(Product.class, serviceDetail.getServiceOrderType());
		OrderDetail orderDetail=serviceDetail.getOrderDetail();
		String orderCategory=serviceDetail.getOrderCategory();
		String orderSubCategory=serviceDetail.getOrderSubCategory();
		if(Product.GVPN_MACD.equals(prod) 
				&& (Objects.nonNull(orderCategory) && "ADD_SITE".equals(orderCategory))){
			prod= Enum.valueOf(Product.class, "GVPN");
		}
		if(Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("parallel")){
			LOGGER.info("Parallel");
			if(serviceDetail.getServiceOrderType().contains("ILL") || serviceDetail.getServiceOrderType().contains("IAS")){
				LOGGER.info("Parallel for ILL");
				prod= Enum.valueOf(Product.class, "ILL");
			}else if(serviceDetail.getServiceOrderType().contains("GVPN")){
				LOGGER.info("Parallel for GVPN");
				prod= Enum.valueOf(Product.class, "GVPN");
			}
		}
		AbstractActivationFactory productConfig = activationConfigProducer.getProductConfig(prod);
		if (productConfig != null) {
			response = processProductConfiguration(serviceDetail, routerType, prod, productConfig, "rf", actionType,
					requestId, null);
		} else {
//			status = false;
			LOGGER.warn("Product Config is not found for service id {} and product {}", serviceDetail.getServiceId(),
					prod);
		}
		return response;
	}

	/**
	 * 
	 * processIpConfigurationXml - This method is used to process the IP
	 * Configuration XML
	 *
	 * @param serviceId
	 * @return
	 */
	public Response processTxConfigurationXml(String serviceId, String actionType, String requestId, String txType) {
//		Boolean status = true;
		Response response = new Response();
		try {
			LOGGER.info("Process Starting to generate the tx config xml with service Id {}", serviceId);
			ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceId,TaskStatusConstants.ISSUED);
			if (serviceDetail != null) {
				response = processTxServiceConfiguration(serviceDetail, actionType, requestId, txType);
			} else {
//				status = false;
				LOGGER.warn("service detail is not found for service id {}", serviceId);
			}

		} catch (Exception e) {
			response.setStatus(false);
			response.setErrorMessage(e.getMessage());
			LOGGER.error("Exception in processing TX Configuration XML", e);
		}

		return response;
	}

	public Response processRfConfigurationXml(String serviceId, String actionType, String requestId) {
//		Boolean status = true;
		Response response = new Response();
		try {
			LOGGER.info("Process Starting to generate the rf config xml with service Id {}", serviceId);
			ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceId,TaskStatusConstants.ISSUED);
			if (serviceDetail != null) {
				response = processRfServiceConfiguration(serviceDetail, actionType, requestId);
				LOGGER.info("processRfServiceConfiguration response from NetP {}", response);
			} else {
//				status = false;
				LOGGER.warn("service detail is not found for service id {}", serviceId);
			}

		} catch (Exception e) {
//			status = false;
			response.setStatus(false);
			response.setErrorMessage(e.getMessage());
			LOGGER.error("Exception in processing RF Configuration XML", e);
		}

		return response;
	}

	/**
	 * processProductConfiguration - This method process the Product Configuration
	 * 
	 * @param serviceId
	 * @param status
	 * @param serviceDetail
	 * @param routerType
	 * @param prod
	 * @param productConfig
	 * @return
	 */
	private Response processProductConfiguration(ServiceDetail serviceDetail, RouterType routerType, Product prod,
			AbstractActivationFactory productConfig, String type, String actionType, String requestId, String txType) {
		Boolean status = true;
		Response response = new Response();
		AbstractConfiguration routerConfig = productConfig.getActivationConfigFactory(routerType);
		if (routerConfig != null) {
			response = processRouterConfig(serviceDetail, routerConfig, type, actionType, requestId, txType);
			LOGGER.info("processRouterConfig response from NetP {}", response);
		} else {
			status = false;
			LOGGER.warn("Router Config is not found for service id {} and product {} and router {}",
					serviceDetail.getServiceId(), prod, routerType);
		}
		return response;
	}

	/**
	 * processRouterConfig - This method process the Router Config
	 * 
	 * @param actionType
	 * @param serviceDetail
	 * @param routerConfig
	 * @return Response
	 */
	private Response processRouterConfig(ServiceDetail serviceDetail, AbstractConfiguration routerConfig, String type,
			String actionType, String requestId, String txType) {
		Response response = new Response();
		String flowName = "";
		if (requestId.contains("_") || requestId.contains("#")) {

			if (requestId.contains("#")) {
				String flow[] = requestId.split("#");
				flowName = flow[2].substring(0, flow[2].lastIndexOf("_"));
			} 
		}
		if (type.equals("tx")) {
			String txConfigXml = routerConfig.generateTxConfigXml(serviceDetail, actionType, requestId, txType);

			LOGGER.info("Generated XML which is to be passed to the Netp {}", txConfigXml);
			response = messageCreator.convertAndSend(txConfigXml, netpCreateQueue);
			LOGGER.info("XML is passed to Netp with status {}", response);
				persistNetworkInv(serviceDetail, requestId, flowName, txConfigXml);
		} else if (type.equals("rf")) {
			String rfConfigXml = routerConfig.generateRFConfigXml(serviceDetail, actionType, requestId);
			LOGGER.info("Generated XML which is to be passed to the Netp {}", rfConfigXml);
			response = messageCreator.convertAndSend(rfConfigXml, netpCreateQueue);
			LOGGER.info("XML is passed to Netp with status {}", response);
				persistNetworkInv(serviceDetail, requestId, flowName, rfConfigXml);
		} else if (type.equals("ip")) {

			String ipConfigXml = routerConfig.generateIPConfigXml(serviceDetail, actionType, requestId);
			LOGGER.info("Generated XML which is to be passed to the Netp {}", ipConfigXml);
			response = messageCreator.convertAndSend(ipConfigXml, netpCreateQueue);
			LOGGER.info("Generated XML response which is to be passed to the Netp {}", ipConfigXml);
			response.setData("false");
			if(Objects.nonNull(serviceDetail.getLastmileType()) && (serviceDetail.getLastmileType().toLowerCase().contains("wireless")
					|| serviceDetail.getLastmileType().toLowerCase().contains("rf"))){
				LOGGER.info("Set for RF");
				response.setData("true");
			}
			LOGGER.info("XML is passed to Netp with status {}", response);
			persistNetworkInv(serviceDetail, requestId, flowName, ipConfigXml);
		}
		return response;
	}

	/**
	 * persistNetworkInv
	 * 
	 * @param serviceDetail
	 * @param processId
	 * @param flowName
	 * @param ipConfigXml
	 */
	private void persistNetworkInv(ServiceDetail serviceDetail, String requestId, String flowName, String ipConfigXml) {
		NetworkInventory ackNetworkInv = new NetworkInventory();
		ackNetworkInv.setCreatedDate(new Timestamp((new Date()).getTime()));
		ackNetworkInv.setRequest(ipConfigXml);
		ackNetworkInv.setRequestId(requestId);
		ackNetworkInv.setServiceCode(serviceDetail.getServiceId());
		ackNetworkInv.setType("ACK_" + flowName);
		networkInventoryRepository.save(ackNetworkInv);

		NetworkInventory subNetworkInv = new NetworkInventory();
		subNetworkInv.setCreatedDate(new Timestamp((new Date()).getTime()));
		subNetworkInv.setRequest(ipConfigXml);
		subNetworkInv.setRequestId(requestId);
		subNetworkInv.setServiceCode(serviceDetail.getServiceId());
		subNetworkInv.setType("SUB_" + flowName);
		networkInventoryRepository.save(subNetworkInv);
	}

	public Object processDummyIpConfigurationXml(String serviceId, String actionType, String requestId) {
		Response response = new Response();
		try {
			LOGGER.info("processDummyIpConfigurationXml:Process Starting to generate the ip config xml with service Id {}", serviceId);
			ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceId,TaskStatusConstants.ISSUED);
			if (serviceDetail != null) {
				response = processDummyIpServiceConfiguration(serviceDetail, actionType, requestId);
			} else {
				LOGGER.warn("service detail is not found for service id {}", serviceId);
			}
		} catch (Exception e) {
			response.setStatus(false);
			response.setErrorMessage(e.getMessage());
			LOGGER.error("Exception in processing IP Configuration XML", e);
		}

		return response;
	}
	
	private Response processDummyIpServiceConfiguration(ServiceDetail serviceDetail, String actionType, String requestId) {
		LOGGER.info("processDummyIpServiceConfiguration invoked");
		Response response = new Response();
		RouterType routerType = RouterType.ALU;
		if (serviceDetail.getRouterMake() != null)
			routerType = (serviceDetail.getRouterMake().contains("ALCATEL")) ? RouterType.ALU
					: (serviceDetail.getRouterMake().contains("CISCO") ? RouterType.CISCO : RouterType.JUNIPER);
		Product prod = Enum.valueOf(Product.class, serviceDetail.getServiceOrderType());
		OrderDetail orderDetail=serviceDetail.getOrderDetail();
		String orderCategory=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderCategory(serviceDetail, orderDetail);
		String orderSubCategory=serviceDetail.getOrderSubCategory();
		orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
		orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
		if(Product.GVPN_MACD.equals(prod) 
				&& (Objects.nonNull(orderCategory) && "ADD_SITE".equals(orderCategory))){
			prod= Enum.valueOf(Product.class, "GVPN");
		}
		AbstractActivationFactory productConfig = activationConfigProducer.getProductConfig(prod);
		if (productConfig != null) {
			response = processDummyProductConfiguration(serviceDetail, routerType, prod, productConfig, "ip", actionType,
					requestId, null);
		} else {
			LOGGER.warn("Product Config is not found for service id {} and product {}", serviceDetail.getServiceId(),
					prod);
		}
		return response;
	}
	
	private Response processDummyProductConfiguration(ServiceDetail serviceDetail, RouterType routerType, Product prod,
			AbstractActivationFactory productConfig, String type, String actionType, String requestId, String txType) {
		LOGGER.info("processDummyProductConfiguration invoked");
		Boolean status = true;
		Response response = new Response();
		AbstractConfiguration routerConfig = productConfig.getActivationConfigFactory(routerType);
		if (routerConfig != null) {
			response = processRouterDummyConfig(serviceDetail, routerConfig, type, actionType, requestId, txType);
		} else {
			status = false;
			LOGGER.warn("Router Config is not found for service id {} and product {} and router {}",
					serviceDetail.getServiceId(), prod, routerType);
		}
		return response;
	}

	private Response processRouterDummyConfig(ServiceDetail serviceDetail, AbstractConfiguration routerConfig, String type,
			String actionType, String requestId, String txType) {
		LOGGER.info("processRouterDummyConfig invoked");
		Response response = new Response();
		String flowName = "";
		if (requestId.contains("_") || requestId.contains("#")) {

			if (requestId.contains("#")) {
				String flow[] = requestId.split("#");
				flowName = flow[2].substring(0, flow[2].lastIndexOf("_"));
			} 
		}
		if (type.equals("ip")) {
			String ipConfigXml = routerConfig.generateDummyIPConfigXml(serviceDetail, actionType, requestId);
			LOGGER.info("Generated XML which is to be passed to the Netp {}", ipConfigXml);
			response = messageCreator.convertAndSend(ipConfigXml, netpCreateQueue);
			LOGGER.info("XML is passed to Netp with status ", response);
			persistNetworkInv(serviceDetail, requestId, flowName, ipConfigXml);
		}else if (type.equals("rf")) {
			String rfConfigXml = routerConfig.generateDummyRFConfigXml(serviceDetail, actionType, requestId);
			LOGGER.info("Generated XML which is to be passed to the Netp {}", rfConfigXml);
			response = messageCreator.convertAndSend(rfConfigXml, netpCreateQueue);
			LOGGER.info("XML is passed to Netp with status {}", response);
			persistNetworkInv(serviceDetail, requestId, flowName, rfConfigXml);
		}
		return response;
	}
	
	@Transactional
	public Response processIpTerminateConfigurationXml(String serviceId, String actionType, String requestId) {
		Response response = new Response();
		try {
			LOGGER.info("Process Starting to generate the ip terminate config xml with service Id {}", serviceId);
			ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceId,TaskStatusConstants.ACTIVE);
			if (serviceDetail != null) {
				response = processIpTerminateServiceConfiguration(serviceDetail, actionType, requestId);
			} else {
				LOGGER.warn("service detail is not found for service id {}", serviceId);
				response.setStatus(false);
				response.setErrorMessage("ACTIVE Service Detail Record not exists");
			}

		} catch (Exception e) {
			response.setStatus(false);
			response.setErrorMessage(e.toString());
			LOGGER.error("Exception in processIpTerminateConfigurationXml", e);
		}

		return response;
	}

	private Response processIpTerminateServiceConfiguration(ServiceDetail serviceDetail, String actionType,
			String requestId) {
		LOGGER.info("processIpTerminateServiceConfiguration invoked");
		Response response = new Response();
		RouterType routerType = RouterType.ALU;
		if (serviceDetail.getRouterMake() != null){
			routerType = (serviceDetail.getRouterMake().contains("ALCATEL")) ? RouterType.ALU
					: (serviceDetail.getRouterMake().contains("CISCO") ? RouterType.CISCO : RouterType.JUNIPER);
		}
		Product prod = Enum.valueOf(Product.class, serviceDetail.getServiceOrderType());
		if(serviceDetail.getServiceOrderType().contains("ILL") || serviceDetail.getServiceOrderType().contains("IAS")){
			prod= Enum.valueOf(Product.class, "ILL");
		}else if(serviceDetail.getServiceOrderType().contains("GVPN")){
			prod= Enum.valueOf(Product.class, "GVPN");
		}
		AbstractActivationFactory productConfig = activationConfigProducer.getProductConfig(prod);
		if (productConfig != null) {
			response = processProductConfiguration(serviceDetail, routerType, prod, productConfig, "ip", actionType,
					requestId, null);
		} else {
			LOGGER.warn("Product Config is not found for processIpTerminateServiceConfiguration service id {} and product {}", serviceDetail.getServiceId(),
					prod);
		}
		return response;
	}


	public Response processRfTerminateConfigurationXml(String serviceId, String actionType, String requestId) {
		Response response = new Response();
		try {
			LOGGER.info("Process Starting to generate the processRfTerminateConfigurationXml with service Id {}", serviceId);
			ServiceDetail serviceDetail = serviceDetailRepository
					.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceId, TaskStatusConstants.ACTIVE);
			if (serviceDetail != null) {
				response = processRfTerminateServiceConfiguration(serviceDetail, actionType, requestId);
			} else {
				LOGGER.warn("service detail is not found for service id {}", serviceId);
			}

		} catch (Exception e) {
			response.setStatus(false);
			response.setErrorMessage(e.getMessage());
			LOGGER.error("Exception in processing processRfTerminateConfigurationXml XML", e);
		}
		return response;
	}

	private Response processRfTerminateServiceConfiguration(ServiceDetail serviceDetail, String actionType,
			String requestId) {
		Response response = new Response();
		RouterType routerType = RouterType.ALU;
		if (serviceDetail.getRouterMake() != null)
			routerType = (serviceDetail.getRouterMake().contains("ALCATEL")) ? RouterType.ALU
					: (serviceDetail.getRouterMake().contains("CISCO") ? RouterType.CISCO : RouterType.JUNIPER);
		Product prod = Enum.valueOf(Product.class, serviceDetail.getServiceOrderType());
		if(serviceDetail.getServiceOrderType().contains("ILL") || serviceDetail.getServiceOrderType().contains("IAS")){
			prod= Enum.valueOf(Product.class, "ILL");
		}else if(serviceDetail.getServiceOrderType().contains("GVPN")){
			prod= Enum.valueOf(Product.class, "GVPN");
		}
		AbstractActivationFactory productConfig = activationConfigProducer.getProductConfig(prod);
		if (productConfig != null) {
			response = processProductConfiguration(serviceDetail, routerType, prod, productConfig, "rf", actionType,
					requestId, null);
		} else {
			LOGGER.warn("Product Config is not found for processRfTerminateServiceConfiguration service id {} and product {}", serviceDetail.getServiceId(),
					prod);
		}
		return response;
	}
	
	public Response processDummyRfConfigurationXml(String serviceId, String actionType, String requestId) {
		LOGGER.info("processDummyRfConfigurationXml method invoked");
		Response response = new Response();
		try {
			LOGGER.info("Process Starting to generate the dummy rf config xml with service Id {}", serviceId);
			ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceId,TaskStatusConstants.ISSUED);
			if (serviceDetail != null) {
				response = processDummyRfServiceConfiguration(serviceDetail, actionType, requestId);
				LOGGER.info("processRfServiceConfiguration response from NetP {}", response);
			} else {
				LOGGER.warn("service detail is not found for service id {}", serviceId);
			}

		} catch (Exception e) {
			response.setStatus(false);
			response.setErrorMessage(e.getMessage());
			LOGGER.error("Exception in processing Dummy RF Configuration XML", e);
		}

		return response;
	}
	
	private Response processDummyRfServiceConfiguration(ServiceDetail serviceDetail, String actionType, String requestId) {
		LOGGER.info("processDummyRfServiceConfiguration method invoked");
		Response response = new Response();
		RouterType routerType = RouterType.ALU;
		if (serviceDetail.getRouterMake() != null)
			routerType = (serviceDetail.getRouterMake().contains("ALCATEL")) ? RouterType.ALU
					: (serviceDetail.getRouterMake().contains("CISCO") ? RouterType.CISCO : RouterType.JUNIPER);
		Product prod = Enum.valueOf(Product.class, serviceDetail.getServiceOrderType());
		OrderDetail orderDetail=serviceDetail.getOrderDetail();
		String orderCategory=serviceDetail.getOrderCategory();
		String orderSubCategory=serviceDetail.getOrderSubCategory();
		if(Product.GVPN_MACD.equals(prod) 
				&& (Objects.nonNull(orderCategory) && "ADD_SITE".equals(orderCategory))){
			prod= Enum.valueOf(Product.class, "GVPN");
		}
		if(Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("parallel")){
			LOGGER.info("Parallel");
			if(serviceDetail.getServiceOrderType().contains("ILL") || serviceDetail.getServiceOrderType().contains("IAS")){
				LOGGER.info("Parallel for ILL");
				prod= Enum.valueOf(Product.class, "ILL");
			}else if(serviceDetail.getServiceOrderType().contains("GVPN")){
				LOGGER.info("Parallel for GVPN");
				prod= Enum.valueOf(Product.class, "GVPN");
			}
		}
		AbstractActivationFactory productConfig = activationConfigProducer.getProductConfig(prod);
		if (productConfig != null) {
			response = processDummyProductConfiguration(serviceDetail, routerType, prod, productConfig, "rf", actionType,
					requestId, null);
		} else {
			LOGGER.warn("Product Config is not found for service id {} and product {}", serviceDetail.getServiceId(),
					prod);
		}
		return response;
	}
}
