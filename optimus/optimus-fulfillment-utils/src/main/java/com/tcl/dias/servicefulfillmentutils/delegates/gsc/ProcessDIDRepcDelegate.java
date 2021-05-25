package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.AddressDetail;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.AuditLog;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroupToAsset;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.GscFlowGroupToAssetRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DIDSupplierBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DidNumber;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DidNumberCustRequest;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DidNumberRequest;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component("processDIDRepcDelegate")
public class ProcessDIDRepcDelegate implements JavaDelegate {

    private static final Logger Logger = LoggerFactory.getLogger(ProcessDIDRepcDelegate.class);

    @Autowired
    WorkFlowService workFlowService;

    @Autowired
    RestClientService restClientService;

    @Autowired
    GscService gscService;

    @Autowired
    ComponentAndAttributeService componentAndAttributeService;
    
    @Autowired
    private ScAssetRepository scAssetRepository;

    @Value("${gsc.createorderinrepc.authorization}")
    private String createOrderInRepcAuthorization;

    @Value("${gsc.order.did.number.url}")
    private String orderDidNumberUrl;
    
    @Autowired
    private ScAssetAttributeRepository scAssetAttributeRepository;
    
    @Autowired
    private GscFlowGroupToAssetRepository gscFlowGroupToAssetRepository;
    
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	private ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Autowired
	private ScServiceDetailRepository scServiceDetailRepository;
    
	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

    public void execute(DelegateExecution execution) {
        try {
            Map<String, Object> executionVariables = execution.getVariables();
            Logger.info("Inside DID number order delegate variables {}", executionVariables);
            Task task=workFlowService.processServiceTask(execution);
            String serviceCode = (String) executionVariables.get(SERVICE_CODE);
            Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
            requestForDidRepcNumber(serviceId, serviceCode, task,execution);
        } catch (Exception e) {
        	execution.setVariable("didrepcStatus", "failure");
    		execution.setVariable("didrepcPortingStatus", "failure");
            Logger.error("Exception in DID number order {}", e);
        }
        workFlowService.processServiceTaskCompletion(execution);
    }

    @Transactional(readOnly = false)
    public void requestForDidRepcNumber(Integer serviceId, String serviceCode, Task task,DelegateExecution execution) {
    	List<ScAsset> scAssets = null;
    	RestResponse response = null;
        try {
                Logger.info("Inside Process DID number request {}", serviceId);
                Map<String, Object> executionVariables = execution.getVariables();

                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", createOrderInRepcAuthorization);
                headers.put("Host", "");
                
				if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-order-creation-repc")) {
					
					scAssets = getScAssets(task, "did-porting-number-order-creation-repc");
				} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-order-creation-repc")) {
					scAssets = getScAssets(task, "did-new-number-order-creation-repc");

				}
				List<String> attributes = new ArrayList<String>();
				attributes.add("emergencyAddress");
				attributes.add("profileId");
				attributes.add("selectedSuppliers");
				attributes.add("terminationNumberIsdCode");
				attributes.add("sipServiceId");
				attributes.add("cityAbbr");
				
				ScServiceDetail scServiceDetail = null;
				Optional<ScServiceDetail> optionalScServiceDetail =  scServiceDetailRepository.findById(serviceId);
				if(optionalScServiceDetail.isPresent()) {
					scServiceDetail = optionalScServiceDetail.get();
				}
				

				Map<String, String> scComponentAttributes = getComponentAttributes(serviceId,
						AttributeConstants.COMPONENT_LM, "A", attributes);
				
				Map<String, String> sipComponentsMap = null;
				
