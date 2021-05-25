package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.OFFERING_NAME;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_TYPE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAsset;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAssetReserved;
import com.tcl.dias.servicefulfillment.entity.entities.AuditLog;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroup;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetAttributeRepository;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.BridgeBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.CustRequestsBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.CustomerOdrCreationBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DetailsByCallTypeBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DetailsBySupplierBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.NumbersBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscNumberStatus;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component("CreateOrderInRepcDelegate")
public class CreateOrderInRepcDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(CreateOrderInRepcDelegate.class);
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
    RestClientService restClientService;
	
	@Autowired
    GscService gscService;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	@Value("${gsc.createorderinrepc.url}")
	private String createOrderInRepcUrl;
	
	@Value("${gsc.createorderinrepc.authorization}")
	private String createOrderInRepcAuthorization;
	
	@Autowired
	ScAssetAttributeRepository scAssetAttributeRepository;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private SimpleDateFormat o2cDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void execute(DelegateExecution execution) {
		try {
			Map<String, Object> executionVariables = execution.getVariables();
			logger.info("Inside create order in REPC delegate variables {}", executionVariables);
			workFlowService.processServiceTask(execution);
			String serviceCode = (String) executionVariables.get(SERVICE_CODE);
			Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
			requestForCreateOrderInRepc(0, serviceId, serviceCode, execution);
		} catch (Exception e) {
			logger.error("Exception in create order in REPC {}", e);
		}
		workFlowService.processServiceTaskCompletion(execution);
    }
	
	private void requestForCreateOrderInRepc(Integer count, Integer serviceId, String serviceCode, DelegateExecution execution) {
		try {
			if(count < GscConstants.API_MAX_RETRY_COUNT) {
				logger.info("Create order in REPC - try count {}", count);
				Map<String, Object> executionVariables = execution.getVariables();
				
				Map<String, String> headers = new HashMap<>();
				headers.put("Content-Type", "application/json");
				headers.put("Accept", "application/json");
				headers.put("Authorization", createOrderInRepcAuthorization);
				headers.put("Host", "");
				
				CustomerOdrCreationBean customerOdrCreationBean = constructRequestForCreateOrderInRepc(serviceId, serviceCode, executionVariables);
				String custRequest = Utils.convertObjectToJson(customerOdrCreationBean);
				AuditLog auditLog = gscService.saveAuditLog(custRequest, null,
						serviceCode, "CreateOrderInRepc", execution.getProcessInstanceId());
				//String custRequest = "{\"ownerType\":\"TCL\",\"customerID\":22822,\"ownerEntityId\":26449,\"contractServiceAbbr\":\"ENTLOCA\",\"solutionName\":null,\"solutionId\":null,\"tags\":null,\"custRequests\":[{\"originCountryAbbr\":\"GBZ\",\"originCityAbbr\":\"LDN\",\"endCustomerName\":\"TEST OPTIMUS-O2C-VAS-PSTN\",\"numbers\":[{\"correlationId\":\"TEST2\",\"invoiceGroupId\":null,\"detailsBySupplier\":[{\"supplierId\":24284,\"inventoryBankNumberId\":\"1002\",\"accessNumber\":null,\"routingNo\":null,\"routingNoReservationId\":null,\"platformUpdateDate\":null,\"detailsByCallType\":[{\"callType\":\"fixed\",\"blocked\":\"N\",\"custOutpulsedDigits\":\"41444666666\",\"accessType\":\"pstn\",\"directConnectionId\":null,\"pstnRoutingCMS\":44091}],\"supplierReadyDate\":null}],\"inServiceDate\":null}]}]}";
				logger.info("Create order in REPC - request {}", custRequest);
				
				RestResponse response = restClientService.post(createOrderInRepcUrl, custRequest, null, null, headers);
				//String response = "{\"ownerType\":\"TCL\",\"customerID\":22822,\"ownerEntityId\":26449,\"contractServiceAbbr\":\"ENTLOCA\",\"solutionName\":\"\",\"solutionId\":\"\",\"tags\":[],\"custRequests\":[{\"originCountryAbbr\":\"GBZ\",\"originCityAbbr\":\"LDN\",\"endCustomerName\":\"TEST OPTIMUS-O2C-VAS-PSTN\",\"numbers\":[{\"vasNumberId\":\"1038212-1-1-1\",\"correlationId\":\"TEST2\",\"invoiceGroupId\":null,\"acBridgePlatform\":\"\",\"detailsBySupplier\":[{\"supplierId\":24284,\"servAbbr\":\"LOCALB\",\"accessNumber\":\"\",\"routingNo\":\"\",\"platformUpdateDate\":null,\"detailsByCallType\":[{\"callType\":\"fixed\",\"blocked\":false,\"custOutpulsedDigits\":\"41444666666\",\"accessType\":\"pstn\",\"directConnectionId\":null,\"pstnRoutingCMS\":44091,\"switchingDetails\":[{\"sequence\":1,\"id\":\"CM\",\"description\":\"CMS ID\",\"value\":\"44091\"},{\"sequence\":2,\"id\":\"OP\",\"description\":\"OUTPULSED DIGITS\",\"value\":\"41444666666\"},{\"sequence\":3,\"id\":\"OS\",\"description\":\"OUTGOING SUMMA4 DGT\",\"value\":\"99741444666666\"},{\"sequence\":4,\"id\":\"OL\",\"description\":\"ORIGINATING LINE INFORMATION\",\"value\":\"*\"},{\"sequence\":5,\"id\":\"L2\",\"description\":\"CLID NUMBER SPNP\",\"value\":\"99\"},{\"sequence\":6,\"id\":\"L1\",\"description\":\"CLID TRANSLATION NA\",\"value\":\"99\"},{\"sequence\":7,\"id\":\"L\",\"description\":\"TRANSLATION CLID\",\"value\":\"*\"}],\"routingDetails\":[{\"requiredAction\":\"PUT\",\"platform\":\"INTL SONUS\",\"serviceId\":\"08M4461-aL\",\"pcc\":\"446\",\"callTypeNo\":1,\"mainIncomingDigits\":\"\",\"serviceName\":\"LNS\",\"tollfreeNumber\":\"\",\"inRoutes\":[{\"destNationalId\":\"999084461\",\"partition\":\"VTS\"}],\"forcedOutRouteLabelId\":\"\",\"outRouteNbRoutesPerCall\":\"2\",\"outRoutePrioritizationType\":\"3\",\"outRouteAction\":\"\",\"outRouteScriptId\":\"\",\"outRoutePartition\":\"\",\"outRouteAttributes\":\"69\",\"outRouteRoutingCriteria\":\"\",\"outRouteNbRoutesRequested\":\"\",\"outRouteEndPointsRequired\":\"1\",\"outRouteEndPointFormat\":\"NPA\",\"sid\":\"\",\"cid\":\"\",\"outRouteEndPoints\":[{\"sequence\":1,\"tag\":\"T_PSTN\",\"endPoint1\":\"ITFS_XX_NPAT_PSTN997\"}],\"outpulseType\":\"CUSTOM\",\"outpulseNbDigits\":0,\"outpulsePrefix\":\"\",\"outpulseCustomDigits\":\"4409141444666666\",\"outpulseCustomDigitsPccPosition\":\"\",\"clidType\":null,\"clidCustomDigits\":\"\",\"clidCustomDigitsPccPosition\":\"\",\"clidNatureOfAddressType\":null,\"clidNumberingPlanIndicatorType\":null,\"clidPresentationType\":null,\"clidScreeningType\":null,\"billingNumberType\":null,\"billingNumberCustomDigits\":null,\"billingNumberCustomDigitsPccPosition\":\"\",\"billingNumberNatureOfAddressType\":null,\"billingNumberNumberingPlanIndicatorType\":null,\"oliType\":null,\"connectionDtgHeader\":\"\"}]}],\"supplierReadyDate\":null}],\"inServiceDate\":null,\"endServiceDate\":null}]}]}";
				logger.info("Create order in REPC - response {}", response);
				gscService.updateAuditLog(auditLog, Utils.toJson(response));
				if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
					CustomerOdrCreationBean customerOdrCreationResBean = Utils.fromJson(response.getData(), new TypeReference<CustomerOdrCreationBean>() { });
					String processRepcResponse = processRepcResponse(customerOdrCreationBean, customerOdrCreationResBean);
					if(processRepcResponse.isEmpty()) {
						execution.setVariable(GscConstants.KEY_CREATE_ORDER_IN_REPC_STATUS, GscConstants.VALUE_SUCCESS);
						
						//GSC Group creation for inventoryNumbers
						if(Objects.nonNull(executionVariables.get(GscConstants.KEY_REPC_CREATE_ORDER_TRIGGERFOR))) {
							List<Integer> scAssets = new ArrayList<Integer>();
							String triggeredFor =  (String) executionVariables.get(GscConstants.KEY_REPC_CREATE_ORDER_TRIGGERFOR);
							if(triggeredFor.equals("inventoryNumbers")) {
								for (CustRequestsBean custRequest1 : customerOdrCreationBean.getCustRequests()) {
									for (NumbersBean number : custRequest1.getNumbers()) {
										for (DetailsBySupplierBean supplierDetail : number.getDetailsBySupplier()) {
											scAssets.add(supplierDetail.getRoutingId());
										}
									}
								}
								if(!scAssets.isEmpty()) {
									GscFlowGroup createGscFlowGroup = gscService.createGscFlowGroup("CreateOrderInRepc-"+triggeredFor, execution.getProcessInstanceId(), "ProcessInstanceId", "system", scAssets);
									execution.setVariable(GscConstants.KEY_GSC_FLOW_GROUP_ID, createGscFlowGroup.getId());
								}
							}
						}
					} else {
						try {
							logger.info("Create order in REPC - error log started");
							componentAndAttributeService.updateAdditionalAttributes(serviceId,
									"CreateOrderInRepcCallFailureReason",
									componentAndAttributeService.getErrorMessageDetails(processRepcResponse, "FAILED"),
									AttributeConstants.ERROR_MESSAGE, "gsc-repc-order-creation");
						} catch (Exception e) {
							logger.error("Create order in REPC - error message details {}", e);
						}
					}
					execution.setVariable("orderInRepcRes", response.getData());
				} else {
					try {
						logger.info("Create order in REPC - error log started");
						componentAndAttributeService.updateAdditionalAttributes(serviceId,
								"CreateOrderInRepcCallFailureReason",
								componentAndAttributeService.getErrorMessageDetails(response.getErrorMessage(), response.getStatus().toString()),
								AttributeConstants.ERROR_MESSAGE, "gsc-repc-order-creation");
					} catch (Exception e) {
						logger.error("Create order in REPC - error message details {}", e);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Create order in REPC - error message details {}", e);
		}
	}
	
	public CustomerOdrCreationBean constructRequestForCreateOrderInRepc(Integer serviceId, String serviceCode, Map<String, Object> executionVariables) throws TclCommonException, ParseException {
		CustomerOdrCreationBean customerOdrCreationBean = new CustomerOdrCreationBean();
		customerOdrCreationBean.setOwnerType("TCL");
		
		//Required component attributes construction part
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId,
				AttributeConstants.COMPONENT_GSCLICENSE, "A", Arrays.asList("cmsId", "terminationNumberIsdCode", "workingTemporaryTerminationNumber", "bridgeDetails", "profileId"));
		String terminationNumberIsdCode = scComponentAttributesmap.getOrDefault("terminationNumberIsdCode", "");
		if(terminationNumberIsdCode != null && !terminationNumberIsdCode.isEmpty()) {
			terminationNumberIsdCode = terminationNumberIsdCode.replace("+", "");
		} else {
			terminationNumberIsdCode = "";
		}
		scComponentAttributesmap.put("terminationNumberIsdCodeWithoutSymbol", terminationNumberIsdCode);
		
		if (Objects.nonNull(executionVariables.get(GscConstants.IS_PARTNER_ORDER))
				&& ((String) executionVariables.get(GscConstants.IS_PARTNER_ORDER)).equals("yes")) {
			customerOdrCreationBean.setOwnerType(gscService
					.getRepcPartnerAbbreviationName((Integer) executionVariables.get(MasterDefConstants.SC_ORDER_ID)));
		}
		if(Objects.nonNull(executionVariables.get(GscConstants.CUSTOMER_ORG_ID)) && !((String)executionVariables.get(GscConstants.CUSTOMER_ORG_ID)).isEmpty()) {
			//customerOdrCreationBean.setCustomerID(22822);
			customerOdrCreationBean.setCustomerId(Integer.parseInt((String) executionVariables.get(GscConstants.CUSTOMER_ORG_ID)));
		}
		if(Objects.nonNull(executionVariables.get(GscConstants.SUPPLIER_ORG_ID)) && !((String)executionVariables.get(GscConstants.SUPPLIER_ORG_ID)).isEmpty() ) {
			//customerOdrCreationBean.setOwnerEntityId(26449);
			customerOdrCreationBean.setOwnerEntityId(Integer.parseInt((String) executionVariables.get(GscConstants.SUPPLIER_ORG_ID)));
		}
		if(Objects.nonNull(executionVariables.get(GscConstants.SERVICE_TYPE_REPC))) {
			//customerOdrCreationBean.setContractServiceAbbr("ENTLOCA");
			customerOdrCreationBean.setContractServiceAbbr((String) executionVariables.get(GscConstants.SERVICE_TYPE_REPC));
		}
		customerOdrCreationBean.setCustRequests(constructCustRequestsBean(serviceId, serviceCode, executionVariables, scComponentAttributesmap));
		return customerOdrCreationBean;
	}
	
	public List<CustRequestsBean> constructCustRequestsBean(Integer serviceId, String serviceCode, Map<String, Object> executionVariables, Map<String, String> scComponentAttributesmap) throws TclCommonException, ParseException {
		List<CustRequestsBean> custRequests = new ArrayList<>();
		String offeringName = (String) executionVariables.get(OFFERING_NAME);
		if(offeringName.contains("LNS") 
				|| offeringName.contains("ACANS") || offeringName.contains("ACLNS")) {
			if(Objects.nonNull(executionVariables.get(GscConstants.KEY_REPC_CREATE_ORDER_TRIGGERFOR))) {
				String triggeredFor =  (String) executionVariables.get(GscConstants.KEY_REPC_CREATE_ORDER_TRIGGERFOR);
				List<NumbersBean> constructNumbersBean = constructNumbersBean(serviceId, serviceCode, triggeredFor, executionVariables, scComponentAttributesmap);
				for(NumbersBean numbersBean : constructNumbersBean) {
					List<NumbersBean> numbers = new ArrayList<>();
					numbers.add(numbersBean);
					CustRequestsBean custRequestsBean = new CustRequestsBean();
					if(Objects.nonNull(executionVariables.get(GscConstants.ORIGIN_COUNTRY_CODE))) {
						custRequestsBean.setOriginCountryAbbr((String) executionVariables.get(GscConstants.ORIGIN_COUNTRY_CODE));
					}
					custRequestsBean.setOriginCityAbbr(numbersBean.getCityCode());
					custRequestsBean.setEndCustomerName("TEST OPTIMUS-O2C-VAS-PSTN");
					custRequestsBean.setNumbers(numbers);
					custRequests.add(custRequestsBean);
				}
			}
		} else {
			CustRequestsBean custRequestsBean = new CustRequestsBean();
			if(Objects.nonNull(executionVariables.get(GscConstants.ORIGIN_COUNTRY_CODE))) {
				custRequestsBean.setOriginCountryAbbr((String) executionVariables.get(GscConstants.ORIGIN_COUNTRY_CODE));
			}
			custRequestsBean.setEndCustomerName("TEST OPTIMUS-O2C-VAS-PSTN");
			/*if(Objects.nonNull(executionVariables.get(GscConstants.KEY_GENERATE_ROUTING_NUMBER_STATUS)) 
					&& executionVariables.get(GscConstants.KEY_GENERATE_ROUTING_NUMBER_STATUS).equals(GscConstants.VALUE_SUCCESS)) {*/
			if(Objects.nonNull(executionVariables.get(GscConstants.KEY_REPC_CREATE_ORDER_TRIGGERFOR))) {
				String triggeredFor =  (String) executionVariables.get(GscConstants.KEY_REPC_CREATE_ORDER_TRIGGERFOR);
				custRequestsBean.setNumbers(constructNumbersBean(serviceId, serviceCode, triggeredFor, executionVariables, scComponentAttributesmap));
			} else {
				//custRequestsBean.setNumbers(constructNumbersBean(serviceId, serviceCode, "reservedNumber", executionVariables));
			}
			custRequests.add(custRequestsBean);
		}
		return custRequests;
	}
	
	public List<NumbersBean> constructNumbersBean(Integer serviceId, String serviceCode, String flow, Map<String, Object> executionVariables, Map<String, String> scComponentAttributesmap) throws TclCommonException, ParseException {
		List<NumbersBean> numbers = new ArrayList<>();
		List<DetailsBySupplierBean> detailsBySuppliersBean = constructDetailsBySupplierBean(serviceId, flow, executionVariables, scComponentAttributesmap);
		
		HashMap<String, String> bridgeMapper = new HashMap<>();
		String offeringName = (String) executionVariables.get(OFFERING_NAME);
		String originCountryCode = (String) executionVariables.get(GscConstants.ORIGIN_COUNTRY_CODE);
		if(offeringName.contains("ACANS") || offeringName.contains("ACLNS") || offeringName.contains("ACDTFS")) {
			String bridgeDetails = scComponentAttributesmap.getOrDefault("bridgeDetails", null);
			if(bridgeDetails != null) {
				List<BridgeBean> bridgeBeanDetails = Utils.convertJsonToObject(bridgeDetails, ArrayList.class);
				for(BridgeBean bridgeBeanDetail: bridgeBeanDetails) {
					bridgeMapper.put(bridgeBeanDetail.getCity(), bridgeBeanDetail.getBridge());
				}
			}
		}
		Integer profileId = StringUtils.isEmpty(scComponentAttributesmap.getOrDefault("profileId", ""))?0:Integer.parseInt(scComponentAttributesmap.getOrDefault("profileId", ""));
		for(DetailsBySupplierBean detailsBySupplierBean : detailsBySuppliersBean) {
			NumbersBean numbersBean = new NumbersBean();
			numbersBean.setCorrelationId(serviceCode);
			numbersBean.setInvoiceGroupId(profileId);
			List<DetailsBySupplierBean> detailsBySupplier = new ArrayList<>();
			detailsBySupplier.add(detailsBySupplierBean);
			numbersBean.setDetailsBySupplier(detailsBySupplier);
			if(detailsBySupplierBean.getCityCode() != null) {
				numbersBean.setCityCode(detailsBySupplierBean.getCityCode());
			}
			if(detailsBySupplierBean.getCityCode() != null && bridgeMapper.containsKey(detailsBySupplierBean.getCityCode())) {
				numbersBean.setAcBridgePlatform(bridgeMapper.get(detailsBySupplierBean.getCityCode()));
			} else if(originCountryCode != null && bridgeMapper.containsKey(originCountryCode)) {
				numbersBean.setAcBridgePlatform(bridgeMapper.get(originCountryCode));
			}
			numbers.add(numbersBean);
		}
		return numbers;
	}
	
	public List<DetailsBySupplierBean> constructDetailsBySupplierBean(Integer serviceId, String flow, Map<String, Object> executionVariables, Map<String, String> scComponentAttributesmap) throws TclCommonException, ParseException {
		List<DetailsBySupplierBean> detailsBySupplier = new ArrayList<>();
		String accessType = (String) executionVariables.get(GscConstants.ACCESS_TYPE);
		String offeringName = (String) executionVariables.get(OFFERING_NAME);
		
		String terminationNumberIsdCode = scComponentAttributesmap.getOrDefault("terminationNumberIsdCodeWithoutSymbol", "");
		String orderType = "NEW";		
		if(executionVariables.get(ORDER_TYPE) != null && !((String) executionVariables.get(ORDER_TYPE)).isEmpty()) {
			orderType = (String) executionVariables.get(ORDER_TYPE);
		}
		if(flow.equals("routingNumberRequired")) {
			List<GscScAssetReserved> repcReservedRoutingNumbers = gscService.getRepcReservedRoutingNumbers(serviceId);
			for(GscScAssetReserved repcReservedRoutingNumber: repcReservedRoutingNumbers) {
				if(!repcReservedRoutingNumber.getRoutingName().equals("N/A")) {
					DetailsBySupplierBean detailsBySupplierBean = new DetailsBySupplierBean();
					detailsBySupplierBean.setSupplierId(Integer.parseInt(repcReservedRoutingNumber.getSupplierId()));
					detailsBySupplierBean.setRoutingNo(repcReservedRoutingNumber.getRoutingName());					
					detailsBySupplierBean.setRoutingId(repcReservedRoutingNumber.getRoutingId());
					if(repcReservedRoutingNumber.getReservationId() != null) {
						detailsBySupplierBean.setRoutingNoReservationId(repcReservedRoutingNumber.getReservationId());
					}
					if(repcReservedRoutingNumber.getTollfreeName() != null) {
						detailsBySupplierBean.setAccessNumber(repcReservedRoutingNumber.getTollfreeName());
					}
					//detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId, repcReservedRoutingNumber.getOutpulseName(), "pstn"));
					if(accessType != null && (accessType.equalsIgnoreCase("PUBLIC IP") || accessType.equalsIgnoreCase("MPLS"))) {
						if(orderType.equalsIgnoreCase("NEW")) {
							ScServiceDetail sipServiceDetail = gscService.getSipServiceDetailByChildService(serviceId);
							if(!Objects.isNull(sipServiceDetail)) {
								Map<String, String> sipScComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(
										sipServiceDetail.getId(), AttributeConstants.COMPONENT_GSCLICENSE, "A", Arrays.asList("workingTemporaryTerminationNumber"));
								//String isWorkingTemporaryTerminationNumber = scComponentAttributesmap.getOrDefault("isWorkingTemporaryTerminationNumber", "");
								String isWorkingTemporaryTerminationNumber = (String) executionVariables.get(MasterDefConstants.TEMP_OUTPULSE_PRESENT);
								if(isWorkingTemporaryTerminationNumber.equalsIgnoreCase("YES")) {
									detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId,
											sipScComponentAttributesmap.getOrDefault("workingTemporaryTerminationNumber", ""), "pstn", orderType, repcReservedRoutingNumber.getTollfreeName(), scComponentAttributesmap));
								} else {
									detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId,
											repcReservedRoutingNumber.getOutpulseName(), "direct connection", orderType, repcReservedRoutingNumber.getTollfreeName(), scComponentAttributesmap));								
								}
							}
						} else {
							detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId,
									repcReservedRoutingNumber.getOutpulseName(), "direct connection", orderType, repcReservedRoutingNumber.getTollfreeName(), scComponentAttributesmap));
						}
					} else {
						detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId, repcReservedRoutingNumber.getOutpulseName(), "pstn", orderType,repcReservedRoutingNumber.getTollfreeName(), scComponentAttributesmap));						
					}					
					if(offeringName.contains("LNS") 
							|| offeringName.contains("ACANS") || offeringName.contains("ACLNS")) {
						ScAssetAttribute scAssetAttribute = scAssetAttributeRepository
								.findByScAsset_IdAndAttributeName(repcReservedRoutingNumber.getOutpulseId(), "cityCode");
						if(scAssetAttribute != null) {
							detailsBySupplierBean.setCityCode(scAssetAttribute.getAttributeValue());
						}
					}
					detailsBySupplier.add(detailsBySupplierBean);
				}
			}
		} else if(flow.equals("routingNumberNA") || flow.equals("routingNumberNAPort")) {
			//List<GscScAssetReserved> repcReservedRoutingNumbers = gscService.getRepcReservedRoutingNumbers(serviceId);
			Integer flowGroupId = (Integer) executionVariables.get(GscConstants.KEY_GSC_FLOW_GROUP_ID);
			List<GscScAsset> repcReservedRoutingNumbers;
			if(flow.equals("routingNumberNAPort")) {
				repcReservedRoutingNumbers = gscService.getTollFreeAndRoutingFromOutpuseByOutpluseFlowGrpID(serviceId,
					flowGroupId);
			} else {
				repcReservedRoutingNumbers = gscService.getTollFreeAndRoutingFromOutpuseByFlowGrpIDAndStatus(serviceId,
						flowGroupId, GscNumberStatus.IN_REPC_ORDER_CREATION);
			}
			for(GscScAsset repcReservedRoutingNumber: repcReservedRoutingNumbers) {
				DetailsBySupplierBean detailsBySupplierBean = new DetailsBySupplierBean();
				if(!repcReservedRoutingNumber.getRoutingName().equals("N/A")) {
					detailsBySupplierBean.setRoutingNo(repcReservedRoutingNumber.getRoutingName());
				}
				detailsBySupplierBean.setRoutingId(repcReservedRoutingNumber.getRoutingId());
				if(repcReservedRoutingNumber.getTollfreeName() != null) {
					detailsBySupplierBean.setAccessNumber(repcReservedRoutingNumber.getTollfreeName());
				}
				List<ScAssetAttribute> scAssetAttributes = scAssetAttributeRepository
						.findByScAsset_IdAndAttributeNameIn(repcReservedRoutingNumber.getRoutingId(), Arrays.asList("supplierActivationDate", "supplierId"));
				if (Objects.nonNull(scAssetAttributes) && !scAssetAttributes.isEmpty()) {
					for(ScAssetAttribute scAssetAttribute : scAssetAttributes) {
						if (Objects.nonNull(scAssetAttribute) && Objects.nonNull(scAssetAttribute.getAttributeValue())) {
							if(scAssetAttribute.getAttributeName().equalsIgnoreCase("supplierActivationDate")) {
								Date date = o2cDateFormat.parse(scAssetAttribute.getAttributeValue());
								detailsBySupplierBean.setSupplierReadyDate(sdf.format(date));
							} else if(scAssetAttribute.getAttributeName().equalsIgnoreCase("supplierId")) {
								detailsBySupplierBean.setSupplierId(Integer.parseInt(scAssetAttribute.getAttributeValue()));				
							}
						}
					}
				}
				//detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId, repcReservedRoutingNumber.getOutpulseName(), "pstn"));
				if(accessType != null && (accessType.equalsIgnoreCase("PUBLIC IP") || accessType.equalsIgnoreCase("MPLS"))) {
					if(orderType.equalsIgnoreCase("NEW")) {
						ScServiceDetail sipServiceDetail = gscService.getSipServiceDetailByChildService(serviceId);
						if(!Objects.isNull(sipServiceDetail)) {
							Map<String, String> sipScComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(
									sipServiceDetail.getId(), AttributeConstants.COMPONENT_GSCLICENSE, "A", Arrays.asList("workingTemporaryTerminationNumber"));
							//String isWorkingTemporaryTerminationNumber = scComponentAttributesmap.getOrDefault("isWorkingTemporaryTerminationNumber", "");
							String isWorkingTemporaryTerminationNumber = (String) executionVariables.get(MasterDefConstants.TEMP_OUTPULSE_PRESENT);
							if(isWorkingTemporaryTerminationNumber.equalsIgnoreCase("YES")) {
								detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId,
										sipScComponentAttributesmap.getOrDefault("workingTemporaryTerminationNumber", ""), "pstn", orderType, repcReservedRoutingNumber.getTollfreeName(), scComponentAttributesmap));
							} else {
								detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId,
										repcReservedRoutingNumber.getOutpulseName(), "direct connection", orderType, repcReservedRoutingNumber.getTollfreeName(), scComponentAttributesmap));
							}
						}
					} else {
						detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId,
								repcReservedRoutingNumber.getOutpulseName(), "direct connection", orderType, repcReservedRoutingNumber.getTollfreeName(), scComponentAttributesmap));
					}
				} else {
					detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId, repcReservedRoutingNumber.getOutpulseName(), "pstn", orderType, repcReservedRoutingNumber.getTollfreeName(), scComponentAttributesmap));
				}
								
				if(offeringName.contains("LNS") 
						|| offeringName.contains("ACANS") || offeringName.contains("ACLNS")) {
					ScAssetAttribute scAssetAttribute = scAssetAttributeRepository
							.findByScAsset_IdAndAttributeName(repcReservedRoutingNumber.getOutpulseId(), "cityCode");
					if(scAssetAttribute != null) {
						detailsBySupplierBean.setCityCode(scAssetAttribute.getAttributeValue());
					}
				}				
				detailsBySupplier.add(detailsBySupplierBean);
			}
		} else if(flow.equals("inventoryNumbers")) {
			List<GscScAssetReserved> repcReservedTollNumbers = gscService.getRepcReservedTollNumbers(serviceId);
			for(GscScAssetReserved repcReservedTollNumber: repcReservedTollNumbers) {
				DetailsBySupplierBean detailsBySupplierBean = new DetailsBySupplierBean();
				if(repcReservedTollNumber.getRoutingId() != null) {
					detailsBySupplierBean.setRoutingId(repcReservedTollNumber.getRoutingId());
				}
				detailsBySupplierBean.setAccessNumberId(repcReservedTollNumber.getTollfreeId());
				detailsBySupplierBean.setAccessNumber(repcReservedTollNumber.getTollfreeName());
				detailsBySupplierBean.setInventoryBankNumberId(repcReservedTollNumber.getReservationId());
				//detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId, repcReservedTollNumber.getOutpulseName(), "pstn"));
				if(accessType != null && (accessType.equalsIgnoreCase("PUBLIC IP") || accessType.equalsIgnoreCase("MPLS"))) {
					if(orderType.equalsIgnoreCase("NEW")) {
						ScServiceDetail sipServiceDetail = gscService.getSipServiceDetailByChildService(serviceId);
						if(Objects.nonNull(sipServiceDetail)) {
							Map<String, String> sipScComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(
									sipServiceDetail.getId(), AttributeConstants.COMPONENT_GSCLICENSE, "A", Arrays.asList("workingTemporaryTerminationNumber"));
							//String isWorkingTemporaryTerminationNumber = scComponentAttributesmap.getOrDefault("isWorkingTemporaryTerminationNumber", "");
							String isWorkingTemporaryTerminationNumber = (String) executionVariables.get(MasterDefConstants.TEMP_OUTPULSE_PRESENT);
							if(isWorkingTemporaryTerminationNumber.equalsIgnoreCase("YES")) {
								detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId,
										sipScComponentAttributesmap.getOrDefault("workingTemporaryTerminationNumber", ""), "pstn", orderType, repcReservedTollNumber.getTollfreeName(), scComponentAttributesmap));
							} else {
								detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId,
										repcReservedTollNumber.getOutpulseName(), "direct connection", orderType, repcReservedTollNumber.getTollfreeName(), scComponentAttributesmap));
							}						
						}
					} else {
						detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId,
								repcReservedTollNumber.getOutpulseName(), "direct connection", orderType, repcReservedTollNumber.getTollfreeName(), scComponentAttributesmap));
					}
				} else {
					detailsBySupplierBean.setDetailsByCallType(constructDetailsByCallTypeBean(serviceId, repcReservedTollNumber.getOutpulseName(), "pstn", orderType, repcReservedTollNumber.getTollfreeName(), scComponentAttributesmap));
				}
								
				if(offeringName.contains("LNS") 
						|| offeringName.contains("ACANS") || offeringName.contains("ACLNS")) {
					ScAssetAttribute scAssetAttribute = scAssetAttributeRepository
							.findByScAsset_IdAndAttributeName(repcReservedTollNumber.getOutpulseId(), "cityCode");
					if(scAssetAttribute != null) {
						detailsBySupplierBean.setCityCode(scAssetAttribute.getAttributeValue());
					}
				}
				detailsBySupplier.add(detailsBySupplierBean);
			}
		}
		return detailsBySupplier;
	}
	
	public List<DetailsByCallTypeBean> constructDetailsByCallTypeBean(Integer serviceId, String outpulse, String accessType, String orderType, String accessNumber, Map<String, String> scComponentAttributesmap) throws TclCommonException {
		String terminationNumberIsdCode = scComponentAttributesmap.getOrDefault("terminationNumberIsdCodeWithoutSymbol", "");
		List<DetailsByCallTypeBean> detailsByCallType = new ArrayList<>();
		List<String> repcCallTypeList = gscService.getRepcCallTypeList(serviceId);
		for(String callType : repcCallTypeList) {
			DetailsByCallTypeBean detailsByCallTypeBean = new DetailsByCallTypeBean();
			detailsByCallTypeBean.setCallType(callType);
			detailsByCallTypeBean.setBlocked("N");
			if("Same As Number".equalsIgnoreCase(outpulse)) {
				if(accessNumber != null) {
					detailsByCallTypeBean.setCustOutpulsedDigits(terminationNumberIsdCode + accessNumber);
				} else {
					detailsByCallTypeBean.setCustOutpulsedDigits(terminationNumberIsdCode + scComponentAttributesmap.getOrDefault("workingTemporaryTerminationNumber", ""));
				}
			} else {
				detailsByCallTypeBean.setCustOutpulsedDigits(terminationNumberIsdCode + outpulse);
			}
			detailsByCallTypeBean.setAccessType(accessType);
			if(accessType.equals("direct connection")) {
				ScServiceDetail sipServiceDetail = null;
				if(orderType.equalsIgnoreCase("MACD")) {
					sipServiceDetail = gscService.getParentServiceDetail(serviceId);
				} else {
					sipServiceDetail = gscService.getSipServiceDetailByChildService(serviceId);
				}
				if(sipServiceDetail != null) {
					Map<String, String> sipAttributes = commonFulfillmentUtils.getComponentAttributes(sipServiceDetail.getId(), AttributeConstants.COMPONENT_GSCLICENSE,
							"A", Arrays.asList("interconnectId"));
					String interconnectId = sipAttributes.getOrDefault("interconnectId", null);
					if(interconnectId != null && !interconnectId.isEmpty()) {
						detailsByCallTypeBean.setDirectConnectionId(Integer.parseInt(interconnectId));
					}
				}
			}
			//detailsByCallTypeBean.setPstnRoutingCMS(44091);			
			detailsByCallTypeBean.setPstnRoutingCMS(Integer.parseInt(scComponentAttributesmap.getOrDefault("cmsId", null)));
			detailsByCallType.add(detailsByCallTypeBean);
		}
		return detailsByCallType;
	}
	
	private String processRepcResponse(CustomerOdrCreationBean requestBean, CustomerOdrCreationBean responseBean) throws TclCommonException {
    	StringBuilder response = new StringBuilder();
		if (responseBean != null) {
			HashMap<String, DetailsBySupplierBean> responseMapper = new HashMap<String, DetailsBySupplierBean>();
			for (CustRequestsBean custRequest : requestBean.getCustRequests()) {
				for (NumbersBean number : custRequest.getNumbers()) {
					for (DetailsBySupplierBean supplierDetail : number.getDetailsBySupplier()) {
						responseMapper.put(extractRepcNumberKey(supplierDetail), supplierDetail);
					}
				}
			}
			for (CustRequestsBean custRequest : responseBean.getCustRequests()) {
				for (NumbersBean number : custRequest.getNumbers()) {
					for (DetailsBySupplierBean supplierDetail : number.getDetailsBySupplier()) {
						Boolean errorFlag = false;
						for (DetailsByCallTypeBean callTypeDetail : supplierDetail.getDetailsByCallType()) {
							if (callTypeDetail.getStatus() != null
									&& callTypeDetail.getStatus().equalsIgnoreCase("FAILED")) {
								response.append(callTypeDetail.getStatusMsg()+"\n");	
								errorFlag = true;
							}
						}
						DetailsBySupplierBean detailsBySupplierBean = responseMapper.get(extractRepcNumberKey(supplierDetail));
						if(detailsBySupplierBean != null) {
							Map<String, String> atMap = new HashMap<String, String>();
							if(supplierDetail.getDetailsByCallType() != null) {
								atMap.put("detailsByCallType", Utils.convertObjectToJson(supplierDetail.getDetailsByCallType()));
							}
							if(number.getVasNumberId() != null) {
								atMap.put("vasNumberId", number.getVasNumberId());
							}
							if(detailsBySupplierBean.getInventoryBankNumberId() != null && supplierDetail.getSupplierId() != null) {
								atMap.put("supplierId", supplierDetail.getSupplierId().toString());
							}
							if(errorFlag) {
								atMap.put("repcOrderStatus", "FAILED");	
							} else {
								atMap.put("repcOrderStatus", "SUCCESS");
							}
							if(detailsBySupplierBean.getAccessNumberId() != null) {
								gscService.updatedScAssetAttribute(detailsBySupplierBean.getAccessNumberId(), atMap, "system");
							}
							if(detailsBySupplierBean.getRoutingId() != null) {
								gscService.updatedScAssetAttribute(detailsBySupplierBean.getRoutingId(), atMap, "system");
							}
						}
					}
				}
			}
		}
		return response.toString();
    }
    
    private String extractRepcNumberKey(DetailsBySupplierBean supplierDetail) {
    	StringBuilder key = new StringBuilder();
    	/*if(supplierDetail.getSupplierId() != null) {
    		key.append(supplierDetail.getSupplierId());
    	}
    	if(supplierDetail.getInventoryBankNumberId() != null) {
    		key.append(supplierDetail.getInventoryBankNumberId());
    	}*/
    	if(supplierDetail.getAccessNumber() != null) {
    		key.append(supplierDetail.getAccessNumber());
    	}
    	if(supplierDetail.getRoutingNo() != null) {
    		key.append(supplierDetail.getRoutingNo());
    	}
    	return key.toString();
    }
}