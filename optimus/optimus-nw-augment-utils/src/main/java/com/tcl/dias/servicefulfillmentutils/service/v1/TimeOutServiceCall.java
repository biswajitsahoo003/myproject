package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.networkaugment.entity.entities.ScComponentAttribute;
import com.tcl.dias.networkaugment.entity.entities.ScServiceDetail;
import com.tcl.dias.networkaugment.entity.entities.Task;
import com.tcl.dias.networkaugment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.networkaugment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.networkaugment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class TimeOutServiceCall {
	private static final Logger logger = LoggerFactory.getLogger(TimeOutServiceCall.class);


	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	public static final String TIME_OUT_ERROR = " response timed out.";
	public static final String TIME_OUT_CODE = "504";
	public static final String CRAMER = "Cramer";
	public static final String NETP = "NetP";
	public static final String WPS = "WPS";

	@Autowired
	TaskService taskService;
	
	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	NetworkAugmentationWorkFlowService workFlowService;
	
	  @Autowired
	    FlowableBaseService flowableBaseService;


	@Transactional(readOnly = false)
	public void processTimeOutTask(DelegateExecution execution) throws TclCommonException {

		Map<String, Object> processMap = execution.getVariables();
		Integer serviceId = (Integer) processMap.get(MasterDefConstants.SERVICE_ID);
		
		logger.info("processTimeOutTask serviceId :{}",serviceId);
		logger.info("TimeOutServiceCall  with current activity:{}",execution.getCurrentActivityId());

		Optional<ScServiceDetail> optionalServiceDetails = scServiceDetailRepository.findById(serviceId);

		ScServiceDetail scServiceDetail = optionalServiceDetails.get();		
		
		if (execution.getCurrentActivityId().equalsIgnoreCase("get-ip-service-info-async-wait-time")) {		
						
			execution.setVariable("getIpServiceSyncCallCompleted", true);
			execution.setVariable("getIpServiceSuccess", false);
			
			
			if(processMap.get("getIpServiceRetryAttempt")==null) {
				logger.info("processTimeOutTask serviceId :{} getIpServiceRetryAttempt 1",serviceId);
				execution.setVariable("getIpServiceRetryAttempt", 1);
			}else {
				Integer getIpServiceRetryAttempt = (Integer)processMap.get("getIpServiceRetryAttempt");
				getIpServiceRetryAttempt = getIpServiceRetryAttempt +1;
				logger.info("processTimeOutTask serviceId :{} getIpServiceRetryAttempt {}",serviceId,getIpServiceRetryAttempt);
				execution.setVariable("getIpServiceRetryAttempt", getIpServiceRetryAttempt);
			}
			execution.setVariable("getIpServiceSuccess", false);
			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "serviceDesignIpInfoCallFailureReason",
					componentAndAttributeService.getErrorMessageDetails(CRAMER+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "get-ip-service-info-async");
			
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"get-ip-service-info-async",CRAMER+TIME_OUT_ERROR);
			
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("Set-mfd-clr-async-wait-time")) {			
			
			execution.setVariable("setMfdCLRSuccess", false);
			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "setMFDCLRCallFailureReason",
					componentAndAttributeService.getErrorMessageDetails(CRAMER+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "Set-mfd-clr-async");
			
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"Set-mfd-clr-async",CRAMER+TIME_OUT_ERROR);
			
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("Set-clr-async-wait-time")) {			
			
			execution.setVariable("setCLRSuccess", false);
			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "setCLRCallFailureReason",
					componentAndAttributeService.getErrorMessageDetails(CRAMER+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "Set-clr-async");
			
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"Set-clr-async",CRAMER+TIME_OUT_ERROR);
		
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("get-tx-downtime-info-async-wait-time")) {
			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "txDownTimeAsyncErrorMessage",
					componentAndAttributeService.getErrorMessageDetails(CRAMER+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "get-tx-downtime-info-async");
			execution.setVariable("isTxDownTimeCallSuccess", false);
			execution.setVariable("getTxDownTimeSuccess", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"get-tx-downtime-info-async",CRAMER+TIME_OUT_ERROR);

		}else if (execution.getCurrentActivityId().equalsIgnoreCase("get-ip-release-info-async-wait-time")) {
			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "serviceDesignReleaseIpInfoCallFailureReason",
					componentAndAttributeService.getErrorMessageDetails(CRAMER+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "get-release-dummy-wan-ip-async");
			
			execution.setVariable("releaseDummyWANIPDummySyncCallSuccess", false);
			execution.setVariable("getReleaseDummyIpServiceSuccess", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"get-release-dummy-wan-ip-async",CRAMER+TIME_OUT_ERROR);

		}else if (execution.getCurrentActivityId().equalsIgnoreCase("get-ip-assign-info-async-wait-time")) {
			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "serviceDesignAssignIpInfoCallFailureReason",
					componentAndAttributeService.getErrorMessageDetails(CRAMER+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "get-assign-dummy-wan-ip-async");
			
			execution.setVariable("assignDummyWANIPDummySyncCallSuccess", false);
			execution.setVariable("getAssignDummyIpServiceSuccess", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"get-assign-dummy-wan-ip-async",CRAMER+TIME_OUT_ERROR);

		}else if (execution.getCurrentActivityId().equalsIgnoreCase("release-dummy-service-configuration-config-async-wait-time")) {
			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "release-dummy-service-configuration",
					componentAndAttributeService.getErrorMessageDetails(NETP+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "release-dummy-service-configuration");
			execution.setVariable("releaseDummyServiceConfigurationSuccess", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"release-dummy-service-configuration-config-async",NETP+TIME_OUT_ERROR);
			
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("assign-dummy-service-configuration-config-async-wait-time")) {
			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "assign-dummy-service-configuration",
					componentAndAttributeService.getErrorMessageDetails(NETP+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "assign-dummy-service-configuration");
			
			execution.setVariable("assignDummyServiceConfigurationSuccess", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"assign-dummy-service-configuration-config-async",NETP+TIME_OUT_ERROR);
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("assign-rf-dummy-service-configuration-config-async-wait-time")) {
			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "assign-rf-dummy-service-configuration",
					componentAndAttributeService.getErrorMessageDetails(NETP+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "assign-rf-dummy-service-configuration");
			execution.setVariable("assignRfDummyServiceConfigurationSuccess", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"assign-rf-dummy-service-configuration-config-async",NETP+TIME_OUT_ERROR);
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("service-configuration-ack-async-wait-time")
				|| execution.getCurrentActivityId().equalsIgnoreCase("service-configuration-config-async-wait-time")) {

			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "ipconfigFailureReason",
					componentAndAttributeService.getErrorMessageDetails(NETP+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "service-configuration");
			
			execution.setVariable("serviceConfigurationSuccess", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"service-configuration-config-async",NETP+TIME_OUT_ERROR);

		} else if (execution.getCurrentActivityId()
				.equalsIgnoreCase("preview-service-configuration-config-async-wait-time")
				|| execution.getCurrentActivityId().equalsIgnoreCase("preview-service-configuration-config-async")) {
			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "ipconfigFailureReason",
					componentAndAttributeService.getErrorMessageDetails(NETP+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "preview-service-configuration");
			
			execution.setVariable("previewIpConfigSuccess", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"preview-service-configuration-config-async",NETP+TIME_OUT_ERROR);

		} else if (execution.getCurrentActivityId().equalsIgnoreCase("cancel-service-configuration-config-async-wait-time")) {

			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "ipconfigFailureReason",
					componentAndAttributeService.getErrorMessageDetails(NETP+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "cancel-service-configuration");
			
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"cancel-service-configuration-config-async",NETP+TIME_OUT_ERROR);

		}else if (execution.getCurrentActivityId().equalsIgnoreCase("ip-terminate-configuration-config-async-wait-time")) {

			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "ipconfigFailureReason",
					componentAndAttributeService.getErrorMessageDetails(NETP+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "ip-terminate-configuration");
			
			execution.setVariable("ipTerminateConfigurationSuccess", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"ip-terminate-configuration-config-async",NETP+TIME_OUT_ERROR);

		}  else if (execution.getCurrentActivityId().equalsIgnoreCase("get-clr-async-wait-time")) {
			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "serviceDesignTxCallFailureReason",
					componentAndAttributeService.getErrorMessageDetails(CRAMER+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "get-clr");
			
			execution.setVariable("getCLRSuccess", false);	 
			
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"get-clr-async",CRAMER+TIME_OUT_ERROR);

		} else if (execution.getCurrentActivityId().equalsIgnoreCase("tx-sdh-configuration-ack-async-wait-time")
				|| execution.getCurrentActivityId().equalsIgnoreCase("tx-sdh-configuration-status-async-wait-time")) {
			
			execution.setVariable("txSDHConfigurationSuccess", false);			 

			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "txSdhConfigFailureReason",
					componentAndAttributeService.getErrorMessageDetails(NETP+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "tx-configuration-sdh");
			
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"tx-sdh-configuration-status-async",NETP+TIME_OUT_ERROR);
			
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("tx-mpls-configuration-ack-async-wait-time")
				|| execution.getCurrentActivityId().equalsIgnoreCase("tx-mpls-configuration-status-async-wait-time")) {
			
			execution.setVariable("txMPLSConfigurationSuccess", false);

			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "txMplsConfigFailureReason",
					componentAndAttributeService.getErrorMessageDetails(NETP+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "tx-configuration-mpls");
			
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"tx-mpls-configuration-status-async",NETP+TIME_OUT_ERROR);
			
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("get-mux-info-async-wait-time")) {

			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "muxInfoErrorMessage",
					componentAndAttributeService.getErrorMessageDetails(CRAMER+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "get_mux_info_async");
			
			execution.setVariable("isMuxInfoAvailable", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"get-mux-info-async",CRAMER+TIME_OUT_ERROR);
			
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("enrich-service-design-async-wait-time")) {

			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "serviceDesignCallFailureReason",
					componentAndAttributeService.getErrorMessageDetails(CRAMER+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "enrich-service-design");
			
			execution.setVariable("serviceDesignCompleted", false);
			execution.setVariable("isCLRSyncCallSuccess", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"enrich-service-design-async",CRAMER+TIME_OUT_ERROR);
			
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("product_commissioning_async-wait-time")) {

			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "product_commissioning_error",
					componentAndAttributeService.getErrorMessageDetails(WPS+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "product_commissioning");
			
			execution.setVariable("productCommissioningStatus", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"product_commissioning_async",WPS+TIME_OUT_ERROR);
			
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("cpe_product_commissioning_async-wait-time")) {

			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "cpe_product_commissioning_error",
					componentAndAttributeService.getErrorMessageDetails(WPS+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "cpe_product_commissioning");
			
			execution.setVariable("cpeProductCommissioningStatus", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"cpe_product_commissioning_async",WPS+TIME_OUT_ERROR);
			
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("cpe_billing_account_creation_async-wait-time")) {

			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "cpe_account_creation_error",
					componentAndAttributeService.getErrorMessageDetails(WPS+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "cpe_account_creation");
			
			execution.setVariable("cpeAccountCreationStatus", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"cpe_billing_account_creation_async",WPS+TIME_OUT_ERROR);
			
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("billing_account_creation_async-wait-time")) {

			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "billing_account_creation_error",
					componentAndAttributeService.getErrorMessageDetails(WPS+TIME_OUT_ERROR, TIME_OUT_CODE),
					AttributeConstants.ERROR_MESSAGE, "billing_account_creation");
			
			execution.setVariable("billingAccountCreationStatus", false);
			workFlowService.processServiceTaskCompletionByTaskKey(execution,"billing_account_creation_async",WPS+TIME_OUT_ERROR);
			
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("deemed-service-acceptance")) {
			
			Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceId,"service-acceptance");
			
			Map<String, String> mapper = new HashMap<>();
			mapper.put("deemedAcceptance", "Yes");			
			mapper.put("customerAcceptanceDate",DateUtil.convertDateToString(new Date()));		

//			componentAndAttributeService.updateAttributes(scServiceDetail.getId(), mapper,AttributeConstants.COMPONENT_LM,task.getSiteType());
			
			flowableBaseService.taskDataEntry(task, mapper);
			taskService.processTaskLogDetails(task,TaskLogConstants.CLOSED,"Accepted by System",null);			
			
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("ip-downtime-trigger")) {
			logger.info("ip-downtime-trigger");
			execution.setVariable("serviceConfigurationAction", "ACTIVITY_GO_AHEAD");
		}else if (execution.getCurrentActivityId().equalsIgnoreCase("tx-downtime-trigger")) {
			logger.info("tx-downtime-trigger");
			execution.setVariable("txConfigAction", "ACTIVITY_GO_AHEAD");
		}
		
	}

}
