package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.AuditLog;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroupToAsset;
import com.tcl.dias.servicefulfillment.entity.entities.ScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.GscFlowGroupToAssetRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DidNumber;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DidNumberCustRequest;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component("updateDidPatchRepc")
public class UpdatePatchNumberDelegate implements JavaDelegate {

    private static final Logger Logger = LoggerFactory.getLogger(UpdatePatchNumberDelegate.class);

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

    @Value("${gsc.patchorderinrepc.authorization}")
    private String createOrderInRepcAuthorization;

    @Value("${gsc.patch.did.number.url}")
    private String patchDidNumberUrl;
    
    @Autowired
    private ScAssetAttributeRepository scAssetAttributeRepository;
    
    @Autowired
    private GscFlowGroupToAssetRepository gscFlowGroupToAssetRepository;
    
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	private ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
    


    public void execute(DelegateExecution execution) {
        try {
            Map<String, Object> executionVariables = execution.getVariables();
            Logger.info("Inside Patch DID number delegate variables {}", executionVariables);
            Task task=workFlowService.processServiceTask(execution);
            String serviceCode = (String) executionVariables.get(SERVICE_CODE);
            Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
            requestForDidPortingNumber(serviceId, serviceCode, task,execution);
        } catch (Exception e) {
            Logger.error("Exception in Patch DID number {}", e);
        }
        workFlowService.processServiceTaskCompletion(execution);
    }

