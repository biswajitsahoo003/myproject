package com.tcl.dias.serviceactivation.cramer.getipserviceasync;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.RfService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.tcl.dias.common.serviceinventory.bean.OptimusRfDataBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.constants.CramerConstants;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.AssignDummyWANIPResponse;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.CramerIPServiceRequestFailure;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.CramerServiceErrorDetails;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.GetIPServiceInfoResponse;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.ReleaseDummyWANIPResponse;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.SetCLRInfoResponse;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans.SubmitCramerIPServiceFailureResponse;
import com.tcl.dias.serviceactivation.entity.entities.Bgp;
import com.tcl.dias.serviceactivation.entity.entities.InterfaceProtocolMapping;
import com.tcl.dias.serviceactivation.entity.entities.IpDummyDetail;
import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.entities.VpnMetatData;
import com.tcl.dias.serviceactivation.entity.entities.Vrf;
import com.tcl.dias.serviceactivation.entity.repository.BgpRepository;
import com.tcl.dias.serviceactivation.entity.repository.InterfaceProtocolMappingRepository;
import com.tcl.dias.serviceactivation.entity.repository.IpDummyDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstCambiumDetailsRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstP2PDetailsRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstRadwinDetailsRepository;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.rule.engine.service.RuleEngineService;
import com.tcl.dias.serviceactivation.service.ServiceActivationService;
import com.tcl.dias.servicefulfillment.entity.entities.MstStatus;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServiceStatusDetails;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceStatusDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskDataRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorBean;
import com.tcl.dias.servicefulfillmentutils.beans.ErrorDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Endpoint
public class GetIpServiceEndPoint {

    private static final Logger logger = LoggerFactory.getLogger(GetIpServiceEndPoint.class);

    @Autowired
    MQUtils mqUtils;

    @Value("${queue.ipserviceasync}")
    String ipServiceAsyncQueue;
    
    @Value("${queue.rfinventory}")
   	String rfInventoryHandoverQueue;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    ServiceActivationService serviceActivationService;    

	@Autowired
	RuleEngineService ruleEngineService;
	
	@Autowired
	NetworkInventoryRepository networkInventoryRepository;
	
    @Autowired
    ComponentAndAttributeService componentAndAttributeService;
    
    @Autowired
    ScOrderRepository scOrderRepository;
    
    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;
    
    @Autowired
    ScServiceAttributeRepository scServiceAttributeRepository;
    
    @Autowired
    ScComponentRepository scComponentRepository;
    
    @Autowired
	ServiceDetailRepository serviceDetailRepository;
    
    @Autowired
   	IpDummyDetailRepository ipDummyDetailRepository;
    
    @Autowired
	WorkFlowService workFlowService;
    
    @Autowired
    TaskRepository taskRepository;

	@Autowired
	TaskDataRepository taskDataRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	

	@Autowired
	MstRadwinDetailsRepository mstRadwinDetailsRepository;

	@Autowired
	MstCambiumDetailsRepository mstCambiumDetailsRepository;
	
	@Autowired
	MstP2PDetailsRepository mstP2PDetailsRepository;
	
	@Autowired
	InterfaceProtocolMappingRepository interfaceProtocolMappingRepository;
	
	@Autowired
	BgpRepository bgpRepository;
	
	@Autowired
	TaskCacheService taskCacheService;
	
	@Autowired
	ServiceStatusDetailsRepository serviceStatusDetailsRepository;
	
	@Autowired
	private UserInfoUtils userInfoUtils;

	@Value("${rabbitmq.o2c.si.terminate.queue}")
	private String siTerminateDetail;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	RfService rfService;

	

