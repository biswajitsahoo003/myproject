package com.tcl.dias.serviceinventory.listener;

import static com.tcl.dias.serviceinventory.util.ServiceInventoryUtils.fromJson;
import static com.tcl.dias.serviceinventory.util.ServiceInventoryUtils.toJson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ServiceDetailedInfoBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderBean;
import com.tcl.dias.common.serviceinventory.beans.OrderSummaryBeanResponse;
import com.tcl.dias.common.serviceinventory.beans.SICustomerInfoBean;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderRequest;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderResponse;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIPriceRevisionDetailBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceAssetComponentBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoGVPNBean;
import com.tcl.dias.common.serviceinventory.beans.SiServiceSiContractInfoBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.common.webex.beans.SIExistingGVPNBean;
import com.tcl.dias.common.webex.beans.SIInfoSearchBean;
import com.tcl.dias.serviceinventory.beans.IpcProductSolutionBean;
import com.tcl.dias.serviceinventory.beans.SIDetailedInfoResponse;
import com.tcl.dias.serviceinventory.renewals.RenevalsValidateBean;
import com.tcl.dias.serviceinventory.renewals.RenewalsQuoteDetail;
import com.tcl.dias.serviceinventory.service.v1.IpcServiceInventoryService;
import com.tcl.dias.serviceinventory.service.v1.RenewalsServiceInventoryService;
import com.tcl.dias.serviceinventory.service.v1.ServiceInventoryService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.vavr.control.Try;