    @Transactional(readOnly = false)
    public void requestForDidPortingNumber(Integer serviceId, String serviceCode, Task task,DelegateExecution execution) {
    	List<ScAsset> scAssets = null;
    	RestResponse response = null;
        try {
                Logger.info("Inside Process DID number request {}", serviceId);
                Map<String, Object> executionVariables = execution.getVariables();

                HttpHeaders headers = new HttpHeaders();
			    headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
			    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			    headers.set("Authorization", createOrderInRepcAuthorization);
			    headers.set("Host", "");
                
				scAssets = getScAssets(task, task.getMstTaskDef().getKey());
			
				List<String> attributes = new ArrayList<String>();
				attributes.add("cmsId");
			    ScServiceDetail scServiceDetail =  scServiceDetailRepository.findById(serviceId).orElse(new ScServiceDetail());
				Map<String, String> scComponentAttributes = commonFulfillmentUtils.getComponentAttributes(serviceId,
						AttributeConstants.COMPONENT_LM, "A", attributes);
				
				List<String> attributeNames = new ArrayList<>();
				attributeNames.add("source_country_code_repc");
				attributeNames.add("cityName");
				
				List<ScServiceAttribute> scServiceAttributes = scServiceAttributeRepository
						.findByScServiceDetail_idAndAttributeNameIn(serviceId, attributeNames);
				
				Map<String, String> scAttributesMap = commonFulfillmentUtils
						.getServiceAttributesAttributesWithAdditionalParam(scServiceAttributes);
                
                DidNumberCustRequest didPatchNumberRequest = constructUpdateDidNumberRequest(scAssets,scServiceDetail,scComponentAttributes);
                String didNumber="";
                if(didPatchNumberRequest!=null && didPatchNumberRequest.getNumbers() != null && !didPatchNumberRequest.getNumbers().isEmpty()){
					didNumber=didPatchNumberRequest.getNumbers().stream().findFirst().get().getDidNumberId();
				}
                String didPatchNumberRequestPaylod = Utils.convertObjectToJson(didPatchNumberRequest);
                AuditLog auditLog = gscService.saveAuditLog(didPatchNumberRequestPaylod, null,
                        serviceCode, task.getMstTaskDef().getKey()+"patchDIDRequest", execution.getProcessInstanceId());
                Logger.info("Patch DID number - request {}", didPatchNumberRequestPaylod);

                response = restClientService.patch(patchDidNumberUrl+didNumber, didPatchNumberRequestPaylod, headers);
                Logger.info("Patch DID number - response {}", response);
                gscService.updateAuditLog(auditLog, Utils.toJson(response));
                if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
                    Logger.info("Inside requestForDidPortingNumber success :{} and data:{}", serviceId,response.getData());

					List<DidNumberCustRequest> updateDidNumberResponse = Utils.fromJson(response.getData(), new TypeReference<List<DidNumberCustRequest>>() {});
                    String processPatchDidNumberResponse = processPatchDidNumberResponse(updateDidNumberResponse.get(0), scAssets,task);
			        Logger.info("Inside processPatchDidNumberResponse response:{}", processPatchDidNumberResponse);

                    if (!processPatchDidNumberResponse.equalsIgnoreCase("success")) {
    			        Logger.info("Inside processPatchDidNumberResponse failure:{}", processPatchDidNumberResponse);

						saveErrorLogs(serviceId, task, execution, response);
					}
				} else {
                    Logger.info("Inside requestForDidPortingNumber Failure case :{} and data:{}", serviceId,response.getData());

					saveFaliureStatusToScAsset(scAssets,task);
					saveErrorLogs(serviceId, task, execution, response);

				}
        } catch (Exception e) {
	        Logger.error("Inside DID error failure:{}", e);

        	saveFaliureStatusToScAsset(scAssets,task);
        	try {
    	        Logger.error("Inside DID error failure save into component attributes:{}", e);
				saveErrorLogs(serviceId, task, execution, response);
			} catch (TclCommonException e1) {
				 Logger.error("Error while updating Patch number error logs for serviceCode :{} error: {}", serviceCode, e1);
			}
            e.printStackTrace();
            Logger.error("Error in Patch DID number for serviceCode error {}", serviceCode, e);
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
		Logger.info("Patch DID number/Porting - error log started for service Id {} ", serviceId);
		
		setFaliureStatusToFlowable(task, execution);
		String errorMsg = null;
		String status = null;
		List<DidNumberCustRequest> updateDidNumberResponse = null;
		if(response == null) {
			errorMsg = "System Error";
		}else {
			if(response.getData() != null) {
				updateDidNumberResponse = Utils.fromJson(response.getData(), new TypeReference<List<DidNumberCustRequest>>() {});
			}
			errorMsg = response.getData() != null ? null: response.getErrorMessage();
			status = response.getStatus().toString();
		}
		
		saveErrorDetails(serviceId, task, errorMsg, status, updateDidNumberResponse != null && !updateDidNumberResponse.isEmpty() ?  updateDidNumberResponse.get(0): null);
		
		Logger.info("Patch DID number/Porting - error log ended for service Id {} ", serviceId);
		
	}
	
	private void saveErrorDetails(Integer serviceId, Task task, String errorMsg, String status, DidNumberCustRequest updateDidNumberResponse)
			throws TclCommonException {
		if(updateDidNumberResponse != null) {
			componentAndAttributeService.saveFlowGroupAttriputeWithAdditionalParam("patchCallFaliure",
					componentAndAttributeService.getErrorMessageDetails(updateDidNumberResponse, status), task.getGscFlowGroupId(),
					null, serviceId);
		}else {
			componentAndAttributeService.saveFlowGroupAttriputeWithAdditionalParam("patchCallFaliure",
					componentAndAttributeService.getErrorMessageDetails(errorMsg, status), task.getGscFlowGroupId(),
					null, serviceId);
		}
	}
	