    @PayloadRoot(namespace = "http://www.tcl.com/2012/09/csvc/inf", localPart = "getIPServiceInfoResponse")
    @ResponsePayload
   	public void processIPServiceDetailsRequest(@RequestPayload GetIPServiceInfoResponse request)
			throws TclCommonException {
		logger.info("IPServiceAsyncRequest received input request {} ", request);

		String errorMessage = "";
		String errorCode = "";

		NetworkInventory networkInventory = new NetworkInventory();
		String serviceId = "";
		if (null != request.getService() && null != request.getService().getService()
				&& null != request.getService().getService().getIasService()
						& null != request.getService().getService().getIasService().getServiceID()) {
			logger.info("Service Id Exists");
			serviceId = request.getService().getService().getIasService().getServiceID();
		} else {
			logger.info("Service Id doesn't Exists");
			serviceId = "NA";
		}

		try {

			if (request != null && request.getHeader() != null && request.getHeader().getRequestID() != null) {
				String processInstanceId = request.getHeader().getRequestID();

				String instance[] = processInstanceId.split("#");
				processInstanceId = instance[1];
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					logger.error("Exception in processIPServiceDetailsRequestthreadwait", e);
				}
				Execution execution = runtimeService.createExecutionQuery().processInstanceId(instance[1])
						.activityId("get-ip-service-info-async").singleResult();
				if (execution != null) {
					networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
					networkInventory.setServiceCode(serviceId);
					networkInventory.setType(CramerConstants.GET_IP_ASYNC);
					networkInventory.setRequestId(processInstanceId);

					try {
						String serviceCode = serviceActivationService.processCramerResponse(request);
						if (StringUtils.isNotBlank(serviceCode)) {
							logger.info("Service Activation - RuleEngine - started");
							ruleEngineService.applyRule(serviceCode);
							runtimeService.setVariable(execution.getId(), "getIpServiceSuccess", true);
							// MACD Changes
							ServiceDetail serviceDetails = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceCode,TaskStatusConstants.ISSUED);
							String orderCategory=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderCategory(serviceDetails, serviceDetails.getOrderDetail());
							String orderSubCategory=serviceDetails.getOrderSubCategory();
							orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
							orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
							if(serviceDetails!=null && orderCategory!=null 
									&& serviceDetails.getServiceOrderType().equalsIgnoreCase("GVPN") && "ADD_SITE".equalsIgnoreCase(orderCategory)){
								logger.info("MACD ADDSITE for GVPN::{}",serviceDetails.getId());
								Optional<ScServiceDetail> scServiceDetailOptional=scServiceDetailRepository.findById(serviceDetails.getScServiceDetailId());
								if(scServiceDetailOptional.isPresent() && scServiceDetailOptional.get().getParentUuid()!=null){
									logger.info("MACD ADDSITE for GVPN::{}",serviceDetails.getId());
									ServiceDetail parentServiceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(scServiceDetailOptional.get().getParentUuid(),TaskStatusConstants.ACTIVE);
									ScComponentAttribute siteTypeAttribute=scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetails.getScServiceDetailId(),"siteType","LM", "A");
									if(parentServiceDetail!=null && siteTypeAttribute!=null && siteTypeAttribute.getAttributeValue()!=null && !siteTypeAttribute.getAttributeValue().isEmpty()) {
										if (Objects.nonNull(parentServiceDetail.getVpnMetatDatas())
												&& !parentServiceDetail.getVpnMetatDatas().isEmpty()) {
											logger.info("MACD ADDSITE for GVPN parentServiceDetail VpnMetaData exists for Service Id::{}",serviceDetails.getId());
											Optional<VpnMetatData> parentVpnMetatDataOptional = parentServiceDetail.getVpnMetatDatas()
													.stream().findFirst();
											if (parentVpnMetatDataOptional.isPresent()) {
												VpnMetatData parentVpnMetatData = parentVpnMetatDataOptional.get();
												logger.info("MACD ADDSITE for GVPN parentVpnMetatData exists::{} with current SiteTupe::{}",parentVpnMetatData.getId(),siteTypeAttribute.getAttributeValue());
												if(parentVpnMetatData.getL2OTopology().toLowerCase().contains("spoke") 
														&& siteTypeAttribute.getAttributeValue().toLowerCase().contains("spoke")) {
													logger.info("MACD ADDSITE for Prev and Current are spoke for service id::{}",serviceDetails.getId());
													runtimeService.setVariable(execution.getId(), "confirmOrderRequired", false);
												}else if(parentVpnMetatData.getL2oSiteRole().toLowerCase().contains("mesh") 
														&& siteTypeAttribute.getAttributeValue().toLowerCase().contains("mesh")) {
													logger.info("MACD ADDSITE for Prev and Current are mesh for service id::{}",serviceDetails.getId());
													runtimeService.setVariable(execution.getId(), "confirmOrderRequired", false);
												}else {
													logger.info("MACD ADDSITE for Prev and Current not mached for service id::{}",serviceDetails.getId());
													runtimeService.setVariable(execution.getId(), "confirmOrderRequired", true);
												}
											}
										}
									}
										
								}
							}
							if(serviceDetails!=null && (serviceDetails.getServiceOrderType().equalsIgnoreCase("GVPN")
									|| serviceDetails.getServiceOrderType().equalsIgnoreCase("IAS")
									|| serviceDetails.getServiceOrderType().equalsIgnoreCase("ILL")) && 
									("ADD_SITE".equalsIgnoreCase(orderCategory)
											|| "NEW".equalsIgnoreCase(serviceDetails.getOrderType()))) {
								logger.info("NEW or ADDSITE for service id::{}",serviceDetails.getId());
								List<InterfaceProtocolMapping> interfaceProtocolMappings = serviceDetails
										.getInterfaceProtocolMappings().stream()
										.filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
										.collect(Collectors.toList());
								if (!interfaceProtocolMappings.isEmpty()) {
									logger.info("InterfaceProtocolMappings exists for service id::{}",serviceDetails.getId());
									interfaceProtocolMappings.sort(
											Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
									InterfaceProtocolMapping interfaceProtocolMapping = interfaceProtocolMappings
											.get(0);
									if (interfaceProtocolMapping != null) {
										try {
											logger.info("InterfaceProtocolMapping id exists::{}",interfaceProtocolMapping.getInterfaceProtocolMappingId());
											if (interfaceProtocolMapping.getEthernetInterface() != null && (interfaceProtocolMapping.getEthernetInterface().getModifiedIpv4Address()==null
													|| interfaceProtocolMapping.getEthernetInterface().getModifiedIpv4Address().isEmpty())) {
												logger.info("Modified Ipv4 Address not exists for NEW or ADDSITE of InterfaceProtocolMapping Id::{}",interfaceProtocolMapping.getInterfaceProtocolMappingId());
												runtimeService.setVariable(execution.getId(), "confirmOrderRequired", true);
											}
										}catch(Exception ee) {
											logger.error("Exception in confirmOrderRequired flag",ee);
										}
									}
								}
							}
							if(Objects.nonNull(serviceDetails) && Objects.nonNull(serviceDetails.getOrderDetail())){
								if(((serviceDetails.getServiceOrderType().equals("ILL_MACD") 
										|| serviceDetails.getServiceOrderType().equals("GVPN_MACD") 
										|| serviceDetails.getServiceOrderType().equals("IAS_MACD")) 
										&& ((Objects.nonNull(serviceDetails.getOrderCategory()) 
										&& serviceDetails.getOrderCategory().equals("CHANGE_BANDWIDTH"))
										|| (Objects.nonNull(serviceDetails.getOrderSubCategory()) 
												&& (serviceDetails.getOrderSubCategory().toLowerCase().contains("lm")
														|| serviceDetails.getOrderSubCategory().toLowerCase().contains("bso")
														|| serviceDetails.getOrderSubCategory().equalsIgnoreCase("Shifting")))))
										|| (serviceDetails.getOrderDetail().getExtOrderrefId()!=null 
											&& serviceDetails.getOrderDetail().getExtOrderrefId().toLowerCase().contains("izosdwan")
												&& serviceDetails.getOrderCategory()!=null && 
												serviceDetails.getOrderCategory().equalsIgnoreCase("CHANGE_ORDER")
												&& serviceDetails.getOrderSubCategory()==null)){
										logger.info("MACD ChangeBandwidth or IzoSdwan::{}",serviceDetails.getIsportChanged());
										runtimeService.setVariable(execution.getId(), "isPortChanged",serviceDetails.getIsportChanged()==1?true:false);
										runtimeService.setVariable(execution.getId(), "isDownTimeRequired",serviceDetails.getIsdowntimeReqd()==1?true:false);
										runtimeService.setVariable(execution.getId(), "skipDummyConfig",serviceDetails.getSkipDummyConfig()==1?true:false);
										
										if(Objects.nonNull(serviceDetails.getLastmileType()) && !serviceDetails.getLastmileType().isEmpty() && (serviceDetails.getLastmileType().equalsIgnoreCase("OnnetRf") || serviceDetails.getLastmileType().equalsIgnoreCase("Onnet Wireless"))){
											logger.info("OnnetRF MACD");
											ScServiceAttribute feasibilitySolutionTypeAttributes = scServiceAttributeRepository
													.findFirstByScServiceDetail_idAndAttributeNameAndCategory(serviceDetails.getScServiceDetailId(), "closest_provider_bso_name",
															"FEASIBILITY");
											if(Objects.nonNull(feasibilitySolutionTypeAttributes) && Objects.nonNull(feasibilitySolutionTypeAttributes.getAttributeValue())
													&& !feasibilitySolutionTypeAttributes.getAttributeValue().isEmpty() && !feasibilitySolutionTypeAttributes.getAttributeValue().toLowerCase().contains("pmp")){
												logger.info("P2P");
												ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceId,"INPROGRESS");
												serviceActivationService.addComponentAttr(scServiceDetail,serviceDetails,serviceId);
											}
										}
								}
							}
							
							if (serviceDetails != null && serviceDetails.getScServiceDetailId() != null) {
								logger.info("Service Activation Id::{} and Service Fulfillment Id::{}",
										serviceDetails.getId(), serviceDetails.getScServiceDetailId());
								Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailRepository
										.findById(serviceDetails.getScServiceDetailId());
								if (scServiceDetailOptional.isPresent()) {
									logger.info("Sc Service Detail Exists");
									ScServiceDetail scServiceDetail = scServiceDetailOptional.get();

									ScOrder scOrder=scOrderRepository.findByOpOrderCodeAndIsActive(scServiceDetail.getScOrderUuid(),"Y");
									String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);

									if ((scOrder != null && (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("NEW"))
											&& scOrder.getOpOrderCode()!=null && scOrder.getOpOrderCode().contains("IAS"))
											&& serviceDetails.getVrfs() != null
											&& !serviceDetails.getVrfs().isEmpty()) {
										logger.info("IAS Vrf detail exists::{}",scOrder.getId());
										Optional<Vrf> vrfOptional = serviceDetails.getVrfs().stream().findFirst();
										if (vrfOptional.isPresent()) {
											logger.info("IAS Vrf exists");
											Vrf vrf = vrfOptional.get();
											if (vrf.getVrfName()!=null && !vrf.getVrfName().isEmpty() && !"NOT_APPLICABLE".equalsIgnoreCase(vrf.getVrfName())) {
												logger.info("ConfirmOrder not required for IAS Vrf::{}",vrf.getVrfId());
												runtimeService.setVariable(execution.getId(), "confirmOrderRequired",false); // Allow Zero Touch
												if(scServiceDetail.getIsAmended()!=null && scServiceDetail.getIsAmended().equalsIgnoreCase("Y")) {
													runtimeService.setVariable(execution.getId(), "confirmOrderRequired",true); // Allow Zero Touch

												}
											}else if(serviceDetails.getRouterMake()!=null && !serviceDetails.getRouterMake().isEmpty() 
													&& "JUNIPER".equalsIgnoreCase(serviceDetails.getRouterMake())
													&& vrf.getVrfName()!=null && !vrf.getVrfName().isEmpty() && "NOT_APPLICABLE".equalsIgnoreCase(vrf.getVrfName())
													&& serviceDetails.getInterfaceProtocolMappings()!=null){
												logger.info("ConfirmOrder required for IAS Juniper device with Vrf::{}",vrf.getVrfId());
												
												boolean isPublicAsNumberExists=false;
												List<InterfaceProtocolMapping> routerInterfaceProtocolMappings = serviceDetails.getInterfaceProtocolMappings()
														.stream().filter(ser -> ser.getCpe() == null && ser.getEndDate() == null)
														.collect(Collectors.toList());
												logger.info("routerInterfaceProtocolMappings for::{},=>{}",serviceDetails.getId(),routerInterfaceProtocolMappings);
												if (routerInterfaceProtocolMappings!=null && !routerInterfaceProtocolMappings.isEmpty()) {
													logger.info("Router Protocol not empty");
													routerInterfaceProtocolMappings
															.sort(Comparator.comparing(InterfaceProtocolMapping::getVersion).reversed());
													InterfaceProtocolMapping routerInterfaceProtocolMapping = routerInterfaceProtocolMappings
															.get(0);
													logger.info("Router Protocol Id::{}",routerInterfaceProtocolMapping.getInterfaceProtocolMappingId());
													Integer bgpId=interfaceProtocolMappingRepository.findByInterfaceProtocolMappingId(routerInterfaceProtocolMapping.getInterfaceProtocolMappingId());
													if(bgpId!=null){
														logger.info("Bgp Id::{}",bgpId);
														Optional<Bgp> bgpOptional=bgpRepository.findById(bgpId);
														if(bgpOptional.isPresent()){
															logger.info("Bgp exists");
															Bgp bgp=bgpOptional.get();
															if (Objects.nonNull(bgp) && Objects
																	.nonNull(bgp.getRemoteAsNumber())) {
																logger.info("Bgp Remote AsNumber exists");
																if ((bgp.getRemoteAsNumber() >= 64512 
																		&& bgp.getRemoteAsNumber() <= 65535)) {
																	logger.info("Bgp Remote AsNumber condition met");
																} else {
																	logger.info("Bgp Remote AsNumber condition not met");
																	isPublicAsNumberExists = true;
																}
															}
														}
													}
												}
												if(isPublicAsNumberExists){
													logger.info("Public As Number");
													runtimeService.setVariable(execution.getId(), "confirmOrderRequired",true); // Don't Allow Zero Touch
												}else{
													logger.info("Private As Number");
													runtimeService.setVariable(execution.getId(), "confirmOrderRequired",false);
													if(scServiceDetail.getIsAmended()!=null && scServiceDetail.getIsAmended().equalsIgnoreCase("Y")) {
														runtimeService.setVariable(execution.getId(), "confirmOrderRequired",true); // Allow Zero Touch

													}
												}
											}else if(vrf.getVrfName()!=null && !vrf.getVrfName().isEmpty() && "NOT_APPLICABLE".equalsIgnoreCase(vrf.getVrfName())){
												logger.info("ConfirmOrder required for IAS Vrf::{}",vrf.getVrfId());
												runtimeService.setVariable(execution.getId(), "confirmOrderRequired",true); // Don't Allow Zero Touch
											}
										}
									}
								}
							}
							if(serviceDetails!=null && (serviceDetails.getServiceOrderType().equalsIgnoreCase("IAS") || serviceDetails.getServiceOrderType().equalsIgnoreCase("ILL")) && 
									("ECO INTERNET".equalsIgnoreCase(serviceDetails.getServiceSubtype()))) {
								logger.info("Econet Service Id::{}",serviceDetails.getId());
								runtimeService.setVariable(execution.getId(), "confirmOrderRequired", true);
							}
							if((Objects.nonNull(serviceDetails) && Objects.nonNull(serviceDetails.getOrderDetail()))
									&& (("ADD_SITE".equals(serviceDetails.getOrderCategory()))
									|| (serviceDetails.getServiceOrderType().equalsIgnoreCase("ILL")
									|| serviceDetails.getServiceOrderType().equalsIgnoreCase("GVPN")
									|| serviceDetails.getServiceOrderType().equalsIgnoreCase("IAS")))) {
								logger.info("Confirm Order- Push to RF Inventory");
								boolean migrateToInventoryflag = true;
								OptimusRfDataBean optimusRfDataBean = serviceActivationService.confirmOrderRFtoInv(serviceCode, migrateToInventoryflag, "CMIP");
							}

							if((Objects.nonNull(serviceDetails) && Objects.nonNull(serviceDetails.getOrderDetail()))
									&& (!("ADD_SITE".equals(serviceDetails.getOrderCategory()))
									|| (serviceDetails.getServiceOrderType().equalsIgnoreCase("ILL_MACD")
									|| serviceDetails.getServiceOrderType().equalsIgnoreCase("GVPN_MACD")
									|| serviceDetails.getServiceOrderType().equalsIgnoreCase("IAS_MACD")))) {
								logger.info("Confirm Order- Push MACD to RF Inventory");
								boolean migrateToInventoryflag = true;
								OptimusRfDataBean optimusRfDataBean = serviceActivationService.confirmOrderMacdRFtoInv(serviceCode, migrateToInventoryflag, "CMIP");
							}

							logger.info("Service Activation - RuleEngine - completed");
							
						} else {
							logger.info("Service Activation - serviceCode is null - RuleEngine - skipped");
							runtimeService.setVariable(execution.getId(), "getIpServiceSuccess", true);
						}
					} catch (Exception e) {
						runtimeService.setVariable(execution.getId(), "getIpServiceSuccess", false);
						errorMessage = e.getMessage();
						errorCode = "500";
						logger.error("processIPServiceDetailsRequestException",e);

					}