/**
 * Listener class for Service Inventory
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ServiceInventoryListener {

	public static final Logger LOGGER = LoggerFactory.getLogger(ServiceInventoryListener.class);

	@Autowired
	ServiceInventoryService inventoryService;
	
	@Autowired
	IpcServiceInventoryService ipcInventoryService;
	
	@Autowired
	RenewalsServiceInventoryService renewalsServiceInventoryService;

	/**
	 * Queue Listener
	 *
	 * @param request
	 * @return String
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.order.get.queue}") })
	public String fetchSIOrderData(String request) {
		return Try.success(request)
				.map(s -> {
					SIGetOrderRequest getOrderRequest = fromJson(request, SIGetOrderRequest.class);
					return getOrderRequest;
				})
				.flatMap(inventoryService::getOrderData)
				.toEither()
				.fold(error -> {
					LOGGER.error("Error occurred while getting SI order data", error);
					SIGetOrderResponse response = new SIGetOrderResponse();
					response.setStatus(CommonConstants.ERROR);
					response.setMessage(error.getMessage());
					return toJson(response);
				}, siOrderDataBean -> {
					SIGetOrderResponse response = new SIGetOrderResponse();
					response.setStatus(CommonConstants.SUCCESS);
					response.setOrder(siOrderDataBean);
					return toJson(response);
				});
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.order.wholesale.get.queue}") })
	public String fetchSIOrderWholsaleData(String request) {
		return Try.success(request)
				.map(s -> {
					SIGetOrderRequest getOrderRequest = fromJson(request, SIGetOrderRequest.class);
					return getOrderRequest;
				})
				.flatMap(inventoryService::getOrderWholesaleData)
				.toEither()
				.fold(error -> {
					LOGGER.error("Error occurred while getting SI order data", error);
					SIGetOrderResponse response = new SIGetOrderResponse();
					response.setStatus(CommonConstants.ERROR);
					response.setMessage(error.getMessage());
					return toJson(response);
				}, siOrderDataBean -> {
					SIGetOrderResponse response = new SIGetOrderResponse();
					response.setStatus(CommonConstants.SUCCESS);
					response.setOrder(siOrderDataBean);
					return toJson(response);
				});
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.orderIdInRespecToServiceId.queue}") })
	public String getOrderIdFromServiceId(Message<String> responseBody) throws TclCommonException {
		String response = null;
		try {
			String tpsServiceId = responseBody.getPayload();
			LOGGER.info("service Id {}", tpsServiceId);
			Integer orderId = inventoryService.getOrderIdFromServiceId(tpsServiceId);
			if (orderId != null)
				response = Utils.convertObjectToJson(orderId);
		} catch (Exception e) {
			LOGGER.error("Error in fetching Order Id From Service Id",e);
		}

		return response;
	}
	

	/**
	 * Queue Listener
	 *
	 * @param request
	 * @return String
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.ias.queue}") })
	public String fetchSIOrderDataForIAS(Message<String> request) {
		String response = "";
		try {
			String serviceId = request.getPayload();

			SIDetailedInfoResponse responseBean = inventoryService.getDeatiledSIInfoForILLService(serviceId);

			response = Utils.convertObjectToJson(responseBean);

		} catch (Exception e) {

			LOGGER.error("error in getting stae info ", e);
		}

		return response;

	}
	
	/**
	 * Queue Listener
	 *
	 * @param request
	 * @return String
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.iwan.queue}") })
	public String fetchSIOrderDataForIWAN(Message<String> request) {
		String response = "";
		try {
			String serviceId = request.getPayload();

			SIDetailedInfoResponse responseBean = inventoryService.getDeatiledSIInfoForIWANService(serviceId);

			response = Utils.convertObjectToJson(responseBean);

		} catch (Exception e) {

			LOGGER.error("error in getting stae info ", e);
		}

		return response;

	}
	
	/**
	 * Queue Listener
	 *
	 * @param request
	 * @return String
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.gvpn.queue}") })
	public String fetchSIOrderDataForGVPN(Message<String> request) {
		String response = "";
		try {
			String serviceId = request.getPayload();

			SIDetailedInfoResponse responseBean = inventoryService.getDeatiledSIInfoForGvpnService(serviceId);

			response = Utils.convertObjectToJson(responseBean);

		} catch (Exception e) {

			LOGGER.error("error in getting stae info ", e);
		}

		return response;

	}
	
	
	/**
	 * Queue Listener
	 *
	 * @param request
	 * @return String
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.related.details.queue}") })
	public String fetchSIDataforAllRelatedServices(Message<String> request) {
		String response = "";
		try {
			String serviceId = request.getPayload();

			List<SIServiceInfoBean> responseBean = inventoryService.getServiceDetailsPrimarySecondary(serviceId);

			response = Utils.convertObjectToJson(responseBean);
			

		} catch (Exception e) {

			LOGGER.error("error in getting stae info ", e);
		}

		LOGGER.info(" rabbitmq.si.related.details.queue response {}",response);
		return response;

	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.ipc.si.related.details.queue}") })
	public String getIPCSIAssetDetails(Message<String> request) {
		String response = "";
		try {
			String serviceId = request.getPayload();
			List<SIServiceAssetComponentBean> responseBean = inventoryService.getIPCSIAssetDetails(serviceId);
			response = Utils.convertObjectToJson(responseBean);
			LOGGER.info("Response::"+response);
		} catch (Exception e) {
			LOGGER.error("Error in getting si ipc asset info ", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.ipc.si.solutions.queue}") })
	public String getIpcExistingSolutionDetails(Message<String> request) {
		String response = "";
		try {
			String serviceId = request.getPayload();
			List<IpcProductSolutionBean> responseBean = ipcInventoryService.getIpcExistingSolutionDetails(serviceId);
			response = Utils.convertObjectToJson(responseBean);
		} catch (Exception e) {
			LOGGER.error("Error in getIpcExistingSolutionDetails: ", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.si.order.get.izopc.queue}")})
    public Map<String,String> getLRDetailsForIzopc(String request) {
        try {
           
            return inventoryService.getLRDetailsForIzoBasedOnServiceId(request);
        } catch (Exception e) {
            LOGGER.error("error in Service inventory details for izopc", e);
        }
        return new HashMap<>();
    }
	/**
	 * Queue Listener for customer cuio for given partner id
	 *
	 * @param request
	 * @return String
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.get.cutomer.cuid.by.partner}")})
	public String getCustomerCUIDByPartnerId (String request) {
		String response = "";
		try {
			response = Utils.convertObjectToJson(inventoryService.getCustomerCUIDByPartnerId(Integer.valueOf(request)));
            LOGGER.info("Queue Reponse getCustomerCUIDByPartnerId "+response);
		} catch (Exception e) {
			LOGGER.error("error in Service inventory details for getCustomerCUIDByPartnerId", e);
		}
		return response;
	}

	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.ipc.si.details.queue}") })
	public void processServiceInventory(String requestBody) {
		try {
			LOGGER.info("Input Payload received for processServiceInventory received");
			String serviceInventoryData = requestBody;
			LOGGER.info("ServiceInventory data {} ::",serviceInventoryData);
			ScOrderBean request = (ScOrderBean) Utils.convertJsonToObject(serviceInventoryData, ScOrderBean.class);
			inventoryService.processServiceInventoryData(request);
		} catch (Exception e) {
			LOGGER.error("Error in process processServiceInventory data ", e);
		}
	}

	/**
	 * Queue listener to retrieve Existing GVPN details from service inventory for
	 * UCAAS WEBEX
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.gvpn.si.info.webex.queue}") })
	public String getGVPNServiceInventoryInfoForUCAAS(String request) throws TclCommonException {
		String response = null;
		try {
			SIExistingGVPNBean existingGVPNRequest = Utils.convertJsonToObject(request, SIExistingGVPNBean.class);
			SIExistingGVPNBean serviceInfoBean = inventoryService.getGVPNInfoForUCAAS(existingGVPNRequest);
			LOGGER.info("Service info : {}",Utils.convertObjectToJson(serviceInfoBean));
			if (Objects.nonNull(serviceInfoBean) && serviceInfoBean.getTotalItems() != 0)
				response = Utils.convertObjectToJson(serviceInfoBean);
		} catch (Exception e) {
			LOGGER.error("Error in fetching GVPN details From Service Inventory for given Customer ID");
		}

		return response;
	}

	/**
	 * Queue listener to retrieve Existing GVPN details using search Criteria
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.search.gvpn.si.info.webex.queue}") })
	public String getGVPNInfoBySearchCriteriaForUCAAS(String request) throws TclCommonException {
		String response = null;
		try {
			SIInfoSearchBean searchBean = Utils.convertJsonToObject(request, SIInfoSearchBean.class);
			SIExistingGVPNBean serviceInfoBean = inventoryService.getGVPNInfoBySearchCriteria(searchBean);
			LOGGER.info("Service info by search criteria : {}", serviceInfoBean);
			if (Objects.nonNull(serviceInfoBean) && serviceInfoBean.getTotalItems() != 0)
				response = Utils.convertObjectToJson(serviceInfoBean);
		} catch (Exception e) {
			LOGGER.error(
					"Error in fetching GVPN details From Service Inventory for given Customer ID and search criteria");
		}

		return response;
	}
	
	/**
	 * Queue listener to get all Existing GVPN details for downloading information
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.download.gvpn.si.info.webex.queue}") })
	public String downloadGVPNInfo(String request) throws TclCommonException {
		String response = null;
		try {
			SIExistingGVPNBean existingGVPNRequest = Utils.convertJsonToObject(request, SIExistingGVPNBean.class);
			SIExistingGVPNBean serviceInfoBean = inventoryService.downloadSIInfoGVPN(existingGVPNRequest);
			LOGGER.info("Service inventory details for downloading excel : {}", Utils.convertObjectToJson(serviceInfoBean));
			if (Objects.nonNull(serviceInfoBean) && !serviceInfoBean.getServiceInfos().isEmpty())
				response = Utils.convertObjectToJson(serviceInfoBean);
		} catch (Exception e) {
			LOGGER.error("Error in fetching GVPN details From Service Inventory for given request");
		}

		return response;
	}

	/**
	 * Queue listener to retrieve Existing GVPN details from service inventory using
	 * service ID for UCAAS WEBEX
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.gvpn.si.info.serviceid.webex.queue}") })
	public String getGVPNInfoByServiceId(String request) throws TclCommonException {
		String response = null;
		try {
			String serviceId = request;
			LOGGER.info("Request received : {}", request);
			SIServiceInfoGVPNBean serviceInfoBean = inventoryService.getGVPNInfoByServiceId(serviceId);
			LOGGER.info("Getting service inventory details by service id : {}",
					Objects.nonNull(serviceInfoBean) ? Utils.convertObjectToJson(serviceInfoBean) : null);
			if (Objects.nonNull(serviceInfoBean))
				response = Utils.convertObjectToJson(serviceInfoBean);
		} catch (Exception e) {
			LOGGER.error("Error in fetching GVPN details From Service Inventory for given service ID");
		}

		return response;
	}	

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.o2c.migration.si.details.queue}") })
	public String getMigrationServiceInventoryDetails(String requestBody) {
		try {
			LOGGER.info("Input Payload received for getMigrationServiceInventoryDetails received: {}",requestBody);
			if(Objects.nonNull(requestBody)){
				SIOrderDataBean siOrderBean=inventoryService.getMigrationServiceInventoryDetails(requestBody);
				SIGetOrderResponse response = new SIGetOrderResponse();
				response.setStatus(CommonConstants.SUCCESS);
				response.setOrder(siOrderBean);
				return toJson(response);
			}else{
				SIGetOrderResponse response = new SIGetOrderResponse();
				response.setStatus(CommonConstants.ERROR);
				response.setMessage("Input data is empty");
				return toJson(response);
			}
			
		} catch (Exception e) {
			LOGGER.error("Error in process processServiceInventory data ", e);
			SIGetOrderResponse response = new SIGetOrderResponse();
			response.setStatus(CommonConstants.ERROR);
			response.setMessage("Error in process processServiceInventory data");
			return toJson(response);
		}
	}
	
    
    /**
     * Queue Listener
     *
     * @param request
     * @return String
     */
    @RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.order.details.queue}") })
    public String fetchOrderDataBasedOnService(String request) {
        String response = "";
        List<SIServiceDetailsBean>  serviceDetailsBeanList = null;
        try {
        	 LOGGER.info("request payload {}", request);
            String[] serviceIds = (request).split(",");
            LOGGER.info("service Ids passed {}",Arrays.asList(serviceIds));
           
            serviceDetailsBeanList = inventoryService.getOrderDataBasedOnService(serviceIds);
            
            response = Utils.convertObjectToJson(serviceDetailsBeanList);
            LOGGER.info("response {}", response);
        } catch (Exception e) {

            LOGGER.error("error in getting service details ", e);
        }
        return response;
    }

	/*
	 * Queue Listener
	 * @author Harini Sri Reka J
	 * @param request
	 * @return String
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.order.details.primary.secondary.queue}") })
	public String fetchOrderDataBasedOnServiceForPriSec(String request) {
		String response = "";
		List<SIServiceDetailsBean>  serviceDetailsBeanList = new ArrayList<>();
		HashSet<SIServiceDetailsBean> serviceDetailBeanSet= new HashSet<>();


		try {
			LOGGER.info("request payload {}", request);
			String[] serviceIds = (request).split(",");
			LOGGER.info("service Ids passed {}",Arrays.asList(serviceIds));

			serviceDetailBeanSet = inventoryService.getOrderDataBasedOnServiceForPriSec(serviceIds);

			response = Utils.convertObjectToJson(serviceDetailBeanSet);
			LOGGER.info("response {}", response);
		} catch (Exception e) {

			LOGGER.error("error in getting service details ", e);
		}
		return response;
	}
    
    /**
	 * Queue Listener
	 *
	 * @param request
	 * @return String
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.service.details.list.queue}") })
	public String fetchSIDataforAllRelatedServicesList(String request) {
		String response = "";
		try {
			 LOGGER.info("request payload {}", request);
			 String[] serviceIds = (request).split(",");

			 OrderSummaryBeanResponse responseBean = inventoryService.getServiceDetailsListPrimarySecondary(serviceIds);

			response = Utils.convertObjectToJson(responseBean);

		} catch (Exception e) {

			LOGGER.error("error in getting stae info ", e);
		}

		return response;

	}
	
	 /**
     * Queue Listener
     *
     * @param request
     * @return String
     */
    @RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.order.add.site.queue}") })
    public String fetchOrderDataForAddSite(String request) {
        String response = "";
        List<SIServiceDetailsBean>  serviceDetailsBeanList = null;
        try {
        	 LOGGER.info("request payload {}", request);
            String[] serviceIds = (request).split(",");
            LOGGER.info("service Ids passed {}",Arrays.asList(serviceIds));
           
            serviceDetailsBeanList = inventoryService.getOrderDataBasedOnAddSite(serviceIds);
            
            response = Utils.convertObjectToJson(serviceDetailsBeanList);
            LOGGER.info("response {}", response);
        } catch (Exception e) {

            LOGGER.error("error in getting service details ", e);
        }
        return response;
    }

	/**
	 * Queue Listener
	 * Primary and Secondary for Add Site
	 * @param request
	 * @return String
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.order.add.site.pri.sec.queue}") })
	public String fetchOrderDataForPriSecAddSite(String request) {
		String response = "";
		List<SIServiceDetailsBean>  serviceDetailsBeanList = new ArrayList<>();
		HashSet<SIServiceDetailsBean> serviceDetailBeanSet= new HashSet<>();

		try {
			LOGGER.info("request payload {}", request);
			String[] serviceIds = (request).split(",");
			LOGGER.info("service Ids passed {}",Arrays.asList(serviceIds));

			serviceDetailBeanSet = inventoryService.getOrderDataForServiceForPriSecAddSite(serviceIds);

			response = Utils.convertObjectToJson(serviceDetailBeanSet);
			LOGGER.info("response {}", response);
		} catch (Exception e) {

			LOGGER.error("error in getting service details ", e);
		}
		return response;
	}
    
    /**
	 * Queue Listener
	 *
	 * @param request
	 * @return String
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.npl.details.queue}") })
	public String fetchSIDataforAllServicesNPL(Message<String> request) {
		String response = "";
		try {
			String serviceId = request.getPayload();

			List<SIServiceInfoBean> responseBean = inventoryService.getServiceDetailsforNPL(serviceId);

			response = Utils.convertObjectToJson(responseBean);

			LOGGER.info("Response received: {}" +response);

		} catch (Exception e) {

			LOGGER.error("error in getting stae info ", e);
		}

		return response;

	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.order.get.service.queue}") })
	public String fetchSIOrderDataBasedOnServiceId(Message<String> request) {
		String response = "";
		try {
			String serviceId = request.getPayload();

			SIOrderDataBean responseBean = inventoryService.getSiOrderData(serviceId);

			response = Utils.convertObjectToJson(responseBean);

		} catch (Exception e) {

			LOGGER.error("error in getting state info ", e);
		}

		return response;

	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.service.get.contract.info.queue}") })
	public String fetchSiContractInfoBasedOnServiceId(Message<String> request) {
		String response = "";
		try {
			String requestPayload = request.getPayload();
			SIServiceInfoBean sIServiceInfoBean = Utils.convertJsonToObject(requestPayload, SIServiceInfoBean.class);

			SiServiceSiContractInfoBean responseBean = inventoryService.getSiContractInfoByServiceId(sIServiceInfoBean.getTpsServiceId(), sIServiceInfoBean.getProductName());
			response = Utils.convertObjectToJson(responseBean);
		} catch (Exception e) {

			LOGGER.error("error in getting state info ", e);
		}

		return response;

	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.o2c.si.terminate.queue}") })
	public void terminateService(Message<String> request) {
		try {
			LOGGER.info("terminateService");
			String serviceId = request.getPayload();
			LOGGER.info("ServiceId",serviceId);
			inventoryService.terminateService(serviceId);
		} catch (Exception e) {

			LOGGER.error("error in terminateService ", e);
		}
	}
	/**
	 * Queue Listener for EHS NDE detials
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.nde.details.queue}") })
	public String fetchSIDataforAllServicesNDE(Message<String> request) {
		String response =null;
		try {
			String cuid = request.getPayload();

			List<SICustomerInfoBean> responseBean = inventoryService.getServiceDetailsforNDE(cuid);

			response = Utils.convertObjectToJson(responseBean);

			LOGGER.info("Response received: {}" +response);

		} catch (Exception e) {

			LOGGER.error("error in getting stae info ", e);
		}

		return response;

	}
	
	 /**
		 * Queue Listener
		 *
		 * @param request
		 * @return String
		 */
		@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.nde.attributes.details.queue}") })
		public String fetchSiAttributesNde(Message<String> request) {
			String response = "";
			try {
				String serviceId = request.getPayload();

				SIServiceInfoBean responseBean = inventoryService.getAttributesValuesNde(serviceId);

				response = Utils.convertObjectToJson(responseBean);

				LOGGER.info("fetchSiAttributesNde Response received: {}" +response);

			} catch (Exception e) {

				LOGGER.error("error in getting stae info ", e);
			}

			return response;

		}
		
		/**
		 * Queue Listener for mc nde
		 *
		 * @param request
		 * @return String
		 */
		@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.npl.details.mc.queue}") })
		public String fetchSIDataforAllServicesNPLMc(String request) {
			String response = "";
			try {
				LOGGER.info("enter into service inventory quue"+request);
				 String[] serviceIds = (request).split(",");

				List<SIServiceInfoBean> responseBean = inventoryService.getServiceDetailsforNPLMc(serviceIds);

				response = Utils.convertObjectToJson(responseBean);

				LOGGER.info("Response received: {}" +response);

			} catch (Exception e) {

				LOGGER.error("error in getting stae info ", e);
			}

			return response;

		}

	/**
	 * Update Outpulse Detail By SI Order ID
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.inventory.outpulse.queue}") })
	public void updateOutpulseValue(String request) {
		try {
			LOGGER.info("Received Request :: {}", request);
			String siOrderId = request.split(":")[0];
			String outpulse = request.split(":")[1];
			String referenceOrderId = request.split(":")[2];
			inventoryService.updateOutpulseBySIOrderId(siOrderId, outpulse, referenceOrderId);
		} catch (Exception e) {
			LOGGER.error("error in changing outpulse value :: {} ", e);
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.attribute.queue}") })
	public void getServiceInventoryAttributes(Message<String> request) {
		try {
			LOGGER.info("Inside getServiceInventoryAttributes method : {}",request);
			String serviceCode = request.getPayload();
			if(Objects.nonNull(serviceCode) || ! serviceCode.isEmpty()) {
				LOGGER.info("ServiceId value in getServiceInventoryAttributes :{}", serviceCode);
				inventoryService.getServiceInventoryAttributes(serviceCode);
			}
		} catch (Exception e) {

			LOGGER.error("error in getServiceInventoryAttributes ", e);
		}
	}

	/**
	 * Get GSC Service Abbreviation for O2C
	 *
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.inventory.gsc.abbreviation.queue}") })
	public String getGscServiceAbbreviation(String request) {
		String response = "";
		try {
			LOGGER.info("Received Request :: {}", request);
			String erfCustLeId = request.split(CommonConstants.COLON)[0];
			String erfSupplierLeId = request.split(CommonConstants.COLON)[1];
			String subVariant = request.split(CommonConstants.COLON)[2];

			String gscServiceAbbreviation = inventoryService.getGscServiceAbbreviation(erfCustLeId, erfSupplierLeId, subVariant);
			response = Utils.convertObjectToJson(gscServiceAbbreviation);
			LOGGER.info("Response received: {}" +response);
		} catch (Exception e) {
			LOGGER.error("error in getting stae info ", e);
		}
		return response;
	}
	
	/**
     * Queue Listener
     *
     * @param request
     * @return String
     */
    @RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.service.underprov.queue}") })
    public String fetchServiceDetailsForCancellation(String request) {
        String response = "";
        List<SIServiceDetailsBean>  serviceDetailsBeanList = null;
        try {
        	 LOGGER.info("rabbitmq.si.service.underprov.queue request payload {}", request);
            String[] serviceIds = (request).split(",");
            LOGGER.info("service Ids passed {}",Arrays.asList(serviceIds));
           
            serviceDetailsBeanList = inventoryService.getServiceDetailsForCancellation(serviceIds);
            
            response = Utils.convertObjectToJson(serviceDetailsBeanList);
            LOGGER.info("cancellation underprov response {}", response);
        } catch (Exception e) {

            LOGGER.error("cancellation underprov error in getting service details ", e);
        }
        return response;
    }

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.renewals.si.validate.details.queue}") })
	public String fetchLtoOdetails(String request) {
		String response = "";
		try {
			LOGGER.info("Received Request :: {}", request);
			request =request.substring(1, request.length() - 1);
			String[] listString =  (request).split(",");
			List<RenevalsValidateBean> renevalsValidateBeanList  =renewalsServiceInventoryService.constructBasicInfoV2(listString);
			response = Utils.convertObjectToJson(renevalsValidateBeanList);
			
		} catch (Exception e) {
			LOGGER.error("error in changing outpulse value :: {} ", e);
		}
		
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.renewals.si.details.queue}") })
	public String fetchServicedetails(String request) {
		String response = "";
		try {
			LOGGER.info("Received Request :: {}", request);
			request =request.substring(1, request.length() - 1);
			String[] listString =  (request).split(",");
			RenewalsQuoteDetail quoteDetail =renewalsServiceInventoryService.constructQuoteDetailsNplForListV1(listString);
			response = Utils.convertObjectToJson(quoteDetail);
			
		} catch (Exception e) {
			LOGGER.error("error in changing outpulse value :: {} ", e);
		}
		
		return response;
	}
	
	 /**
     * Queue Listener
     *
     * @param request
     * @return String
     */
    @RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.order.details.inactive.queue}") })
    public String fetchOrderDataBasedOnServiceTermination(String request) {
        String response = "";
        List<SIServiceDetailsBean>  serviceDetailsBeanList = null;
        try {
        	 LOGGER.info("request payload {}", request);
            String[] serviceIds = (request).split(",");
            LOGGER.info("service Ids passed {}",Arrays.asList(serviceIds));
           
            serviceDetailsBeanList = inventoryService.getOrderDataBasedOnServiceTermination(serviceIds);
            
            response = Utils.convertObjectToJson(serviceDetailsBeanList);
            LOGGER.info("response {}", response);
        } catch (Exception e) {

            LOGGER.error("error in getting service details ", e);
        }
        return response;
    }
    
    /**
   	 * Queue Listener
   	 *
   	 * @param request
   	 * @return String
   	 */
   	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.npl.details.inactive.queue}") })
   	public String fetchSIDataforAllServicesNPLTermination(String request) {
   		String response = "";
   		try {
   			
   			
   			LOGGER.info("request payload {}", request);
            String[] serviceIds = (request).split(",");
            LOGGER.info("service Ids passed {}",Arrays.asList(serviceIds));

   			List<SIServiceInfoBean> responseBean = inventoryService.getServiceDetailsforNPLTermination(serviceIds);

   			response = Utils.convertObjectToJson(responseBean);

   			LOGGER.info("Response received: {}" +response);

   		} catch (Exception e) {

   			LOGGER.error("error in getting stae info ", e);
   		}

   		return response;

   	}
   	
   	/**
	 * Queue Listener
	 *
	 * @param request
	 * @return String
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.related.details.inactive.queue}") })
	public String fetchSIDataforAllRelatedServicesForInactiveCircuits(Message<String> request) {
		String response = "";
		try {
			String serviceId = request.getPayload();

			List<SIServiceInfoBean> responseBean = inventoryService.getServiceDetailsPrimarySecondaryForInactiveCircuits(serviceId);

			response = Utils.convertObjectToJson(responseBean);
			

		} catch (Exception e) {

			LOGGER.error("error in getting stae info ", e);
		}

		LOGGER.info(" rabbitmq.si.related.details.queue response {}",response);
		return response;

	}
	
    /**
   	 * Price Revision Detail Queue Listener
   	 *
   	 * @param request
   	 * @return String
   	 */
   	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.price.revision.detail.queue}") })
   	public String fetchSIPriceRevisionDetailTermination(String request) {
   		String response = "";
   		try {
   			LOGGER.info("rabbitmq.si.price.revision.detail.queue : request payload {}", request);
            String[] serviceIds = (request).split(",");
            LOGGER.info("rabbitmq.si.price.revision.detail.queue : service Ids passed {}",Arrays.asList(serviceIds));

   			List<SIPriceRevisionDetailBean> responseBean = inventoryService.getPriceRevisionDetailForTermination(serviceIds);
   			response = Utils.convertObjectToJson(responseBean);
   			LOGGER.info("rabbitmq.si.price.revision.detail.queue response - {}", response);
   		} catch (Exception e) {
   			LOGGER.error("rabbitmq.si.price.revision.detail.queue error in getting state info ", e);
   		}
   		return response;
   	}   	
	
   	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.o2c.update.commission.date.queue}") })
	public String updateCommissionDate(Message<String> responseBody) throws TclCommonException {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> inpRequest = ((Map<String, Object>) Utils.convertJsonToObject(responseBody.getPayload(),
					HashMap.class));
			if (inpRequest.containsKey("serviceCode") && null != inpRequest.get("serviceCode")
					&& inpRequest.containsKey("orderCode") && null != inpRequest.get("orderCode")) {
				if (inpRequest.containsKey("commissionDate")) {
					return ipcInventoryService.updateCommissionDate((String) inpRequest.get("serviceCode"),
							(String) inpRequest.get("orderCode"), (String) inpRequest.get("commissionDate"));
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in fetching Order Id From Service Id", e);
		}
		return "FAILURE";
	}
   	
   	/**
	 * Queue Listener
	 *
	 * @param request
	 * @return String
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.service.details.queue}") })
	public String fetchServiceIdDetailsUtilityPortal(Message<String> request) {
		String response = "";
		try {
			String serviceId = request.getPayload();
			List<ServiceDetailedInfoBean> responseBean = inventoryService.fetchServiceDetailsInfoUtil(serviceId);
			response = Utils.convertObjectToJson(responseBean);
		} catch (Exception e) {
			LOGGER.error("fetchServiceIdDetailsUtilityPortal error in getting service details info {} ", e);
		}
		LOGGER.info(" rabbitmq.si.related.details.queue response {}",response);
		return response;

	}


}
