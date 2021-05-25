package com.tcl.dias.servicehandover.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicehandover.bean.lr.termination.OptimusTerminationRequestBO;
import com.tcl.dias.servicehandover.bean.lr.termination.OptimusTerminationResponseBO;
import com.tcl.dias.servicehandover.bean.lr.termination.Request;
import com.tcl.dias.servicehandover.bean.lr.termination.RequestResponse;
import com.tcl.dias.servicehandover.config.SOAPConnector;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrder;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.repository.GnvOrderEntryTabRepository;
import com.tcl.dias.servicehandover.util.GenevaIpcOrderEntryMapper;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.NetworkUniqueProductCreation;
import com.tcl.dias.servicehandover.util.TimeStampUtil;
import com.tcl.servicehandover.entity.GnvOrderEntryTab;


/**
 * Service Class for Network Products Termination Optimus & LR
 * and Invoice Generation
 * 
 * @author yomagesh
 *
 */
@Service
public class BillingProductTerminationService {

	@Autowired
	@Qualifier("Order")
	SOAPConnector orderSoapConnector;
	
	@Value("${createOrder}")
	private String createOrderOperation;
	
	@Autowired
	@Qualifier("Terminate")
	SOAPConnector terminateSoapConnector;

	@Value("${terminateService}")
	private String terminateServiceOperation;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepo;

	@Autowired
	ScChargeLineitemRepository chargeLineitemRepository;

	@Autowired
	GnvOrderEntryTabRepository gnvOrderEntryTabRepository;
	
	@Autowired
	GenevaIpcOrderEntryMapper genevaIpcOrderEntryMapper;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	BillingProductCreationService billingProductCreationService;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	String status = "";
	
	ScServiceDetail serviceDetailOld = null;
	
	String circuitStatus="INACTIVE";
	
	Integer parallelDays=0; 
	