	private void setFaliureStatusToFlowable(Task task, DelegateExecution execution) {
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-patch")) {
			execution.setVariable("repcDidNewNumStatus", "failure");
		}else {
			execution.setVariable("didRepcPortingStatus", "failure");
		}
	}

	private void saveFaliureStatusToScAsset(List<ScAsset> scAssets,Task task) {
		if(scAssets != null && !scAssets.isEmpty()) {
		for (ScAsset scAsset : scAssets) {
			if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-patch")) {
				scAsset.setStatus("did-porting-number-patch-jeopardy");
			} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-order-creation-repc")) {
				scAsset.setStatus("did-new-number-patch-jeopardy");

			}
			scAssetRepository.save(scAsset);
			saveOrUpdateScAssetAttribute(scAsset, "patchDidNumber", "Faliure");
		}
		}
	}

	private DidNumberCustRequest constructUpdateDidNumberRequest(List<ScAsset> scAssets,ScServiceDetail scServiceDetail,Map<String, String> scAttributesMap) {
        DidNumberCustRequest didNumberCustRequest=new DidNumberCustRequest();
        didNumberCustRequest.setCountryAbbr(scServiceDetail.getSourceCountryCodeRepc());
        didNumberCustRequest.setCityAbbr(scAttributesMap.get("cityName"));
        didNumberCustRequest.setNumbers(constructDidNumber(scAssets));
        return didNumberCustRequest;
    }

	private List<DidNumber> constructDidNumber(List<ScAsset> scAssets) {
        List<DidNumber> didNumbers=new ArrayList<>();
        scAssets.forEach(asset -> {
        	DidNumber didNumber=new DidNumber();
        	if(asset.getScAssetAttributes().stream().filter(scAssetAttribute -> scAssetAttribute.getAttributeName().equalsIgnoreCase("didNumberId")).findFirst().isPresent()) {
        		didNumber.setDidNumberId(asset.getScAssetAttributes().stream().filter(scAssetAttribute -> scAssetAttribute.getAttributeName().equalsIgnoreCase("didNumberId")).findFirst().get().getAttributeValue()+" 00:00:00");
			}
        	      	
        	if(asset.getScAssetAttributes().stream().filter(scAssetAttribute -> scAssetAttribute.getAttributeName().equalsIgnoreCase("billingDate")).findFirst().isPresent()) {
				didNumber.setInServiceDate(asset.getScAssetAttributes().stream().filter(scAssetAttribute -> scAssetAttribute.getAttributeName().equalsIgnoreCase("billingDate")).findFirst().get().getAttributeValue()+" 00:00:00");
			}
			didNumbers.add(didNumber);
        });
        return didNumbers;
    }

	private String processPatchDidNumberResponse(DidNumberCustRequest patchDidNumberResponse, List<ScAsset> scAssets,Task task) {
		boolean isSuccess = true;
        Logger.info("Inside processPatchDidNumberResponse:{} ", task.getServiceId());

		if (patchDidNumberResponse != null) {
			Map<String, String> statusMap = new HashMap<>();

			for (DidNumber didNumber : patchDidNumberResponse.getNumbers()) {
				if(!statusMap.containsKey(didNumber.getDidNumberWithoutCC())) {
					statusMap.put(didNumber.getDidNumberWithoutCC(), didNumber.getStatus());
				}
			}
	        Logger.info("Inside processPatchDidNumberResponse statusMap:{} ", statusMap);

			for (ScAsset asset : scAssets) {
				String key = asset.getName();
				if (statusMap.containsKey(key) && statusMap.get(key).equalsIgnoreCase("success")) {
			        Logger.info("Inside success statusMap key:{} ", key);

					saveOrUpdateScAssetAttribute(asset, "patchDidNumber", "Success");
				
					asset.setStatus("DID-REPC-PATCH_COMPLETED");
					scAssetRepository.save(asset);
				}else {
					if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-patch")) {
				        Logger.info("Inside success statusMap did-porting-number-patch key:{} ", key);


						asset.setStatus("did-porting-number-patch-jeopardy");
					} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-order-creation-repc")) {
				        Logger.info("Inside success statusMap did-new-number-order-creation-repc key:{} ", key);

						asset.setStatus("did-new-number-patch-jeopardy");

					}
					saveOrUpdateScAssetAttribute(asset, "patchDidNumber", "Faliure");
					isSuccess = false;
				}
			}
			return isSuccess ? "Success" : "Faliure";
		}else {
			
			return "Faliure";
		}
	}
    
    private void saveOrUpdateScAssetAttribute(ScAsset scAsset, String attributeName, String value) {
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
}
