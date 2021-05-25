package com.tcl.dias.preparefulfillment.servicefulfillment.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderAttributeBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.preparefulfillment.beans.CatalystVdomWrapperAPIResponse;
import com.tcl.dias.preparefulfillment.beans.CustomerLeContactDetailsBean;
import com.tcl.dias.preparefulfillment.beans.SecurityGroupCatalystBean;
import com.tcl.dias.preparefulfillment.beans.SecurityGroupResponse;
import com.tcl.dias.preparefulfillment.constants.ExceptionConstants;
import com.tcl.dias.preparefulfillment.ipc.beans.BillingAddressResponse;
import com.tcl.dias.preparefulfillment.ipc.beans.IPCAddressDetail;
import com.tcl.dias.preparefulfillment.ipc.beans.IPCBillingContact;
import com.tcl.dias.preparefulfillment.ipc.beans.IPCGeoCodeRequestBean;
import com.tcl.dias.preparefulfillment.ipc.beans.IPCGeoCodeResponseBean;
import com.tcl.dias.preparefulfillment.ipc.beans.IPCGeoCodeResponseDetailBean;
import com.tcl.dias.servicefulfillment.entity.entities.IpcImplementationSpoc;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetailAttributes;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.IpcImplementationSpocRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScProductDetailAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScProductDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.ScServiceDetailBean;
import com.tcl.dias.servicefulfillmentutils.constants.IPCServiceFulfillmentConstant;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MstStatusConstant;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.IpcAcceptanceService;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This file contains the IPCServiceFulfillmentService.java class. Service
 * class. Process service acceptance requests
 *
 * @author Mohamed Danish A
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class IPCServiceFulfillmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IPCServiceFulfillmentService.class);

	@Autowired
	private IpcAcceptanceService ipcAccpectanceService;
	
	@Autowired
	private NotificationService notificationService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private ScOrderRepository scOrderRepository;
	
	@Autowired
	private ScContractInfoRepository scContactInfoRepository;

	@Autowired
	private ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	IpcImplementationSpocRepository ipcImplementationSpocRepository;
	
	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;
	
	@Autowired
	ScProductDetailAttributesRepository scProductDetailAttributesRepository;
	
	@Autowired
	ScProductDetailRepository scProductDetailRepository;
	
	@Autowired
	MQUtils mqUtils;
	
	@Autowired
    RestClientService restClientService;
	
	@Value("${rabbitmq.location.detail}")
	private String locationQueue;
	
	@Value("${rabbitmq.suplierle.queue}")
	private String suplierLeQueue;
	
	@Value("${rabbitmq.billing.contact.queue}")
	private String billingContactQueue;
	
	@Value("${geocode.api.url}")
	private String geoAPIUrl;
	
	@Value("${geocode.api.username}")
	private String geoApiUsername;
	
	@Value("${geocode.api.password}")
	private String geoApiPassword;
	
	@Value("${rabbitmq.customer.contacts.queue}")
	private String customerContactsQueue;
	
	@Value("${rabbitmq.o2c.ipc.autoProvision}")
	private String ipcAutoProvisionInCatalystQueue;
	
	@Value("${rabbitmq.o2c.ipc.catalyst.vdom}")
	private String ipcCatalystVdomQueue;
	
	@Value("${rabbitmq.o2c.ipc.catalyst.security.group}")
	private String ipcCatalystSecurityGroupQueue;
	
	@Value("${rabbitmq.o2c.ipc.update.product.attributes}")
	private String ipcUpdateProductAttributes;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	/**
	 * 
	 * processServiceProvison - Process the task of service provision
	 * 
	 * @param scOrderCode
	 * 
	 */
	public void processServiceProvison(String scOrderCode) {

		ScOrder scOrderEntity = scOrderRepository.findByOpOrderCodeAndIsActive(scOrderCode, "Y");
		Optional<ScServiceDetail> optScServiceDetail = scOrderEntity.getScServiceDetails().stream().findFirst();
		ScOrderAttribute scOrderAttribute = scOrderAttributeRepository.findByScOrder_IdAndAttributeName(scOrderEntity.getId(), CommonConstants.IPC_PROVISIONING_FLOW);
		if(scOrderAttribute == null || !scOrderAttribute.getAttributeValue().equals(CommonConstants.IPC_SERVICE_DELIVERY)) {
			optScServiceDetail.ifPresent(scServiceDetail -> {
	
				List<Task> tasks = taskRepository.findByServiceIdAndMstStatus_code(scServiceDetail.getId(),
						MstStatusConstant.OPEN);
	
				tasks.forEach(task -> {
					String processInstanceId = task.getWfProcessInstId();
					if (Objects.nonNull(processInstanceId)) {
						Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId)
								.activityId(task.getMstTaskDef().getKey()).singleResult();
						if (execution != null) {
							runtimeService.trigger(execution.getId());
						} else {
							LOGGER.info("Execution is null for {} ", processInstanceId);
						}
					} else {
						LOGGER.info("Process instance ID is null for the task: {}", task.getId());
					}
				});
				scServiceDetail.setCommissionedDate(new Timestamp(new Date().getTime()));
				scServiceDetail.setServiceCommissionedDate(new Timestamp(new Date().getTime()));
				scServiceDetail.setActualDeliveryDate(new Timestamp(new Date().getTime()));
				scServiceDetailRepository.save(scServiceDetail);
			});
		}

	}

	public String processServiceAcceptance(Integer taskId, Boolean acceptanceFlag, Map<String, Object> map) throws TclCommonException {
		return ipcAccpectanceService.processServiceAcceptance(taskId, acceptanceFlag, map);
	}

	/**
	 * 	
	 * processServiceDeliveryIssue - Process service delivery issue task
	 * 
	 * @param taskId,
	 *            map
	 * @return String
	 * @throws TclCommonException 
	 */
	@SuppressWarnings("unchecked")
	public String processServiceDeliveryIssue(Integer taskId, Boolean issueAcceptance, Map<String, Object> map) throws TclCommonException {

		Optional<Task> optTask = taskRepository.findById(taskId);

		if (map.containsKey(IPCServiceFulfillmentConstant.SERVICE_RESOLUTION)) {
			Map<String, Object> serviceIssue = (Map<String, Object>) map
					.get(IPCServiceFulfillmentConstant.SERVICE_RESOLUTION);
			serviceIssue.put(IPCServiceFulfillmentConstant.TIMESTAMP, System.currentTimeMillis());

			optTask.ifPresent(task -> {
				
				if (issueAcceptance) {
					List<ScServiceDetail> scServiceDetails = scServiceDetailRepository.findByScOrderId(task.getScOrderId());
					scServiceDetails.stream().findFirst().ifPresent(scServiceDetail -> {
						scServiceDetail.setCommissionedDate(new Timestamp(new Date().getTime()));
						scServiceDetail.setServiceCommissionedDate(new Timestamp(new Date().getTime()));
						scServiceDetail.setActualDeliveryDate(new Timestamp(new Date().getTime()));
						scServiceDetailRepository.save(scServiceDetail);
					});
				}

				String processInstanceId = task.getWfProcessInstId();
				if (Objects.nonNull(processInstanceId)) {

					Map<String, Object> processVariables = runtimeService.getVariables(processInstanceId);

					List<Map<String, Object>> resolutionLogs = (List<Map<String, Object>>) processVariables
							.getOrDefault(IPCServiceFulfillmentConstant.SERVICE_RESOLUTION_LOG,
									new ArrayList<Map<String, Object>>());
					map.put(IPCServiceFulfillmentConstant.SERVICE_RESOLUTION_LOG, resolutionLogs);
					resolutionLogs.add(serviceIssue);

					Boolean status = (Boolean) serviceIssue.get(IPCServiceFulfillmentConstant.IS_RESOLVED);
					String comments = (String) serviceIssue.get(IPCServiceFulfillmentConstant.COMMENT);
					String resolution = status ? "Resolved" : "Rejected";
					String taskDetails = "Service Issue status: " + resolution + "\r\n" + "Service Issue Comments: "
							+ comments;

					String customerName = (String) processVariables.get("customerUserName");
					String customerEmail = (String) processVariables.get("customerEmail");

					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String completionDate = sdf.format(new Date());
					
					notificationService.notifyIpcTaskCompletion(customerEmail, customerName, task.getServiceCode(), 
							"Service Issue", completionDate, taskDetails, resolution);

				}

			});

		}

		map.put(IPCServiceFulfillmentConstant.SERVICE_ACCEPTED, Boolean.FALSE);
		map.put("ipcServiceAcceptanceTimeout", IPCServiceFulfillmentConstant.ISSUE_FLOW_ORDER_ACCEPTANCE_DURATION);
		return taskService.manuallyCompleteTask(taskId, map);

	}

	public Map<String, Object> getServiceDeliveryTaskDetails(Integer taskId) {
		Map<String, Object> response = new LinkedHashMap<>();
		Optional<Task> optTask = taskRepository.findById(taskId);
		optTask.ifPresent(task -> {
			String processInstanceId = task.getWfProcessInstId();
			if (Objects.nonNull(processInstanceId)) {
				Map<String, Object> processVariables = runtimeService.getVariables(processInstanceId);
				if (Objects.nonNull(processVariables)) {
					response.put(IPCServiceFulfillmentConstant.SERVICE_ISSUE,
							processVariables.get(IPCServiceFulfillmentConstant.SERVICE_ISSUE));
					response.put(IPCServiceFulfillmentConstant.SERVICE_ISSUE_ITERATION,
							processVariables.get(IPCServiceFulfillmentConstant.SERVICE_ISSUE_ITERATION));
					response.put(IPCServiceFulfillmentConstant.SERVICE_ISSUES_LOG,
							processVariables.get(IPCServiceFulfillmentConstant.SERVICE_ISSUES_LOG));
				}
			} else {
				LOGGER.info("Process instance ID is null for the task: {}", task.getId());
			}
		});
		return response;
	}

	public BillingAddressResponse getAddressDetails(String serviceCode) throws TclCommonException {
		BillingAddressResponse billingAddressResponse[] = {null};
        try {
        	LOGGER.info("ServiceDetailId: {}", serviceCode);
        	List<ScServiceDetail> scServiceDetailList=scServiceDetailRepository.findLastByUuidOrderByIdDesc(serviceCode);
        	if(null==scServiceDetailList || scServiceDetailList.isEmpty()){
        		throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);
        	}
        	LOGGER.info("Service Detail size: {}", scServiceDetailList.size());
    		ScOrder scOrder = scServiceDetailList.get(0).getScOrder();
            if (scOrder == null) {
                throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);
            }
            LOGGER.info("Order Code: {}", scOrder.getOpOrderCode());
            billingAddressResponse[0] = constructBillingAddress(scOrder,billingAddressResponse,serviceCode);
            LOGGER.info("Billing Address constructed is {}", billingAddressResponse);
        } catch (Exception e) {
            LOGGER.warn("Cannot get address details");
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return billingAddressResponse[0];
	}

	private BillingAddressResponse constructBillingAddress(ScOrder scOrder,BillingAddressResponse[] billingAddressResponse, String serviceCode) throws TclCommonException {
		LOGGER.info("constructBillingAddress input param:{}", scOrder.getOpOrderCode());
		try {
	    	billingAddressResponse[0] = new BillingAddressResponse();
	    	scOrder.getScOrderAttributes().stream().forEach(orderAttr ->{
	    			try{
		    			switch (orderAttr.getAttributeName()) {
				    				case LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY:
				    				LOGGER.info("Customer Contracting entity {}:",orderAttr.getAttributeName());
				    				billingAddressResponse[0].setCustomerAddressDetail(constructCustomerLocationDetails(orderAttr));
				    				break;
			    				 }
	    				}catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
						}
	    			
	    		});
	    	 	//Populate Site Address
			if (scOrder != null && scOrder.getUuid().startsWith("IPC")) {
				populateSiteLocationAddressA(scOrder, billingAddressResponse[0]);
			}else {
				populateSiteAddressA(scOrder.getUuid(),billingAddressResponse[0],serviceCode);
			}
	    		
	    	    //Populate Billing Address
	    		ScContractInfo scContactInfo=scContactInfoRepository.findFirstByScOrder_id(scOrder.getId());
	    		billingAddressResponse[0].setBillingAddressDetail(constructBillingInformations(scContactInfo));
	    		//Populate Sp Address
	    		SPDetails spDetail=constructSupplierInformations(scContactInfo.getErfCustSpLeId());
	    		getSpAddressGeoCode(billingAddressResponse[0],spDetail);
    	} catch (Exception e) {
    		LOGGER.info("Site address {}:"+e.getMessage());
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return billingAddressResponse[0];
	}

	
	private void populateSiteLocationAddressA(ScOrder scOrder, BillingAddressResponse billingAddressResponse) {
		Optional<ScServiceDetail> scServiceDetailOptional=scOrder.getScServiceDetails().stream().findFirst();
		if(scServiceDetailOptional.isPresent()){
			ScServiceDetail scServiceDetail=scServiceDetailOptional.get();
			IPCAddressDetail siteAddress = new IPCAddressDetail();
			if(null!=scServiceDetail.getSiteAddress()){
				LOGGER.info("Site address {}:",scServiceDetail.getSiteAddress());
				siteAddress.setAddressLineOne(scServiceDetail.getSiteAddress());
	    		String[] address=scServiceDetail.getSiteAddress().split(",");
	    		siteAddress.setCity(address[0].trim());
	    		siteAddress.setCountry(address[1].trim());
	    		billingAddressResponse.setSiteLocationA(siteAddress);
			}
		}
	}

	private void populateSiteAddressA(String scOrderCode, BillingAddressResponse billingAddressResponse,String serviceCode) {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndScOrder_OpOrderCode(serviceCode, scOrderCode);

		if (scServiceDetail !=null) {
			Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
					.getComponentAttributesDetails(Arrays.asList("destinationCity", "destinationState",
							"destinationCountry", "destinationAddressLineOne", "destinationAddressLineTwo",
							"destinationLocality", "destinationPincode"), scServiceDetail.getId(), "LM", "A");
			String addressFull = StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationAddressLineOne"))
					+ " " + StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationAddressLineTwo")) + " "
					+ StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationLocality"));
			String destinationAddressLineOne = "";
			String destinationAddressLineTwo = "";
			String destinationAddressLineThree = "";

			if (addressFull.length() <= 80) {
				destinationAddressLineOne = addressFull.substring(0, addressFull.length());
			}
			if (addressFull.length() >= 81 && addressFull.length() <= 160) {
				destinationAddressLineOne = addressFull.substring(0, 80);
				destinationAddressLineTwo = addressFull.substring(80, addressFull.length());
			}
			if (addressFull.length() >= 161 && addressFull.length() <= 240) {
				destinationAddressLineOne = addressFull.substring(0, 80);
				destinationAddressLineTwo = addressFull.substring(80, 160);
				destinationAddressLineThree = addressFull.substring(160, addressFull.length());
			}
			if (addressFull.length() > 240) {
				destinationAddressLineOne = addressFull.substring(0, 80);
				destinationAddressLineTwo = addressFull.substring(80, 160);
				destinationAddressLineThree = addressFull.substring(160, 240);
			}

			IPCAddressDetail siteAddress = new IPCAddressDetail();
			siteAddress.setAddressLineOne(destinationAddressLineOne);
			siteAddress.setAddressLineTwo(destinationAddressLineTwo);
			siteAddress.setAddressLineThree(destinationAddressLineThree);
			siteAddress.setCity(StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
			siteAddress.setState(StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
			siteAddress.setCountry(StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
			siteAddress.setPincode(StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationPincode")));
			billingAddressResponse.setSiteLocationA(siteAddress);
		}
	}
	
	private void getSpAddressGeoCode(BillingAddressResponse billingAddressResponse,SPDetails spDetail) throws TclCommonException, IOException {
		if(spDetail!=null 
				&& spDetail.getAddress()!=null){
			IPCAddressDetail addressDetail = getAddressDetailBySupplierId(spDetail.getEntityName());
			if(addressDetail!=null){
				LOGGER.info("Sp address detail exists {}:",addressDetail);
				billingAddressResponse.setSupplierAddressDetail(addressDetail);
				if("United States".equals(addressDetail.getCountry())
						|| "Canada".equals(addressDetail.getCountry())){
					Map<String,String> queryHashMap=new HashMap<>();
					queryHashMap.put("street", spDetail.getAddress());
					queryHashMap.put("city", addressDetail.getCity());
					queryHashMap.put("state",addressDetail.getState());
					queryHashMap.put("country",addressDetail.getCountry());
	    			RestResponse restResponse =restClientService.getWithBasicAuthentication(geoAPIUrl,queryHashMap, geoApiUsername, geoApiPassword);
	    			if(restResponse.getStatus().equals(Status.SUCCESS)){
	    				LOGGER.info("SUCCESS {}:",restResponse.getData());
	    				ObjectMapper mapper = new ObjectMapper();
	    				mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	    			    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	    			    IPCGeoCodeResponseDetailBean ipcGeoCodeResponseDetailBean= mapper.reader().forType(IPCGeoCodeResponseDetailBean.class).readValue(restResponse.getData().toString());
	    				billingAddressResponse.getBillingAddressDetail().setGeoCode(ipcGeoCodeResponseDetailBean.getGeoCode());
	    			}
				}
			}
		}
	}
	
	private void getSpAddressGeoCodeByPOSTCall(BillingAddressResponse billingAddressResponse,SPDetails spDetail) throws TclCommonException {
		if(spDetail!=null 
				&& spDetail.getAddress()!=null){
			IPCGeoCodeRequestBean ipcGeoCodeRequestBean = new IPCGeoCodeRequestBean();
			IPCAddressDetail addressDetail = getAddressDetailBySupplierId(spDetail.getEntityName());
			if(addressDetail!=null){
				LOGGER.info("Sp address detail exists {}:",addressDetail);
				billingAddressResponse.setSupplierAddressDetail(addressDetail);
				if("United States".equals(addressDetail.getCountry())
						|| "Canada".equals(addressDetail.getCountry())){
					ipcGeoCodeRequestBean.setCorrId(Utils.generateUid());
	    			ipcGeoCodeRequestBean.setCountry(addressDetail.getCountry());
	    			ipcGeoCodeRequestBean.setStreetAddress(spDetail.getAddress());
	    			ipcGeoCodeRequestBean.setState(addressDetail.getState());
	    			ipcGeoCodeRequestBean.setCity(addressDetail.getCity());
	    			ipcGeoCodeRequestBean.setZipCode(addressDetail.getPincode());
	    			LOGGER.info("GeoCode Request{}:",ipcGeoCodeRequestBean);
	    			RestResponse restResponse =restClientService.postWithProxyBasicAuthentication(geoAPIUrl, Utils.convertObjectToJson(ipcGeoCodeRequestBean), new HashMap<String,String>(), geoApiUsername, geoApiPassword);
	    			if(restResponse.getStatus().equals(Status.SUCCESS)){
	    				LOGGER.info("SUCCESS {}:",restResponse.getData());
	    				IPCGeoCodeResponseBean ipcGeoCodeResponseBean=(IPCGeoCodeResponseBean) Utils.convertJsonToObject(restResponse.getData(), Object.class);
	    				billingAddressResponse.getBillingAddressDetail().setGeoCode(ipcGeoCodeResponseBean.getAddressObj().getGeoCode());
	    			}
				}
			}
		}
	}

	private IPCBillingContact constructBillingInformations(ScContractInfo scContractInfo) throws TclCommonException {
		if (scContractInfo.getBillingContactId()!=null) {
			LOGGER.info("MDC Filter token value in before Queue call constructBillingInformations {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String billingContactResponse = (String) mqUtils.sendAndReceive(billingContactQueue, String.valueOf(scContractInfo.getBillingContactId()));
			if (StringUtils.isNotBlank(billingContactResponse)) {
				IPCBillingContact billingContact = (IPCBillingContact) Utils.convertJsonToObject(billingContactResponse,
						IPCBillingContact.class);
				LOGGER.info("Printing billing contact for debugging : {} :", billingContact);
				return billingContact;
			}

		}
		return null;
		
	}

	/**
	 * constructCustomerLocationDetails
	 * 
	 * @param cofPdfRequest
	 * @param orderLeAttr
	 * @throws TclCommonException
	 */
	private IPCAddressDetail constructCustomerLocationDetails(ScOrderAttribute orderAttr) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call constructCustomerLocationDetails {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue, String.valueOf(orderAttr.getAttributeValue()));
		if (StringUtils.isNotBlank(locationResponse)) {
			IPCAddressDetail addressDetail = (IPCAddressDetail) Utils.convertJsonToObject(locationResponse,
					IPCAddressDetail.class);
			LOGGER.info("Printing Address detail for debugging : {} :", addressDetail);
			return addressDetail;
		}
		return null;
	}
	
	private SPDetails constructSupplierInformations(Integer spLeId)
			throws TclCommonException {
		if (spLeId != null) {
			LOGGER.info("MDC Filter token value in before Queue call constructSupplierInformations {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String supplierResponse = (String) mqUtils.sendAndReceive(suplierLeQueue, String.valueOf(spLeId));
			if (StringUtils.isNotBlank(supplierResponse)) {
				SPDetails spDetails = (SPDetails) Utils.convertJsonToObject(supplierResponse, SPDetails.class);
				LOGGER.info("Printing spdetails for debugging : {} :", spDetails);
				return spDetails;
			}	
		}
		return null;
	}
	
	private IPCAddressDetail getAddressDetailBySupplierId(String supplierEntityName) throws TclCommonException {
		LOGGER.info("SpEntityName : {} :", supplierEntityName);
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue, String.valueOf(supplierEntityName));
		LOGGER.info("Sp address detail {}:",locationResponse);
		return (IPCAddressDetail) Utils.convertJsonToObject(locationResponse, IPCAddressDetail.class);
	}

	public CustomerLeContactDetailsBean getContactDetailsForTheCustomerLeId(Integer customerLeId) {
		try {
			if(null != customerLeId) {
				LOGGER.info("Customer contact details queue starts {}", customerLeId);
				String contactDetails = (String) mqUtils.sendAndReceive(customerContactsQueue, String.valueOf(customerLeId));
				LOGGER.info("Customer contact details queue ends {}", contactDetails);
				if(StringUtils.isNotBlank(contactDetails)) {
					CustomerLeContactDetailsBean cusLeContactDetailsBean = (CustomerLeContactDetailsBean) Utils.convertJsonToObject(contactDetails, CustomerLeContactDetailsBean.class);
					return cusLeContactDetailsBean;
				}
			}
		} catch(Exception e) {
			LOGGER.error("Error occurred while sending details to customer contacts queue", e);
		}
		return null;
	}
	
	public Task getTaskByIdAndWfTaskId(Integer taskId,String wfTaskId) {
		return taskRepository.findByIdAndWfTaskId(taskId,wfTaskId);
	}

	public List<IpcImplementationSpoc> findAllSpocUsers() {
		List<IpcImplementationSpoc> spocUsersList = ipcImplementationSpocRepository.findAll();
		LOGGER.debug("Spoc users list {}", spocUsersList);
		return spocUsersList;
	}

	public String triggerIpcSignal(Integer taskId, String wfTaskId, String signalId) {
		LOGGER.info("Signal Call initiated for taskId: {}, wftaskId: {}, signalId: {}", taskId, wfTaskId, signalId);
		Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
		if (task != null) {
			Execution execution = runtimeService.createExecutionQuery().processInstanceId(task.getWfProcessInstId()).activityId(signalId).singleResult();
			if (execution != null) {
				LOGGER.info("execution ID: {}, value: {}", execution.getId(), execution);
				runtimeService.trigger(execution.getId());
			} else {
				LOGGER.info("Execution is null");
			}
		}
		LOGGER.info("Signal Call completed for taskId: {}, wftaskId: {}", taskId, wfTaskId);
		return IpcConstants.SUCCESS;
	}

	public String autoProvisionInCatalyst(Integer taskId, String wfTaskId, Map<String, Object> ipcAttributes)
			throws TclCommonException {
		Task task = taskRepository.findByIdAndWfTaskId(taskId, wfTaskId);
		LOGGER.info("IPC Auto provision initiated for orderCode: {}", task.getOrderCode());
		String response = IpcConstants.FAILURE;
		if (task != null) {
			Map<String, String> requestMap = new HashMap<>();
			requestMap.put(CommonConstants.ORDERCODE, task.getOrderCode());
			if (ipcAttributes.containsKey(CommonConstants.PROVISION_TYPE)) {
				requestMap.put(CommonConstants.PROVISION_TYPE,
						String.valueOf(ipcAttributes.get(CommonConstants.PROVISION_TYPE)));
			}
			if (ipcAttributes.containsKey(CommonConstants.IS_ORDER_ALREADY_IMPLEMENTED)) {
				requestMap.put(CommonConstants.IS_ORDER_ALREADY_IMPLEMENTED,
						String.valueOf(ipcAttributes.get(CommonConstants.IS_ORDER_ALREADY_IMPLEMENTED)));
			}
			response = (String) mqUtils.sendAndReceive(ipcAutoProvisionInCatalystQueue,
					Utils.convertObjectToJson(requestMap));
			if (response.equals(IpcConstants.SUCCESS)) {
				taskService.saveIpcTaskAttributes(taskId, wfTaskId, ipcAttributes);
			}
			LOGGER.info("IPC Auto provision completed for orderCode: {} with status: {}", task.getOrderCode(),
					response);
		}
		return response;
	}

	public ScServiceDetailBean getParentScServiceDetails(String uuid) {
		List<ScServiceDetail> scServiceDetailList = scServiceDetailRepository.findByUuidOrderByIdDesc(uuid);
		ScServiceDetail scServiceDetail = new ScServiceDetail();
		ScServiceDetailBean scServiceDetailBean = null;
		if(null != scServiceDetailList && !scServiceDetailList.isEmpty() && scServiceDetailList.size() > 1) {
			scServiceDetailBean = new ScServiceDetailBean();
			scServiceDetail = scServiceDetailList.get(1);
			scServiceDetailBean.setUuid(scServiceDetail.getUuid());
			scServiceDetailBean.setScOrderUuid(scServiceDetail.getScOrderUuid());
			scServiceDetailBean.setErfPrdCatalogProductId(scServiceDetail.getErfPrdCatalogProductId());
			scServiceDetailBean.setErfPrdCatalogOfferingName(scServiceDetail.getErfPrdCatalogOfferingName());
			scServiceDetailBean.setErfPrdCatalogProductName(scServiceDetail.getErfPrdCatalogProductName());
			scServiceDetailBean.setIsIzo(scServiceDetail.getIsIzo());
			scServiceDetailBean.setSiteAddress(scServiceDetail.getSiteAddress());
			scServiceDetailBean.setPopSiteAddress(scServiceDetail.getPopSiteAddress());
			scServiceDetailBean.setPopSiteCode(scServiceDetail.getPopSiteCode());
			scServiceDetailBean.setSourceCountry(scServiceDetail.getSourceCountry());
			scServiceDetailBean.setSourceCity(scServiceDetail.getSourceCity());
			scServiceDetailBean.setSiteType(scServiceDetail.getSiteType());
			scServiceDetailBean.setSourceAddressLineTwo(scServiceDetail.getSourceAddressLineTwo());
			scServiceDetailBean.setSourceLocality(scServiceDetail.getSourceLocality());
			scServiceDetailBean.setSourcePincode(scServiceDetail.getSourcePincode());
			scServiceDetailBean.setSourceState(scServiceDetail.getSourceState());
			scServiceDetailBean.setSourceAddressLineOne(scServiceDetail.getSourceAddressLineOne());
			scServiceDetailBean.setMrc(scServiceDetail.getMrc());
			scServiceDetailBean.setNrc(scServiceDetail.getNrc());
			scServiceDetailBean.setArc(scServiceDetail.getArc());
			scServiceDetailBean.setTaxExemptionFlag(scServiceDetail.getTaxExemptionFlag());
			scServiceDetailBean.setServiceCommissionedDate(scServiceDetail.getServiceCommissionedDate());
			scServiceDetailBean.setServiceStatus(scServiceDetail.getServiceStatus());
			scServiceDetailBean.setCreatedBy(scServiceDetail.getCreatedBy());
			scServiceDetailBean.setUpdatedBy(scServiceDetail.getUpdatedBy());
			scServiceDetailBean.setCreatedDate(scServiceDetail.getCreatedDate());
			scServiceDetailBean.setUpdatedDate(scServiceDetail.getUpdatedDate());
			scServiceDetailBean.setIsActive(scServiceDetail.getIsActive());
			scServiceDetailBean.setActualDeliveryDate(scServiceDetail.getActualDeliveryDate());
			scServiceDetailBean.setErfOdrServiceId(scServiceDetail.getErfOdrServiceId());
			scServiceDetailBean.setOrderType(scServiceDetail.getOrderType());
			scServiceDetailBean.setOrderCategory(scServiceDetail.getOrderCategory());
		}
		return scServiceDetailBean;
	}
	
	public String processProductAttributes(List<Map<String, String>> attributeRequest) throws TclCommonException {
		LOGGER.info("Start processProductAttributes");
		String response = IpcConstants.FAILURE;
		attributeRequest.forEach(attribute -> {
			Integer scProdctDetailId = Integer.parseInt(attribute.get("id"));
			attribute.entrySet().stream()
					.filter(x -> !Arrays.asList("id", "cloudCode", "category").contains(x.getKey()))
					.forEach(productAttrMap -> {
						ScProductDetailAttributes productAttr = scProductDetailAttributesRepository
								.findFirstByScProductDetail_idAndAttributeNameOrderByIdDesc(scProdctDetailId,
										productAttrMap.getKey());
						LOGGER.info("Input Key {}", productAttrMap.getKey());
						if (null != productAttr) {
							productAttr.setAttributeValue(attribute.get(productAttr.getAttributeName()));
						} else {
							LOGGER.info("Create new attribute {}", attribute.get("category"));
							productAttr = new ScProductDetailAttributes();
							productAttr.setScProductDetail(scProductDetailRepository.findById(scProdctDetailId).get());
							productAttr.setCategory(attribute.get("category"));
							productAttr.setAttributeName(productAttrMap.getKey());
							productAttr.setAttributeValue(productAttrMap.getValue());
							productAttr.setIsActive("Y");
							productAttr.setCreatedBy(Utils.getSource());
							productAttr.setCreatedDate(new Timestamp(new Date().getTime()));
						}
						productAttr.setUpdatedBy(Utils.getSource());
						productAttr.setUpdatedDate(new Timestamp(new Date().getTime()));
						scProductDetailAttributesRepository.save(productAttr);
					});
		});
		response = (String) mqUtils.sendAndReceive(ipcUpdateProductAttributes,
				Utils.convertObjectToJson(attributeRequest));
		LOGGER.info("End processProductAttributes {}", response);
		return response;
	}
	
	/**
	 * @param customer
	 * @param location
	 * @return
	 * @throws TclCommonException
	 */
	public CatalystVdomWrapperAPIResponse fetchCatalystVdomDetails(String customer, String location)
			throws TclCommonException {
		LOGGER.info("Start fetchCatalystVdomDetails..");
		Map<String, String> requestMap = new HashMap<>();
		requestMap.put("CUSTOMER", customer);
		requestMap.put("LOCATION", location);
		String catalystResponse = (String) mqUtils.sendAndReceive(ipcCatalystVdomQueue,
				Utils.convertObjectToJson(requestMap));
		CatalystVdomWrapperAPIResponse wrapperApiResp = (CatalystVdomWrapperAPIResponse) Utils
				.convertJsonToObject(catalystResponse, CatalystVdomWrapperAPIResponse.class);
		LOGGER.info("fetchCatalystVdomDetails response {}", wrapperApiResp);
		return wrapperApiResp;
	}

	public SecurityGroupResponse fetchSecurityGroupCatalystDetails(SecurityGroupCatalystBean request, String vDomName) throws TclCommonException {
		LOGGER.info("Start fetchSecurityGroupCatalystDetails..");
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("catalystBean", request);
		requestMap.put("vDomName", vDomName);
		String catalystResponse = (String) mqUtils.sendAndReceive(ipcCatalystSecurityGroupQueue,
				Utils.convertObjectToJson(requestMap));
		LOGGER.info("Security Group catalystResponse {}" , catalystResponse);
		SecurityGroupResponse securityGrpApiResp = (SecurityGroupResponse) Utils
				.convertJsonToObject(catalystResponse, SecurityGroupResponse.class);
		LOGGER.info("fetchSecurityGroupCatalystDetails response {}", securityGrpApiResp);
		return securityGrpApiResp;
	}
	
	public String triggerExpSurvey(Integer taskId) throws TclCommonException {
		Optional<Task> optTask = taskRepository.findById(taskId);
		if(optTask.isPresent()) {
			Task task = optTask.get();
			Map<String, Object> processVariables = runtimeService.getVariables(task.getWfProcessInstId());
			runtimeService.startProcessInstanceByKey(IpcConstants.IPC_MANUAL_EXPERIENCE_SURVEY_WORKFLOW, processVariables);
			return IpcConstants.SUCCESS;
		}
		return IpcConstants.FAILURE; 
	}
}