	boolean isParallelUpgrade = false;
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingProductTerminationService.class);

	/**
	 * Terminates all products that are commissioned via Optimus
	 * 	
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public CreateOrderResponse productTermination(String orderId, String processInstanceId, String serviceCode,
			String serviceType, String serviceId) {
		LOGGER.info("Product Termination invoked for orderId {} and Service type {} parallel days{} isParallelUpgrade {}", orderId, serviceType, parallelDays ,isParallelUpgrade);
		CreateOrder createOrder = new CreateOrder();
		CreateOrderBO createOrderBO = new CreateOrderBO();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		LOGGER.info("service ID: {} service type={}", serviceCode, serviceType);
		ScServiceDetail serviceDetailNew = scServiceDetailRepository.findById(Integer.parseInt(serviceId)).get();
		String orderType=OrderCategoryMapping.getOrderType(serviceDetailNew, serviceDetailNew.getScOrder());
		
		String termEndDate = commonFulfillmentUtils
				.getComponentAttributesDetails(Arrays.asList(NetworkConstants.BILL_START_DATE),
						serviceDetailNew.getId(), "LM", "A")
				.get(NetworkConstants.BILL_START_DATE);

		if (scOrder != null && "MACD".equalsIgnoreCase(orderType)
				&& Objects.nonNull(serviceDetailNew.getOrderSubCategory())
				&& serviceDetailNew.getOrderSubCategory().toLowerCase().contains("parallel")) {
			LOGGER.info("Parallel Flow");
			if (Objects.nonNull(serviceDetailNew.getParentUuid())) {
				serviceCode = serviceDetailNew.getParentUuid();
			}
			ScServiceAttribute scServiceDownTimeAttr = scServiceAttributeRepository
					.findFirstByScServiceDetail_idAndAttributeName(serviceDetailNew.getId(), "downtime_duration");
			if (Objects.nonNull(scServiceDownTimeAttr)
					&& StringUtils.isNotEmpty(scServiceDownTimeAttr.getAttributeValue())) {
				LOGGER.info("ParallelDownTime Days");
				parallelDays = Integer.parseInt(scServiceDownTimeAttr.getAttributeValue());
			}
			isParallelUpgrade = true;
			circuitStatus = "ACTIVE";
		}

		if(isParallelUpgrade) {
			LOGGER.info("For Parallel Upgrade / Downgrade for service {} with term end date as {}:",serviceCode,termEndDate);
			serviceDetailOld = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode, circuitStatus);
		}
		else {
			LOGGER.info("For Hot Upgrade / Downgrade for service {} with term end date as {}:",serviceCode,termEndDate);
			serviceDetailOld = scServiceDetailRepository.findFirstByServiceLinkIdOrderByIdDesc(serviceCode);
		}
		if (serviceDetailNew != null && serviceDetailOld!=null) {
			LOGGER.info("is Parallel Upgrade {} and Parallel Run Days {}",isParallelUpgrade,parallelDays);
			String groupId = NetworkConstants.OPT_PROD.concat(serviceDetailOld.getScOrderUuid())+ "_"+ System.currentTimeMillis() + "_T";
			List<ScChargeLineitem> chargeLineitems = chargeLineitemRepository
					.findByServiceIdAndServiceType(serviceDetailOld.getId().toString(), serviceType);
			chargeLineitems.forEach(chargeLineitem -> {
				NetworkUniqueProductCreation uniqueProductCreation = new NetworkUniqueProductCreation();
				uniqueProductCreation.setProductName(chargeLineitem.getChargeLineitem());
				uniqueProductCreation.setAccountId(chargeLineitem.getAccountNumber());
				uniqueProductCreation.setMrc(Float.parseFloat(chargeLineitem.getMrc()));
				uniqueProductCreation.setNrc(Float.parseFloat(chargeLineitem.getNrc()));
				uniqueProductCreation.setArc(Float.parseFloat(chargeLineitem.getArc()));
				uniqueProductCreation.setScOrder(scOrder);
				uniqueProductCreation.setScServiceDetail(serviceDetailOld);
				uniqueProductCreation.setProcessInstanceId(processInstanceId);
				uniqueProductCreation.setActionType(NetworkConstants.TERMINATE);
				uniqueProductCreation.setOrderType(NetworkConstants.MODIFY);
				uniqueProductCreation.setChangeOrderType(Constants.TERMINATE_ORDER);
				uniqueProductCreation.setQuantity(NetworkConstants.ONE);
				uniqueProductCreation.setServiceType(chargeLineitem.getServiceType());
				uniqueProductCreation.setComponent(chargeLineitem.getComponent());
				uniqueProductCreation.setCpeModel(StringUtils.trimToEmpty(chargeLineitem.getCpeModel()));
				uniqueProductCreation.setHsnCode(chargeLineitem.getHsnCode());
				uniqueProductCreation.setIsMacd(true);
				uniqueProductCreation.setInputGroupId(groupId);
				uniqueProductCreation.setCustomerRef(chargeLineitem.getCustomerRef());
				if (NetworkConstants.NPL.equals(serviceDetailNew.getErfPrdCatalogProductName())) {
					uniqueProductCreation.setSiteType(chargeLineitem.getSiteType());
					uniqueProductCreation.setServiceType(NetworkConstants.ACC_NPL_INTRACITY);
				}
				uniqueProductCreation.setParallelMacd(isParallelUpgrade);
				uniqueProductCreation.setParallelRunDays(parallelDays);
				uniqueProductCreation.setTermEndDate(termEndDate);
				if(chargeLineitem.getSourceProdSequence()!=null) {
					uniqueProductCreation.setSourceProductSeq(chargeLineitem.getSourceProdSequence());
				}else {
					uniqueProductCreation.setSourceProductSeq(gnvOrderEntryTabRepository.findSourceProdSeq(chargeLineitem.getAccountNumber(),chargeLineitem.getChargeLineitem()));
				}
				uniqueProductCreation.setBillingMethod(chargeLineitem.getBillingMethod());
				uniqueProductCreation.setBillingType(chargeLineitem.getBillingType());
				uniqueProductCreation.setMacdServiceId(serviceId);
				chargeLineitem.setActionType(NetworkConstants.TERMINATE);
				chargeLineitemRepository.save(chargeLineitem);
				chargeLineitemRepository.flush();
				uniqueProductCreation.setServicehandoverAudit(new ServicehandoverAudit());
				CreateOrderRequestBO createOrderRequestBO = billingProductCreationService.createUniqueProduct(uniqueProductCreation);
				createOrderBO.getReqOrder().add(createOrderRequestBO);
			});
		}
		
		createOrder.setCreateOrderRequestInput(createOrderBO);
		JaxbMarshallerUtil.jaxbObjectToXML(createOrder, new ServicehandoverAudit());
		CreateOrderResponse createOrderResponse = (CreateOrderResponse) orderSoapConnector
				.callWebService(createOrderOperation, createOrder);
		if (Objects.nonNull(createOrderResponse)) {
			status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
			return createOrderResponse;
		}
		LOGGER.info("Product Termination completed for orderId {} with status {}", orderId, status);
		return new CreateOrderResponse();
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRES_NEW)
	public String serviceTerminationGeneva(String orderId, String processInstanceId, String serviceCode,
			String serviceType, String serviceId, Integer parallelDays, boolean isParallelUpgrade) throws ClassNotFoundException, SQLException, ParseException, InterruptedException {
		LOGGER.info("Service Termination in Geneva Starts service id: {} " ,serviceCode );
		Map<String, String> atMap = new HashMap<>();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
		String groupId = NetworkConstants.OPT_TERM+System.currentTimeMillis();
		Map<String, String> scComponentAttributesAMap=	commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("customerRef","billStartDate","terminationFlowTriggered","contractEndDate"), scServiceDetail.get().getId(), "LM", "A");
		/*String customerRef = StringUtils.trimToEmpty(scComponentAttributesAMap.getOrDefault("customerRef","NA"));
		if(customerRef.equals("NA")) {
			customerRef=loadCustomerDetails.getCrnNumber(scOrder.getErfCustLeId());
		}*/
		String terminationDate="";
		String scenarioType = "";
		String billStartDate = StringUtils.trimToEmpty(scComponentAttributesAMap.get("billStartDate"));
		String terminationTriggered = scComponentAttributesAMap.get("terminationFlowTriggered");
		boolean isTerminated = false;
		if (terminationTriggered != null && "Yes".equals(terminationTriggered)) {
			ScChargeLineitem chargeLineitem = chargeLineitemRepository.findFirstByServiceTermination(serviceId,
					serviceType);
			if (chargeLineitem != null) {
				isTerminated = true;
				String contractEndDate = scComponentAttributesAMap.get("contractEndDate");
				terminationDate = TimeStampUtil.formatWithTimeStampForCommPlusDaysLR(chargeLineitem.getTermDate(), 0);
				String etcCharge = chargeLineitem.getEtcCharge();
				scenarioType = "TERMINATE#|#ETC Charges=" + etcCharge
						+ "#|#ETC WAIVE Flag=N#|#ETC WAIVE TYPE=#|#System Generated ETC=#|#Contract End Date="
						+ TimeStampUtil.formatTerminationDateForLR(contractEndDate) + "#|#";
			}

		} else {
			if (isParallelUpgrade && parallelDays > 0) {
				terminationDate = TimeStampUtil.formatWithTimeStampForCommPlusDaysLR(billStartDate, parallelDays);
				scenarioType = NetworkConstants.TERM_SCENARIO_TYPE.concat(terminationDate).concat("#|#");
				LOGGER.info("Termination Date is {}", terminationDate);
			} else {
				terminationDate = TimeStampUtil.formatWithTimeStampForCommMinusDaysForLR(billStartDate, 1);
				scenarioType = NetworkConstants.TERM_SCENARIO_TYPE.concat(terminationDate).concat("#|#");
				LOGGER.info("Termination Date is {}", terminationDate);
			}
		}

		LOGGER.info("input group id for termination {}" ,groupId);
		String gnvOrder = gnvOrderEntryTabRepository.findCustomerRef(serviceCode);
		String custRef = "";
		if(gnvOrder!=null) {
			custRef = gnvOrder;
		}
		
		if (NetworkConstants.IAS.equals(serviceType)) {
			String eventSrc = gnvOrderEntryTabRepository.getEventSource(serviceCode);
			if (StringUtils.isNotEmpty(eventSrc)) {
				atMap.put("isBurstableProductTerminated", "Yes");
				LOGGER.info("Updating Component attributes for group id: {}" ,groupId);
				componentAndAttributeService.updateAttributes(Integer.parseInt(serviceId), atMap,AttributeConstants.COMPONENT_LM, "A");
				LOGGER.info("Updating Component attributes completed for group id: {}" ,groupId);
			}
		}

		Integer count = Integer.parseInt(gnvOrderEntryTabRepository.checkRecord(serviceCode));
		if(count!=null && count==0) {
			LOGGER.info("Insertion to Geneva Starts for group {}" ,groupId);
			GnvOrderEntryTab gnvOrderEntryTab = new GnvOrderEntryTab();
			gnvOrderEntryTab.setInputGroupId(groupId);
			gnvOrderEntryTab.setSourceSystem(NetworkConstants.LRIND);
			gnvOrderEntryTab.setRequestType(NetworkConstants.SERVICE);
			gnvOrderEntryTab.setActionType(NetworkConstants.TERMINATE);
			gnvOrderEntryTab.setICustomerRef(custRef);
			gnvOrderEntryTab.setIRefundBoo(NetworkConstants.T);
			gnvOrderEntryTab.setIScenarioType(scenarioType);
			gnvOrderEntryTab.setIInflight(NetworkConstants.N);
			gnvOrderEntryTab.setITermProposedDate(terminationDate);
			gnvOrderEntryTab.setITermReasonId( isTerminated ? NetworkConstants.SERVICE_TERMINATION_REASON : NetworkConstants.OPTIMUS_MIGRATED);
			gnvOrderEntryTab.setITermCharges(new BigDecimal(0));
			gnvOrderEntryTab.setIDepositRefundBoo(NetworkConstants.F);
			gnvOrderEntryTab.setIUserName("EAI");
			gnvOrderEntryTab.setIServiceId(serviceCode);
			gnvOrderEntryTabRepository.save(gnvOrderEntryTab);
			LOGGER.info("Insertion to Geneva Ends for group {}" ,groupId);
		
		
			ServicehandoverAudit audit = new ServicehandoverAudit();
			audit.setLegalEntity(scOrder.getErfCustLeName());
			audit.setCreatedDate(new Date());
			audit.setCrnId(custRef);
			audit.setCustomerType(NetworkConstants.DOMESTIC_CUSTOMER);
			audit.setRequest(gnvOrderEntryTab.toString());
			audit.setRequestType(NetworkConstants.SERVICE_TERMINATION_GENEVA);
			audit.setOrderId(scOrder.getOpOrderCode());
			audit.setStatus(NetworkConstants.SUCCESS);
			audit.setOpportunityId(scOrder.getTpsSfdcOptyId());
			audit.setGenevaGrpId(groupId);
			audit.setProviderSegment(NetworkConstants.PROVIDER_SEGMENT);
			audit.setServiceId(serviceId);
			audit.setServiceCode(serviceCode);
			audit.setProcessInstanceId(processInstanceId);
			audit.setAccountNumber("");
			audit.setServiceType(serviceType);
			servicehandoverAuditRepo.save(audit);
			return NetworkConstants.SUCCESS;
		}else {
			LOGGER.info("Service is already terminated in Geneva {}" ,groupId);
		}
		LOGGER.info("Service Termination in Geneva Completed service id: {} " ,serviceCode );
		return NetworkConstants.FAILURE;
	}
	
	
	
	@Transactional(isolation = Isolation.DEFAULT)
	public RequestResponse serviceTerminationLR(String orderId, String processInstanceId, String serviceCode,
			String serviceType, String serviceId) throws ClassNotFoundException, SQLException, ParseException, InterruptedException {
		
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		ServicehandoverAudit servicehandoverAudit = servicehandoverAuditRepo.findByRequestTypeAndServiceCode(NetworkConstants.SERVICE_TERMINATION_GENEVA, serviceId);
		Request request = new Request();
		ServicehandoverAudit audit = new ServicehandoverAudit();
		if(servicehandoverAudit!=null) {
			audit.setLegalEntity(scOrder.getErfCustLeName());
			audit.setCreatedDate(new Date());
			audit.setCrnId(servicehandoverAudit.getCrnId());
			audit.setCustomerType(NetworkConstants.DOMESTIC_CUSTOMER);
			audit.setRequestType(NetworkConstants.SERVICE_TERMINATION_LR);
			audit.setOrderId(scOrder.getOpOrderCode());
			audit.setOpportunityId(scOrder.getTpsSfdcOptyId()); 
			audit.setGenevaGrpId(servicehandoverAudit.getGenevaGrpId());
			audit.setProviderSegment(NetworkConstants.PROVIDER_SEGMENT);
			audit.setServiceId(serviceId);
			audit.setServiceCode(serviceCode);
			audit.setProcessInstanceId(processInstanceId);
			audit.setServiceType(serviceType);
			//JaxbMarshallerUtil.jaxbObjectToXML(request, audit);
			//servicehandoverAuditRepo.save(audit);
			
			request = new Request();
			OptimusTerminationRequestBO optimusTerminationRequestBO = new OptimusTerminationRequestBO();
			optimusTerminationRequestBO.setFlag("");
			optimusTerminationRequestBO.setGroupId(servicehandoverAudit.getGenevaGrpId());
			optimusTerminationRequestBO.setServiceId(serviceCode);
			request.setOptimusTerminationRequest(optimusTerminationRequestBO);
			
			
		}
		RequestResponse requestResponse = null;
		Integer count = Integer.parseInt(gnvOrderEntryTabRepository.checkRecord(serviceCode));
		if (count != null && count == 0) {
			requestResponse = (RequestResponse) terminateSoapConnector.callWebService(terminateServiceOperation,
					request);

			if (Objects.nonNull(requestResponse)) {
				status = requestResponse.getOptimusTerminationResponse().getStatus();
				if ("Geneva Failure".equals(status) || status.contains("F")) {
					status = NetworkConstants.FAILURE;
					audit.setStatus(status);
					audit.setErroMsg(requestResponse.getOptimusTerminationResponse().getErrorMsg());
					requestResponse.getOptimusTerminationResponse().setStatus(status);
				} else {
					status = NetworkConstants.SUCCESS;
					audit.setStatus(status);
					requestResponse.getOptimusTerminationResponse().setStatus(status);
				}
				LOGGER.info("Service Termination completed for service {} with status {}", serviceCode, status);
				return requestResponse;
			} else {
				requestResponse = new RequestResponse();
				requestResponse.getOptimusTerminationResponse().setStatus(NetworkConstants.FAILURE);
			}
		}else {
			LOGGER.info("Service is already terminated in LR {}",serviceCode);
			requestResponse = new RequestResponse();
			requestResponse.setOptimusTerminationResponse(new OptimusTerminationResponseBO());
			requestResponse.getOptimusTerminationResponse().setStatus(NetworkConstants.FAILURE);
		}
		servicehandoverAuditRepo.save(audit);
		LOGGER.info("Service Termination completed for service {} with status {}", serviceCode , status);
		return requestResponse;
	}
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public CreateOrderResponse demoProductTermination(String orderId, String processInstanceId, String serviceCode,
			String serviceType, String serviceId) {
		LOGGER.info("Product Termination invoked for Demo orderId {} and Service type {} ", orderId, serviceType);
		CreateOrder createOrder = new CreateOrder();
		CreateOrderBO createOrderBO = new CreateOrderBO();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderId, "Y");
		LOGGER.info("service ID: {} service type={}", serviceCode, serviceType);
		ScServiceDetail serviceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId)).get();
		if (serviceDetail != null) {
			String groupId = NetworkConstants.OPT_PROD.concat(serviceDetail.getScOrderUuid())+ "_"+ System.currentTimeMillis() + "_T";
			List<ScChargeLineitem> chargeLineitems = chargeLineitemRepository
					.findByServiceIdAndServiceType(serviceDetail.getId().toString(), serviceType);
			chargeLineitems.forEach(chargeLineitem -> {
				NetworkUniqueProductCreation uniqueProductCreation = new NetworkUniqueProductCreation();
				uniqueProductCreation.setProductName(chargeLineitem.getChargeLineitem());
				uniqueProductCreation.setAccountId(chargeLineitem.getAccountNumber());
				uniqueProductCreation.setMrc(Float.parseFloat(chargeLineitem.getMrc()));
				uniqueProductCreation.setNrc(Float.parseFloat(chargeLineitem.getNrc()));
				uniqueProductCreation.setArc(Float.parseFloat(chargeLineitem.getArc()));
				uniqueProductCreation.setScOrder(scOrder);
				uniqueProductCreation.setScServiceDetail(serviceDetail);
				uniqueProductCreation.setProcessInstanceId(processInstanceId);
				uniqueProductCreation.setActionType(NetworkConstants.TERMINATE);
				uniqueProductCreation.setOrderType(NetworkConstants.MODIFY);
				uniqueProductCreation.setChangeOrderType(Constants.TERMINATE_ORDER);
				uniqueProductCreation.setQuantity(NetworkConstants.ONE);
				uniqueProductCreation.setServiceType(chargeLineitem.getServiceType());
				uniqueProductCreation.setComponent(chargeLineitem.getComponent());
				uniqueProductCreation.setCpeModel(StringUtils.trimToEmpty(chargeLineitem.getCpeModel()));
				uniqueProductCreation.setHsnCode(chargeLineitem.getHsnCode());
				uniqueProductCreation.setIsMacd(true);
				uniqueProductCreation.setInputGroupId(groupId);
				uniqueProductCreation.setCustomerRef(chargeLineitem.getCustomerRef());
				if (NetworkConstants.NPL.equals(serviceDetail.getErfPrdCatalogProductName())) {
					uniqueProductCreation.setSiteType(chargeLineitem.getSiteType());
					uniqueProductCreation.setServiceType(NetworkConstants.ACC_NPL_INTRACITY);
				}
				if(chargeLineitem.getSourceProdSequence()!=null) {
					uniqueProductCreation.setSourceProductSeq(chargeLineitem.getSourceProdSequence());
				}else {
					uniqueProductCreation.setSourceProductSeq(gnvOrderEntryTabRepository
							.findSourceProdSeq(chargeLineitem.getAccountNumber(), chargeLineitem.getChargeLineitem()));
				}
				uniqueProductCreation.setBillingMethod(chargeLineitem.getBillingMethod());
				uniqueProductCreation.setBillingType(chargeLineitem.getBillingType());
				uniqueProductCreation.setMacdServiceId(serviceId);
				chargeLineitem.setActionType(NetworkConstants.TERMINATE);
				chargeLineitemRepository.save(chargeLineitem);
				chargeLineitemRepository.flush();
				uniqueProductCreation.setServicehandoverAudit(new ServicehandoverAudit());
				CreateOrderRequestBO createOrderRequestBO = billingProductCreationService.createUniqueProduct(uniqueProductCreation);
				createOrderBO.getReqOrder().add(createOrderRequestBO);
			});
		}
		
		createOrder.setCreateOrderRequestInput(createOrderBO);
		JaxbMarshallerUtil.jaxbObjectToXML(createOrder, new ServicehandoverAudit());
		CreateOrderResponse createOrderResponse = (CreateOrderResponse) orderSoapConnector
				.callWebService(createOrderOperation, createOrder);
		if (Objects.nonNull(createOrderResponse)) {
			status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
			return createOrderResponse;
		}
		LOGGER.info("Product Termination completed for orderId {} with status {}", orderId, status);
		return new CreateOrderResponse();
	}
	
}