					runtimeService.trigger(execution.getId());
				} else {
					logger.info("processIPServiceDetailsRequest Execution is null for {}", processInstanceId);
				}
			} else {
				logger.info(
						"processIPServiceDetailsRequest request id/process instance id is null or invalid to trigger flowable execution {} ",
						request);
			}

		} catch (Exception e) {
			networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			networkInventory.setServiceCode(serviceId);
			networkInventory.setType(CramerConstants.GET_IP_ASYNC);
			networkInventory.setRequestId("NAE");
			errorMessage = com.tcl.dias.servicefulfillmentutils.constants.CramerConstants.SYSTEM_ERROR;
			errorCode = "500";
			logger.error(e.getMessage(), e);
			logger.error("Exception in processIPServiceDetailsRequest {} ", request);
		}

		try {
			networkInventory.setRequest(Utils.convertObjectToXmlString(request, GetIPServiceInfoResponse.class));
			networkInventory.setResponse("VOID");
			networkInventoryRepository.save(networkInventory);
		} catch (Exception e) {
			logger.error("Exception in processIPServiceDetailsRequest", e);
		}

		updateErrorForIP(serviceId, errorMessage, errorCode);
	}
	
    
    @Transactional(readOnly=false,isolation=Isolation.READ_UNCOMMITTED)
    public void updateErrorForIP(String serviceId,String errorMessage,String errorCode) {
    	try {
			ServiceDetail serviceDetails = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceId,TaskStatusConstants.ISSUED);
			if(Objects.nonNull(serviceDetails)){
				ScOrder scOrder=scOrderRepository.findByOpOrderCodeAndIsActive(serviceDetails.getOrderDetail().getExtOrderrefId(), "Y");
				if(Objects.nonNull(scOrder)){
					ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndScOrder_id(serviceId,scOrder.getId());
					logger.info("processIPServiceDetailsRequest serviceId:{}", serviceId);

					if (scServiceDetail != null && StringUtils.isNotBlank(errorMessage)) {
						logger.info("getErrorMessageDetails condition meet for processIPServiceDetailsRequest");

						componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,
								"serviceDesignIpInfoCallFailureReason",
								componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
								AttributeConstants.ERROR_MESSAGE, "get-ip-service-info-async");
					}
				}
			}
			
		} catch (Exception e) {
			logger.info("processIPServiceDetailsRequest error message capture   {} ", e);
		}
    }


	


	@PayloadRoot(namespace = "http://www.tcl.com/2012/09/csvc/inf", localPart = "setCLRInfoResponse")
    @ResponsePayload
    public void processSetCLRInfoResponse(@RequestPayload SetCLRInfoResponse setCLRResponse)
            throws TclCommonException, IllegalArgumentException {
        logger.info("setCLRInfoResponse received input request {} ", setCLRResponse);
        

		logger.info("SetCLRResponse received input SetCLRResponse {} ", setCLRResponse);
		NetworkInventory networkInventory = new NetworkInventory();
		String serviceCode = "";
		String errorMessage = "";
		String errorCode = "";
		String type = "Set-clr-async";

		try {
			if (java.util.Objects.nonNull(setCLRResponse) && java.util.Objects.nonNull(setCLRResponse.getResponse())) {
				String processInstanceId = setCLRResponse.getHeader().getRequestID();
            	String[] instanceId=processInstanceId.split("#");
            	processInstanceId=instanceId[1];
            	String inputType=instanceId[2];
            	serviceCode=instanceId[3];
            	
            	Execution execution = null;
            	if(inputType!=null && inputType.equals("MFD")) {

				 execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
						.activityId("Set-mfd-clr-async").singleResult();
				 	type ="Set-mfd-clr-async";
            	}else if(inputType!=null && inputType.equals("TER")) {

   				 execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
   						.activityId("set-terminate-clr-async").singleResult();
   				 	type ="set-terminate-clr-async";
   				 	
   				 	Object terminationFromL2o=runtimeService.getVariable(execution.getId(), "terminationFromL20");
   				 	if(terminationFromL2o==null || !"yes".equalsIgnoreCase((String)terminationFromL2o)){
   						updateServiceDetailsToTerminate(serviceCode);

   				 	}
               	}else {
            		execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
    						.activityId("Set-clr-async").singleResult();
            	}
				
				//serviceCode = processInstanceId;
            	String status = setCLRResponse.getResponse().getStatus();
            	logger.info("SetCLRResponse processInstanceId={} inputType= {} serviceCode={} status= {} ", processInstanceId,inputType,serviceCode,status);
								
					networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
					networkInventory.setServiceCode(serviceCode);
					networkInventory.setType(type);
					networkInventory.setRequestId(processInstanceId);
					if(status!=null && "true".equalsIgnoreCase(status)) {
						if (execution != null) {	
							if(inputType!=null && inputType.equals("MFD")) {
								runtimeService.setVariable(execution.getId(), "setMfdCLRSuccess", true);
							}else if(inputType!=null && inputType.equals("TER")) {
								runtimeService.setVariable(execution.getId(), "setTerCLRSuccess", true);
							}else {
								runtimeService.setVariable(execution.getId(), "setCLRSuccess", true);
							}
							runtimeService.trigger(execution.getId());
						} else {
							logger.info("processSetClrAsyncRequest Execution is null for {} type={}", processInstanceId,type);
						}
					}
			} else {
				logger.info(
						"setClrAsyncEndPoint::processSetClrAsyncRequest id/process instance id is null or invalid to trigger flowable execution {} ",
						setCLRResponse);
			}
		} catch (Exception e) {
			networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			networkInventory.setServiceCode(serviceCode);
			networkInventory.setType(type);
			networkInventory.setRequestId("NAE");
			errorCode="500";
			errorMessage=com.tcl.dias.servicefulfillmentutils.constants.CramerConstants.SYSTEM_ERROR;
			logger.error(e.getMessage(), e);
		}

		try {
			networkInventory.setRequest(Utils.convertObjectToXmlString(setCLRResponse, SetCLRInfoResponse.class));
			networkInventory.setResponse("VOID");
			networkInventory.setServiceCode(serviceCode);
			networkInventoryRepository.save(networkInventory);
		} catch (Exception e) {
			logger.error("Exception in processGetClrAsyncRequest", e);

		}
		
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
		
    	logger.info("getErrorMessageDetails condition meet for SetClrAsyncEndpoint with serviceCode:{}",serviceCode);


		if (scServiceDetail != null && StringUtils.isNotBlank(errorMessage)) {
			try {
		    	logger.info("getErrorMessageDetails condition meet for SetClrAsyncEndpoint");

				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "serviceDesignCallFailureReason",
						componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
						AttributeConstants.ERROR_MESSAGE,type);
			} catch (Exception e) {
				logger.error("cancelServiceConfigurationDelegate getting error message details----------->{}", e);
			}
		}

	
    }
	


    @PayloadRoot(namespace = "http://www.tcl.com/2012/09/csvc/inf", localPart = "assignDummyWANIPResponse")
    @ResponsePayload
    public void processAssignDummyWanIpResponse(@RequestPayload AssignDummyWANIPResponse request){
        logger.info("assignDummyWANIPResponse received input request {} ", request);
        String errorMessage = "";
		String errorCode = "";

		NetworkInventory networkInventory = new NetworkInventory();
		String serviceId = "";
		if (null != request && null != request.getHeader() && null!=request.getHeader().getRequestID()) {
			logger.info("Header Exists");
			String requestID=request.getHeader().getRequestID();
			String[] requestSplit=requestID.split("#");
			if(requestSplit.length==3){
				serviceId = requestSplit[2];
			}else{
				logger.info("Service Id doesn't Exists from Request ID");
				serviceId = "NA";
			}
		} else {
			logger.info("Service Id doesn't Exists");
			serviceId = "NA";
		}
		
		try {
			if (request != null && request.getHeader() != null && request.getHeader().getRequestID() != null) {
				String processInstanceId = request.getHeader().getRequestID();
				String instance[] = processInstanceId.split("#");
				processInstanceId = instance[1];
				Execution execution = runtimeService.createExecutionQuery().processInstanceId(instance[1])
						.activityId("get-assign-dummy-wan-ip-async").singleResult();
				if (execution != null) {
					networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
					networkInventory.setServiceCode(serviceId);
					networkInventory.setType(CramerConstants.GET_ASSIGN_DUMMY_IP_ASYNC);
					networkInventory.setRequestId(processInstanceId);
					try {
						String status=serviceActivationService.processDummyCramerResponse(request,serviceId);
						if("SUCCESS".equalsIgnoreCase(status)){
							logger.info("Success");
							runtimeService.setVariable(execution.getId(), "getAssignDummyIpServiceSuccess", true);
						}else{
							runtimeService.setVariable(execution.getId(), "getAssignDummyIpServiceSuccess", false);
						}
					} catch (Exception e) {
						logger.error("Exception in processDummyCramerResponse", e);
						runtimeService.setVariable(execution.getId(), "getAssignDummyIpServiceSuccess", false);
						errorMessage = e.getMessage();
						errorCode = "500";
					}
					runtimeService.trigger(execution.getId());
				} else {
					logger.info("processAssignDummyWanIpResponse Execution is null for {}", processInstanceId);
				}
			} else {
				logger.info(
						"processAssignDummyWanIpResponse request id/process instance id is null or invalid to trigger flowable execution {} ",
						request);
			}

		} catch (Exception e) {
			networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			networkInventory.setServiceCode(serviceId);
			networkInventory.setType(CramerConstants.GET_ASSIGN_DUMMY_IP_ASYNC);
			networkInventory.setRequestId("NAE");
			errorMessage = com.tcl.dias.servicefulfillmentutils.constants.CramerConstants.SYSTEM_ERROR;
			errorCode = "500";
			logger.error(e.getMessage(), e);
			logger.error("Exception in processAssignDummyWanIpResponse {} ", request);
		}

		try {
			networkInventory.setRequest(Utils.convertObjectToXmlString(request, AssignDummyWANIPResponse.class));
			networkInventory.setResponse("VOID");
			networkInventoryRepository.save(networkInventory);
		} catch (Exception e) {
			logger.error("Exception in processAssignDummyWanIpResponse", e);
		}

		updateErrorDetails(serviceId, errorMessage, errorCode);
    }
    
    
    @Transactional(readOnly=false,isolation=Isolation.READ_UNCOMMITTED)
    public void updateErrorDetails(String serviceId,String errorMessage,String errorCode) {
    	try {
			ServiceDetail serviceDetails = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceId,TaskStatusConstants.ISSUED);
			if(Objects.nonNull(serviceDetails)){
				ScOrder scOrder=scOrderRepository.findByOpOrderCodeAndIsActive(serviceDetails.getOrderDetail().getExtOrderrefId(), "Y");
				if(Objects.nonNull(scOrder)){
					ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndScOrder_id(serviceId,scOrder.getId());
					logger.info("processAssignDummyWanIpResponse serviceId:{}", serviceId);

					if (scServiceDetail != null && StringUtils.isNotBlank(errorMessage)) {
						logger.info("getErrorMessageDetails condition meet for processAssignDummyWanIpResponse");

						componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,
								"serviceDesignIpInfoCallFailureReason",
								componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
								AttributeConstants.ERROR_MESSAGE, "get-assign-dummy-wan-ip-async");
					}
				}
			}
			
		} catch (Exception e) {
			logger.info("processAssignDummyWanIpResponse error message capture   {} ", e);
		}

    	
    }

    @PayloadRoot(namespace = "http://www.tcl.com/2012/09/csvc/inf", localPart = "releaseDummyWANIPResponse")
    @ResponsePayload
    public void processReleaseDummyWanIpResponse(@RequestPayload ReleaseDummyWANIPResponse request){
        logger.info("processReleaseDummyWanIpResponse received input request {} ", request);
        String errorMessage = "";
		String errorCode = "";

		NetworkInventory networkInventory = new NetworkInventory();
		String serviceId = "";
		if (null != request && null != request.getHeader() && null!=request.getHeader().getRequestID()) {
			logger.info("Header Exists");
			String requestID=request.getHeader().getRequestID();
			String[] requestSplit=requestID.split("#");
			if(requestSplit.length==3){
				serviceId = requestSplit[2];
			}else{
				logger.info("Service Id doesn't Exists from Request ID");
				serviceId = "NA";
			}
		} else {
			logger.info("Service Id doesn't Exists");
			serviceId = "NA";
		}
		
		try {
			if (request != null && request.getHeader() != null && request.getHeader().getRequestID() != null) {
				String processInstanceId = request.getHeader().getRequestID();
				String instance[] = processInstanceId.split("#");
				processInstanceId = instance[1];
				Execution execution = runtimeService.createExecutionQuery().processInstanceId(instance[1])
						.activityId("get-release-dummy-wan-ip-async").singleResult();
				if (execution != null) {
					networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
					networkInventory.setServiceCode(serviceId);
					networkInventory.setType(CramerConstants.GET_RELEASE_DUMMY_IP_ASYNC);
					networkInventory.setRequestId(processInstanceId);
					try {
						if(Objects.nonNull(request.getReleaseSuccess()) && "true".equalsIgnoreCase(request.getReleaseSuccess())){
							logger.info("Success");
							ServiceDetail serviceDetails = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceId,TaskStatusConstants.ISSUED);
							if(Objects.nonNull(serviceDetails)){
								logger.info("Service Detail exists::{}",serviceDetails.getId());
								IpDummyDetail ipDummyDetail=ipDummyDetailRepository.findFirstByServiceDetail_IdOrderByIdDesc(serviceDetails.getId());
								if(Objects.nonNull(ipDummyDetail)){
									logger.info("Ip Dummy Detail exists");
									ipDummyDetail.setEndDate(new Timestamp(new Date().getTime()));
									ipDummyDetail.setModifiedBy("OPTIMUS_RELEASE");
									ipDummyDetailRepository.save(ipDummyDetail);
								}
							}
							runtimeService.setVariable(execution.getId(), "getReleaseDummyIpServiceSuccess", true);
						}else{
							logger.info("Failure");
							runtimeService.setVariable(execution.getId(), "getReleaseDummyIpServiceSuccess", false);
						}
					} catch (Exception e) {
						logger.error("Exception in processReleaseDummyWanIpResponse", e);
						runtimeService.setVariable(execution.getId(), "getReleaseDummyIpServiceSuccess", false);
						errorMessage = e.getMessage();
						errorCode = "500";
					}
					runtimeService.trigger(execution.getId());
				} else {
					logger.info("processReleaseDummyWanIpResponse Execution is null for {}", processInstanceId);
				}
			} else {
				logger.info(
						"processReleaseDummyWanIpResponse request id/process instance id is null or invalid to trigger flowable execution {} ",
						request);
			}

		} catch (Exception e) {
			networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			networkInventory.setServiceCode(serviceId);
			networkInventory.setType(CramerConstants.GET_RELEASE_DUMMY_IP_ASYNC);
			networkInventory.setRequestId("NAE");
			errorMessage = com.tcl.dias.servicefulfillmentutils.constants.CramerConstants.SYSTEM_ERROR;
			errorCode = "500";
			logger.error(e.getMessage(), e);
			logger.error("Exception in processReleaseDummyWanIpResponse {} ", request);
		}

		try {
			networkInventory.setRequest(Utils.convertObjectToXmlString(request, ReleaseDummyWANIPResponse.class));
			networkInventory.setResponse("VOID");
			networkInventoryRepository.save(networkInventory);
		} catch (Exception e) {
			logger.error("Exception in processReleaseDummyWanIpResponse", e);
		}

		try {
			ServiceDetail serviceDetails = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceId,TaskStatusConstants.ISSUED);
			if(Objects.nonNull(serviceDetails)){
				ScOrder scOrder=scOrderRepository.findByOpOrderCodeAndIsActive(serviceDetails.getOrderDetail().getExtOrderrefId(), "Y");
				if(Objects.nonNull(scOrder)){
					ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndScOrder_id(serviceId,scOrder.getId());
					logger.info("processReleaseDummyWanIpResponse serviceId:{}", serviceId);

					if (scServiceDetail != null && StringUtils.isNotBlank(errorMessage)) {
						logger.info("getErrorMessageDetails condition meet for processReleaseDummyWanIpResponse");

						componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,
								"serviceDesignIpInfoCallFailureReason",
								componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
								AttributeConstants.ERROR_MESSAGE, "get-release-dummy-wan-ip-async");
					}
				}
			}
			
		} catch (Exception e) {
			logger.info("processReleaseDummyWanIpResponse error message capture   {} ", e);
		}
    }
    
   
    @PayloadRoot(namespace = "http://www.tcl.com/2012/09/csvc/inf", localPart = "submitCramerIPServiceFailureResponse")
    @ResponsePayload
    public void processSubmitCramerIPServiceFailureResponse(@RequestPayload SubmitCramerIPServiceFailureResponse submitCramerIPServiceFailureResponse)
            throws TclCommonException, IllegalArgumentException {
        logger.info("submitCramerIPServiceFailureResponse received input request {} ", submitCramerIPServiceFailureResponse);
        NetworkInventory networkInventory = new NetworkInventory();
        String type="";
        if (submitCramerIPServiceFailureResponse !=null && submitCramerIPServiceFailureResponse.getHeader() !=null 
        		&&  submitCramerIPServiceFailureResponse.getHeader().getRequestID() !=null) {
        	String request = submitCramerIPServiceFailureResponse.getHeader().getRequestID();
        	
        	String instance[] = request.split("#");
        	String processInstanceId = instance[1];
			        	
        	Execution execution =null;
        	
        	try {
				Thread.sleep(10000);
			} catch (Exception e) {
				logger.error("Exception in processIPServiceDetailsRequestthreadwait", e);
			}
        	
        	if(request!=null && request.contains("#MFD")) {        	
				 execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId)
						.activityId("Set-mfd-clr-async").singleResult();
				 	type ="Set-mfd-clr-async";
           	}else if(request!=null && request.contains("#TER")) {
  				 execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId)
  						.activityId("set-terminate-clr-async").singleResult();
  				 	type ="set-terminate-clr-async";

            }else if(request!=null && request.contains("#CLR")) {
 				 execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId)
 						.activityId("Set-clr-async").singleResult();
 				 	type ="Set-clr-async";
           }else {
           		execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId)
   						.activityId("get-ip-service-info-async").singleResult();
           		type = "get-ip-service-info-async";
           	}

        	networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        	networkInventory.setServiceCode(null!=submitCramerIPServiceFailureResponse.getRequest().getServiceID()?submitCramerIPServiceFailureResponse.getRequest().getServiceID():"NA");
        	networkInventory.setType(type);
        	networkInventory.setRequestId(processInstanceId);
        	if (execution != null) {
        		if(request!=null && request.contains("#MFD")) {  
					runtimeService.setVariable(execution.getId(), "setMfdCLRSuccess", false);
        		}else if(request!=null && request.contains("#TER")) {
					runtimeService.setVariable(execution.getId(), "setTerCLRSuccess", false);
				}else if(request!=null && request.contains("#CLR")) {
					runtimeService.setVariable(execution.getId(), "setCLRSuccess", false);
				}else {
					runtimeService.setVariable(execution.getId(), "getIpServiceSuccess", false);
				}
                runtimeService.trigger(execution.getId());                                       
            } else {
            	logger.info("processSubmitCramerIPServiceFailureResponse Execution is null for {}", processInstanceId);
            }
        } else {
            logger.info(
                    "GetIpServiceEndPoint::submitCramerIPServiceFailureResponse id/process instance id is null or invalid to trigger flowable execution {} ",
                    submitCramerIPServiceFailureResponse);
        }
        
        
		try {
			ScServiceDetail scServiceDetail = scServiceDetailRepository
					.findByUuidAndMstStatus_code(submitCramerIPServiceFailureResponse.getRequest().getServiceID(),TaskStatusConstants.INPROGRESS);

			logger.info("getErrorMessageDetails condition meet for scServiceDetail:{} and serviceId:{}",
					scServiceDetail, submitCramerIPServiceFailureResponse.getRequest().getServiceID());

			if (scServiceDetail != null & submitCramerIPServiceFailureResponse != null
					&& submitCramerIPServiceFailureResponse.getRequest() != null
					&& submitCramerIPServiceFailureResponse.getRequest().getErrorDetails() != null
					&& !submitCramerIPServiceFailureResponse.getRequest().getErrorDetails().isEmpty()) {
				logger.info("getErrorMessageDetails condition meet for processSubmitCramerIPServiceFailureResponse");

				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,
						"serviceDesignIpInfoCallFailureReason", getErrorMessageDetails(submitCramerIPServiceFailureResponse),
						AttributeConstants.ERROR_MESSAGE, type);
			}
		} catch (Exception e) {
			logger.error("processSubmitCramerIPServiceFailureResponse error message capture   {} ", e);
		}
 
        
        try {
        	networkInventory.setRequest(Utils.convertObjectToXmlString(submitCramerIPServiceFailureResponse,SubmitCramerIPServiceFailureResponse.class));
        	networkInventory.setResponse("VOID");
            networkInventoryRepository.save(networkInventory);
        }catch(Exception e) {
        	logger.error("Exception in processSubmitCramerIPServiceFailureResponse", e);
        }
        
    }


	private String getErrorMessageDetails(SubmitCramerIPServiceFailureResponse submitCramerIPServiceFailureResponse) throws TclCommonException {
		logger.info(" GetIpServiceEndPoint getErrorMessageDetails started for netb response");

		ErrorDetailsBean errorDetailsBean = new ErrorDetailsBean();
		errorDetailsBean.setTimestamp(new Timestamp(System.currentTimeMillis()));

		if (submitCramerIPServiceFailureResponse != null
				&& submitCramerIPServiceFailureResponse.getRequest() != null) {
			logger.info("CreateClrAsyncEndPoint getErrorMessageDetails forund");
			CramerIPServiceRequestFailure errorDetails = submitCramerIPServiceFailureResponse.getRequest();

			if (errorDetails != null && errorDetails.getErrorDetails() != null
					&& !errorDetails.getErrorDetails().isEmpty()) {

				for (CramerServiceErrorDetails serviceErrorDetails : errorDetails.getErrorDetails()) {
					ErrorBean errorBean = new ErrorBean();
					errorBean.setErrorLongDescription(serviceErrorDetails.getErrorLongDescription());
					errorBean.setErrorCode(serviceErrorDetails.getErrorCode());
					errorBean.setErrorShortDescription(serviceErrorDetails.getErrorShortDescription());
					errorDetailsBean.getErrorDetails().add(errorBean);
				}
			}
		}

		return Utils.convertObjectToJson(errorDetailsBean);

	}
	
	@Transactional(readOnly = true)
	public void updateServiceDetailsToTerminate(String serviceCode) {
		try {

			ServiceDetail oldActiveServiceDetail = serviceDetailRepository
					.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceCode, "ACTIVE");
			if (oldActiveServiceDetail != null) {
				oldActiveServiceDetail.setServiceState("TERMINATED");
				oldActiveServiceDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
				serviceDetailRepository.save(oldActiveServiceDetail);
				ScServiceDetail oldActiveScServiceDetail = scServiceDetailRepository
						.findByUuidAndMstStatus_code(serviceCode, "ACTIVE");
				if (Objects.nonNull(oldActiveScServiceDetail)) {
					logger.info("Changing SC OLD SERVICE ID ACTIVE TO TERMINATE {}",serviceCode);
					updateServiceStatusAndCreatedNewStatus(oldActiveScServiceDetail, "TERMINATE");
					if(oldActiveScServiceDetail.getIsMigratedOrder()!=null && CommonConstants.Y.equalsIgnoreCase(oldActiveScServiceDetail.getIsMigratedOrder())){
						Map<String,String> atMap=new HashMap<>();
						atMap.put("terminationReason","PARALLEL-TERMINATION");
						componentAndAttributeService.updateAttributes(oldActiveScServiceDetail.getId(), atMap,
								AttributeConstants.COMPONENT_LM, "A");
					}
					oldActiveScServiceDetail.setMstStatus(taskCacheService.getMstStatus("TERMINATE"));
					oldActiveScServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
					oldActiveScServiceDetail.setServiceConfigDate(new Timestamp(new Date().getTime()));
					oldActiveScServiceDetail.setServiceConfigStatus(TaskStatusConstants.TERMINATE);
					scServiceDetailRepository.save(oldActiveScServiceDetail);
				}
				mqUtils.send(siTerminateDetail, oldActiveScServiceDetail.getUuid());
				rfService.populateP2PandPmpTerminationData(oldActiveScServiceDetail);
			}
		} catch (Exception e) {
			logger.error("updateServiceDetailsToTerminate error for this service :{} and error:{}", serviceCode, e);
		}
	}
	
	