				ScServiceDetail sipServiceDetail = null;
				if(scComponentAttributes.containsKey("sipServiceId")) {
					Optional<ScServiceDetail> optionalSipServiceDetail = scServiceDetailRepository
							.findById(Integer.parseInt(scComponentAttributes.get("sipServiceId")));
					if(optionalSipServiceDetail.isPresent()) {
						sipServiceDetail = optionalSipServiceDetail.get();
						List<String> sipAttributes = new ArrayList<String>();
						sipAttributes.add("cmsId");
						sipAttributes.add("secsId");
						sipAttributes.add("interconnectId");
						sipComponentsMap = commonFulfillmentUtils.getComponentAttributes(optionalSipServiceDetail.get().getId(),
								AttributeConstants.COMPONENT_LM, "A", sipAttributes);
					}
				}
				
				
				List<String> attributeNames = new ArrayList<>();
				attributeNames.add("serviceTypeRepc");
				attributeNames.add("source_country_code_repc");
				attributeNames.add("cityName");
				
				List<ScServiceAttribute> scServiceAttributes = scServiceAttributeRepository
						.findByScServiceDetail_idAndAttributeNameIn(serviceId, attributeNames);
				
				Map<String, String> scAttributesMap = commonFulfillmentUtils
						.getServiceAttributesAttributesWithAdditionalParam(scServiceAttributes);
                
				//get parentService details
				Map<String, String> parentComponentsMap = null;
			if (scServiceDetail != null) {
				if (scServiceDetail.getParentId() != null) {
					/**List<ScServiceDetail> parentServiceDetail = scServiceDetailRepository.findByProductNameAndParentId("Global Outbound on Public IP", String.valueOf(scServiceDetail.getParentId()));**/
					List<ScServiceDetail> parentServiceDetail = scServiceDetailRepository.findByServiceTypeAndParentId(GscConstants.GLOBAL_OUTBOUND, String.valueOf(scServiceDetail.getParentId()));
					if (parentServiceDetail != null && !parentServiceDetail.isEmpty()) {
						List<String> parentAttributes = new ArrayList<String>();
						parentAttributes.add("cmsId");
						parentComponentsMap = commonFulfillmentUtils.getComponentAttributes(parentServiceDetail.get(0).getId(),
								AttributeConstants.COMPONENT_LM, "A", parentAttributes);
					}
				}
			}
				
				DidNumberRequest didNumberRequest = constructDidNumberRequest(executionVariables, scComponentAttributes,
						scAssets, serviceId, scAttributesMap, sipComponentsMap, parentComponentsMap, scServiceDetail, sipServiceDetail);
                String didNumberRequestPaylod = Utils.convertObjectToJson(didNumberRequest);
                AuditLog auditLog = gscService.saveAuditLog(didNumberRequestPaylod, null,
                        serviceCode, task.getMstTaskDef().getKey()+"processDIDRepcRequest", execution.getProcessInstanceId());
                Logger.info("Order DID number - request {}", didNumberRequestPaylod);