private void updateServiceStatusAndCreatedNewStatus(ScServiceDetail scServiceDetail,String status) {
		
		ServiceStatusDetails serviceStatusDetails=	serviceStatusDetailsRepository.findFirstByScServiceDetail_idOrderByIdDesc(scServiceDetail.getId());
		
		if(serviceStatusDetails!=null) {
			serviceStatusDetails.setEndTime(new Timestamp(new Date().getTime()));
			serviceStatusDetails.setUpdateTime(new Timestamp(new Date().getTime()));
			serviceStatusDetailsRepository.save(serviceStatusDetails);
		}
		createServiceStaus(scServiceDetail, status);
	}
	
private ServiceStatusDetails createServiceStaus(ScServiceDetail scServiceDetail, String mstStatus) {

	ServiceStatusDetails serviceStatusDetails = new ServiceStatusDetails();
	serviceStatusDetails.setScServiceDetail(scServiceDetail);
	if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

		serviceStatusDetails.setUserName(userInfoUtils.getUserInformation().getUserId());
	}
	serviceStatusDetails.setCreatedTime(new Timestamp(new Date().getTime()));
	serviceStatusDetails.setStartTime(new Timestamp(new Date().getTime()));
	serviceStatusDetails.setUpdateTime(new Timestamp(new Date().getTime()));

	serviceStatusDetails.setStatus(mstStatus);
	serviceStatusDetailsRepository.save(serviceStatusDetails);

	return serviceStatusDetails;

}

}