                response = restClientService.post(orderDidNumberUrl, didNumberRequestPaylod, null, null, headers);
                Logger.info("Order DID number - response {}", response);
                gscService.updateAuditLog(auditLog, Utils.toJson(response));
                if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
                	Logger.info("Create DID repc rsponse {}",response.getStatus());
                    DidNumberRequest didNumberResponse = Utils.fromJson(response.getData(), new TypeReference<DidNumberRequest>() {});
                    String processDidNumberResponse = processDidNumberResponse(didNumberResponse, scAssets,task);
					if (!processDidNumberResponse.equalsIgnoreCase("success")) {
						Logger.info("Error logs for  processDidNumberResponse {}", processDidNumberResponse);
						saveErrorLogs(serviceId, task, execution, response);
					}
				} else {
					Logger.info("Inside Create DID repc failure response");
					saveFaliureStatusToScAsset(scAssets,task);
					saveErrorLogs(serviceId, task, execution, response);

				}
        } catch (Exception e) {
        	saveFaliureStatusToScAsset(scAssets,task);
			try {
				saveErrorLogs(serviceId, task, execution, response);
			} catch (TclCommonException e1) {
				 Logger.error("Error while updating Repc error logs for serviceCode :{} error: {}", serviceCode, e1);
			}
    		execution.setVariable("didrepcStatus", "failure");
    		execution.setVariable("didrepcPortingStatus", "failure");
            Logger.error("Error in Process DID number for serviceCode :{} error: {}", serviceCode, e);
        }
    }

	private List<ScAsset> getScAssets(Task task, String status) {
		List<ScAsset> scAssets = null;
		List<GscFlowGroupToAsset> flowGroupToAssets = gscFlowGroupToAssetRepository
				.findByGscFlowGroupId(task.getGscFlowGroupId());
		if(!flowGroupToAssets.isEmpty()) {
		List<Integer> assetIds = flowGroupToAssets.stream().map(e -> e.getScAssetId()).collect(Collectors.toList());
		if(assetIds != null && !assetIds.isEmpty()) {
			scAssets= scAssetRepository.findByIdInAndStatus(assetIds, status);
		}
		}
		return scAssets;
	}

	private void saveErrorLogs(Integer serviceId, Task task, DelegateExecution execution, RestResponse response)
			throws TclCommonException {
		setFaliureStatusToFlowable(task, execution);
		Logger.info("Create order DID number/porting - error log started for serviceId {}", serviceId);
		
		String errorMsg = null;
		String status = null;
		DidNumberRequest didNumberResponse = null;
		if(response == null) {
			errorMsg = "System Error";
		}else {
			if(response.getData() != null) {
				didNumberResponse = Utils.fromJson(response.getData(), new TypeReference<DidNumberRequest>() {});
			}
			errorMsg = response.getData() != null ? null: response.getErrorMessage();
			status = response.getStatus().toString();
		}
		
		saveErrorDetails(serviceId, task, errorMsg, status, didNumberResponse);
		
		Logger.info("Create order DID number/porting - error log ended for serviceId {}", serviceId);
	}

	private void saveErrorDetails(Integer serviceId, Task task, String errorMsg, String status, DidNumberRequest didNumberResponse)
			throws TclCommonException {
		if(didNumberResponse != null) {
			componentAndAttributeService.saveFlowGroupAttriputeWithAdditionalParam("repcCallFaliure",
					componentAndAttributeService.getErrorMessageDetails(didNumberResponse, status), task.getGscFlowGroupId(),
					null, serviceId);
		}else {
			componentAndAttributeService.saveFlowGroupAttriputeWithAdditionalParam("repcCallFaliure",
					componentAndAttributeService.getErrorMessageDetails(errorMsg, status), task.getGscFlowGroupId(),
					null, serviceId);
		}
	}

	private void setFaliureStatusToFlowable(Task task, DelegateExecution execution) {
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-order-creation-repc")) {
			execution.setVariable("didrepcStatus", "failure");
		}else {
			execution.setVariable("didrepcPortingStatus", "failure");
			execution.setVariable("didrepcStatus", "failure");

		}
	}

	private void saveFaliureStatusToScAsset(List<ScAsset> scAssets,Task task) {
		if(scAssets != null && !scAssets.isEmpty()) {
			for (ScAsset scAsset : scAssets) {
				if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-order-creation-repc")) {
					
					scAsset.setStatus("did-porting-number-order-creation-repc-jeopardy");
				} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-order-creation-repc")) {
					scAsset.setStatus("did-new-number-order-creation-repc-jeopardy");
					
				}
				scAssetRepository.save(scAsset);
				saveOrUpdateScAssetAttribute(scAsset, "repcDIDCall", "Faliure");
			}
		}
	}
	

    private DidNumberRequest constructDidNumberRequest(Map<String, Object> executionVariables, Map<String, String> componentMap, List<ScAsset> scAssets,
    		Integer servivceId, Map<String, String> scAttributesMap, Map<String, String> sipComponentsMap,Map<String, String>  parentComponentsMap, ScServiceDetail scServiceDetail, ScServiceDetail sipServiceDetail){
        DidNumberRequest didNumberRequest=new DidNumberRequest();
        
        DIDSupplierBean didSupplierBean = null;
        String supplier = componentMap.getOrDefault("selectedSuppliers", null);
		if (supplier!=null) {
			Logger.info("got suuplier info");
			List<DIDSupplierBean> selectedSuppliers = Utils.fromJson(supplier, new TypeReference<List<DIDSupplierBean>>() {});
			if(selectedSuppliers != null && !selectedSuppliers.isEmpty()) {
				didSupplierBean = selectedSuppliers.get(0);
				didNumberRequest.setCustomerId(Integer.parseInt(didSupplierBean.getSupplierOrgNo()));
			}
		}
		if(scServiceDetail != null && scServiceDetail.getSupplOrgNo()!=null) {
			didNumberRequest.setOwnerEntityId(Integer.parseInt(scServiceDetail.getSupplOrgNo()));
		}
		else if(sipServiceDetail!=null) {
			didNumberRequest.setOwnerEntityId(sipServiceDetail.getSupplOrgNo()!=null ?Integer.parseInt(sipServiceDetail.getSupplOrgNo()): null);
		}
		
		didNumberRequest.setContractServiceAbbr("GSCDDID");
		
		if(Objects.nonNull(executionVariables.get(GscConstants.SERVICE_TYPE_REPC))) {
			String serviceType = (String) executionVariables.get(GscConstants.SERVICE_TYPE_REPC);
			if(!serviceType.isEmpty() && serviceType.toUpperCase().contains("EUCDRDD")) {
				didNumberRequest.setContractServiceAbbr("EUCDRDD");			
			}
		}

        didNumberRequest.setOwnerType("TCL");
		didNumberRequest.setCustRequests(constructDidNumberCustRequest(executionVariables, componentMap, scAssets,
				servivceId, didSupplierBean, scAttributesMap, sipComponentsMap, parentComponentsMap, scServiceDetail));
        return  didNumberRequest;
    }

	private List<DidNumberCustRequest> constructDidNumberCustRequest(Map<String, Object> executionVariables,
			Map<String, String> componentMap, List<ScAsset> scAssets, Integer servivceId, DIDSupplierBean didSupplierBean,
			Map<String, String> scAttributesMap, Map<String, String> sipComponentsMap, Map<String, String>  parentComponentsMap, ScServiceDetail scServiceDetail) {
        List<DidNumberCustRequest> didNumberCustRequests=new ArrayList<>();
        DidNumberCustRequest didNumberCustRequest=new DidNumberCustRequest();
        
        if(scServiceDetail != null) {
        	didNumberCustRequest.setCountryAbbr(scServiceDetail.getSourceCountryCodeRepc());
        }
        didNumberCustRequest.setCityAbbr(componentMap.containsKey("cityAbbr")?componentMap.get("cityAbbr"):null);
        
        didNumberCustRequest.setNumbers(constructDidNumber(executionVariables, componentMap, scAssets, servivceId, didSupplierBean, sipComponentsMap, parentComponentsMap));
        
        didNumberCustRequests.add(didNumberCustRequest);
        return didNumberCustRequests;
    }

	private List<DidNumber> constructDidNumber(Map<String, Object> executionVariables, Map<String, String> componentMap,
			List<ScAsset> scAssets, Integer servivceId, DIDSupplierBean didSupplierBean, Map<String, String> sipComponentsMap, Map<String, String>  parentComponentsMap) {
        List<DidNumber> didNumbers=new ArrayList<>();
        String emergencyAddress = componentMap.getOrDefault("emergencyAddress", null);
        List<AddressDetail> addressDetails = new ArrayList<>();
        AddressDetail addrDetail = new AddressDetail();
        Iterator<AddressDetail> listItr = null;
		try {
			if (emergencyAddress != null && !emergencyAddress.isEmpty()) {
				addressDetails = Utils.fromJson(emergencyAddress, new TypeReference<List<AddressDetail>>() {
				});
				listItr = addressDetails.iterator();
			}
		} catch (Exception e) {			
			Logger.error("Exception in fecthing addressDetail{}",e);
		}
        for(ScAsset asset: scAssets){
        	DidNumber didNumber=new DidNumber();
//        	didNumber.setAddress(componentMap.get("emergencyAddress"));
        	didNumber.setAddressSameAsCustSite(false);
        	didNumber.setCorrelationId("");
        	didNumber.setCountryCode(componentMap.get("terminationNumberIsdCode"));
        	didNumber.setCustSiteAbbr("");
        	if(parentComponentsMap != null) {
				didNumber.setCustToTataConnectionCMS(parentComponentsMap.containsKey("cmsId") ? Integer.parseInt(parentComponentsMap.get("cmsId")) : null);
			}
        	if(sipComponentsMap != null) {
        		didNumber.setDidCMS(sipComponentsMap.containsKey("cmsId") ? Integer.parseInt(sipComponentsMap.get("cmsId")) : null );
        	}
        	didNumber.setDidNumberWithoutCC(asset.getName());
        	didNumber.setDtgHeader("");
        	didNumber.setEmergencyEnabled(true);
        	didNumber.setInvoiceGroupID(componentMap.get("profileId"));
        	didNumber.setIsCallerId("N");
        	didNumber.setIsCName("N");
        	didNumber.setIsPortable(false);
        	if(didSupplierBean != null) {
        		didNumber.setSupplierId(didSupplierBean.getSupplierOrgNo());
        		didNumber.setOrigDIDOwner(didSupplierBean.getSupplierOrgNo());
        	}
			List<ScAsset> scAssetInfo = scAssetRepository.findByScServiceDetail_id(servivceId);
			if (!scAssetInfo.isEmpty()) {
				for (ScAsset sc : scAssetInfo) {
					for (ScAssetAttribute attribute : sc.getScAssetAttributes()) {
						if ("isPortable".equalsIgnoreCase(attribute.getAttributeName())) {
							if ("Yes".equalsIgnoreCase(attribute.getAttributeValue())) {
								didNumber.setIsPortable(true);
							} else {
								didNumber.setIsPortable(false);
							}
							break;
						}
					}
				}
			}
//			didNumber.setPostalCd(componentMap.get("emergencyAddress"));
//			didNumber.setStateOrProvinceAbbr(componentMap.get("emergencyAddress"));
			didNumber.setStatus("");
			if(sipComponentsMap != null) {
				didNumber.setTataToCustConnectionId(sipComponentsMap.containsKey("interconnectId") ? Integer.parseInt(sipComponentsMap.get("interconnectId")) : null);
			}
			if (addressDetails != null && !addressDetails.isEmpty()) {
				if (listItr != null && listItr.hasNext()) {
					addrDetail = listItr.next();
				} else {
					listItr = addressDetails.iterator();
					addrDetail = listItr.next();
				}
				didNumber.setPostalCd(addrDetail.getPincode());
				didNumber.setAddress(addrDetail.getAddressLineOne());
				didNumber.setStateOrProvinceAbbr(addrDetail.getState());
			}
			else {
				didNumber.setAddress(emergencyAddress);
			}
			didNumbers.add(didNumber);
        };
        return didNumbers;
    }

	private String processDidNumberResponse(DidNumberRequest didNumberResponse, List<ScAsset> scAssets, Task task) {
		boolean isSuccess = true;
		Map<String,ScAsset> scAssetMap = new HashMap<>();
		scAssets.forEach(asset->{
			scAssetMap.put(asset.getName(), asset);
		});
		Logger.info("inside processDidNumberResponse method {}",task.getId());
		if (didNumberResponse != null && didNumberResponse.getCustRequests() != null
				&& didNumberResponse.getCustRequests().get(0) != null
				&& didNumberResponse.getCustRequests().get(0).getNumbers() != null) {

			Map<String, String> statusMap = new HashMap<>();

			for (DidNumber didNumber : didNumberResponse.getCustRequests().get(0).getNumbers()) {
				if(!statusMap.containsKey(didNumber.getDidNumberWithoutCC())) {
					if(scAssetMap.containsKey(didNumber.getDidNumberWithoutCC()) && "SUCESS".equalsIgnoreCase(didNumber.getStatus())) {
						saveOrUpdateScAssetAttribute(scAssetMap.get(didNumber.getDidNumberWithoutCC()), "didNumberId", didNumber.getDidNumberId());
					}
					statusMap.put(didNumber.getDidNumberWithoutCC(), didNumber.getStatus());
				}
			}
			for (ScAsset asset : scAssets) {
				Logger.info("Task def of create did repc {}",task.getMstTaskDef().getKey());
				String key =asset.getName();
				if (statusMap.containsKey(key) && statusMap.get(key).equalsIgnoreCase("success")) {
					Logger.info(" Success numbber for repc call {}",key);
					saveOrUpdateScAssetAttribute(asset, "repcDIDCall", "Success");
					if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-order-creation-repc")) {

						asset.setStatus("did-porting-number-testing");
					} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-order-creation-repc")) {
						asset.setStatus("did-new-number-order-creation-testing");

					}
					scAssetRepository.save(asset);
				} else {
					Logger.info("Failure numbber for repc call {}",key);
					if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-order-creation-repc")) {

						asset.setStatus("did-porting-number-order-creation-repc-jeopardy");
					} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-order-creation-repc")) {
						asset.setStatus("did-new-number-order-creation-repc-jeopardy");

					}
					scAssetRepository.save(asset);
					saveOrUpdateScAssetAttribute(asset, "repcDIDCall", "Faliure");
					isSuccess = false;
				}
			}
			return isSuccess ? "Success" : "Faliure";
		} else {

			return "Faliure";
		}
	}
    
    private void saveOrUpdateScAssetAttribute(ScAsset scAsset, String attributeName, String value) {
    	Logger.info("Attriutes need to update for DID repc new order {}",attributeName);
		ScAssetAttribute scAssetAttribute = scAssetAttributeRepository
				.findByScAsset_IdAndAttributeName(scAsset.getId(), attributeName);
		if(scAssetAttribute != null) {
			scAssetAttribute
					.setAttributeValue(value);
		}else {
			ScAssetAttribute scAssetAttributeNew = new ScAssetAttribute();
			scAssetAttributeNew.setAttributeName(attributeName);
			scAssetAttributeNew.setScAsset(scAsset);
			scAssetAttributeNew.setIsActive("Y");
			scAssetAttributeNew.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			scAssetAttributeRepository.save(scAssetAttributeNew);
		}
	}
    
    public Map<String, String> getComponentAttributes(Integer scServiceDetailId, String componentName,
			String siteType, List<String> attributeName) {
		Logger.info("Entering getComponentAttributes with scServiceId : {} componentName : {} siteType : {}",
				scServiceDetailId, componentName, siteType);
		List<ScComponentAttribute> scComponentAttrs = scComponentAttributesRepository
				.findByScServiceDetailIdAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetailId, componentName, siteType, attributeName);
		Map<String, String> scComponentAttributesAMap = new HashMap<>();
		for (ScComponentAttribute scComponentAttribute : scComponentAttrs) {
			if(scComponentAttribute.getIsAdditionalParam() != null
						&& scComponentAttribute.getIsAdditionalParam().equals(CommonConstants.Y)) {
				Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
						.findById(Integer.valueOf(scComponentAttribute.getAttributeValue()));
				if (scAdditionalServiceParam.isPresent()) {
					scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
							scAdditionalServiceParam.get().getValue());
				}
			}
			else {
			scComponentAttributesAMap.put(scComponentAttribute.getAttributeName(),
					scComponentAttribute.getAttributeValue());
			}
		}
		Logger.info("2 getComponentAttributes response : {}", scComponentAttributesAMap);
		return scComponentAttributesAMap;
	}
}
