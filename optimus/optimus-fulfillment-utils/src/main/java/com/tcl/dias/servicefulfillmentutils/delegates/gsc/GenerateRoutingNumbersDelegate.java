package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.ArrayList;
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
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.custom.IGscScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.AuditLog;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.GetRoutingNumberBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SupplierBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SuppliersBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/*This delegate is used to generate Routing Number*/
@Component("GenerateRoutingNumbersDelegate")
public class GenerateRoutingNumbersDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(GenerateRoutingNumbersDelegate.class);
	
	@Autowired
	RestClientService restClientService;

	@Autowired
	GscService gscService;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Value("${gsc.getroutingnumber.url}")
	private String getRoutingNumberUrl;
	
	@Value("${gsc.getroutingnumber.authorization}")
	private String getRoutingNumberAuthorization;

	public void execute(DelegateExecution execution) {
		try {
			HashMap<SupplierBean, GetRoutingNumberBean> routingNumList = new HashMap<SupplierBean, GetRoutingNumberBean>();
			Map<String, Object> executionVariables = execution.getVariables();
			logger.info("Inside generate routing numbers delegate variables {}", executionVariables);
			Task task = workFlowService.processServiceTask(execution);
			String serviceCode = (String) executionVariables.get(SERVICE_CODE);
			Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
			String offeringName = "";
			if(Objects.nonNull(executionVariables.get(MasterDefConstants.OFFERING_NAME))) {
				offeringName = (String) executionVariables.get(MasterDefConstants.OFFERING_NAME);
			}
			List<IGscScAsset> routingOutpulse = gscService.getRoutingOutpulse(serviceId, offeringName);
			if(Objects.nonNull(executionVariables.get(GscConstants.KEY_GSC_FLOW_GROUP_ID))) {
				Integer gscFlowGroupId = (Integer) executionVariables.get(GscConstants.KEY_GSC_FLOW_GROUP_ID);
				List<Integer> gscFlowGroupToAsset = gscService.getGscFlowGroupToAsset(gscFlowGroupId);
				List<IGscScAsset> tmpToBeRemoved = new ArrayList<IGscScAsset>();
				for(IGscScAsset rOutpulse : routingOutpulse) {
					if(!gscFlowGroupToAsset.contains(rOutpulse.getAssetId())) {
						tmpToBeRemoved.add(rOutpulse);
					}
				}
				routingOutpulse.removeAll(tmpToBeRemoved);
			}
			logger.info("Generate gouting numbers outpulse {}", routingOutpulse);
			SuppliersBean vasSupplierBean = gscService.getSuppliersSelected(serviceId, task.getMstTaskDef().getKey().toLowerCase());
			logger.info("Generate gouting numbers selected suppliers {}", Utils.toJson(vasSupplierBean));
			Integer code = 0;
			if(vasSupplierBean != null && vasSupplierBean.getSuppliers() !=null) {
				for(SupplierBean supplierBean : vasSupplierBean.getSuppliers()) {
					code = requestForGenerateRoutingNumbers(0, serviceCode, serviceId, execution, routingOutpulse.size(), supplierBean, routingNumList);
					if(code < 1)
						break;
				}
				if(code > 0) {
					gscService.persistReservedRoutingNumber(serviceId, routingNumList, routingOutpulse, "admin");
					execution.setVariable(GscConstants.KEY_GENERATE_ROUTING_NUMBER_STATUS, GscConstants.VALUE_SUCCESS);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in Generate Routing Numbers {}", e);
		}
		workFlowService.processServiceTaskCompletion(execution);
	}
	
	private Integer requestForGenerateRoutingNumbers(Integer count, String serviceCode, Integer serviceId, DelegateExecution execution, Integer requiredQty, SupplierBean supplierBean, HashMap<SupplierBean, GetRoutingNumberBean> routingNumList) throws TclCommonException {
		if(count < GscConstants.API_MAX_RETRY_COUNT) {
	
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			headers.set("Accept", "application/json");
			headers.set("Authorization", getRoutingNumberAuthorization);
	
			Map<String, String> inputParams = constructRequestForGenerateRoutingNumbers(execution.getVariables(), supplierBean, requiredQty);
			logger.info("Generate routing numbers - request {}", inputParams);
			AuditLog auditLog = gscService.saveAuditLog(Utils.convertObjectToJson(inputParams), null,
					serviceCode, "GenerateRoutingNumbers", execution.getProcessInstanceId());

			RestResponse response = restClientService.getWithQueryParam(getRoutingNumberUrl, inputParams, headers);
			logger.info("Generate routing numbers - response {}", response);
			gscService.updateAuditLog(auditLog, Utils.toJson(response));
			if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
				GetRoutingNumberBean getRoutingNumberBean = Utils.fromJson(response.getData(), new TypeReference<GetRoutingNumberBean>() {});
				routingNumList.put(supplierBean, getRoutingNumberBean);
				execution.setVariable(GscConstants.KEY_IS_ROUTING_NUMBER_REQ_STATUS, "yes");
				return 1;
			} else if(response.getErrorMessage().contains("Routing Number is Not Applicable") || (response.getData() != null && response.getData().contains("Routing Number is Not Applicable"))) {
				routingNumList.put(supplierBean, constructNARoutingNumber(requiredQty));
				return 1;
			} else {
				try {
					logger.info("Generate routing numbers - error log started");
					componentAndAttributeService.updateAdditionalAttributes(serviceId,
							"GenerateRoutingNumbersCallFailureReason",
							componentAndAttributeService.getErrorMessageDetails(response.getErrorMessage(), response.getStatus().toString()),
							AttributeConstants.ERROR_MESSAGE, "gsc-routing-generation");
				} catch (Exception e) {
					logger.error("Generate routing numbers - error message details {}", e);
				}
			}
			return -1;
		}
		return 0;
	}
	
	public Map<String, String> constructRequestForGenerateRoutingNumbers(Map<String, Object> variables, SupplierBean supplierBean, Integer requiredQty) {
		Map<String, String> inputParams = new HashMap<>();
		inputParams.put("supplierId", supplierBean.getSupplierId()+"");
		inputParams.put("provisioningServiceAbbr", supplierBean.getProvisioningServiceAbbr());
		if(Objects.nonNull(variables.get(GscConstants.CUSTOMER_ORG_ID))) {
			inputParams.put("customerId", (String) variables.get(GscConstants.CUSTOMER_ORG_ID));
		}
		if(Objects.nonNull(variables.get(GscConstants.SERVICE_TYPE_REPC))) {
			inputParams.put("contractServiceAbbr", (String) variables.get(GscConstants.SERVICE_TYPE_REPC));
		}
		if(supplierBean.getStartDigit() != null && !supplierBean.getStartDigit().isEmpty()) {
			inputParams.put("startDigits", supplierBean.getStartDigit());
		}
		inputParams.put("quantity", requiredQty+"");
		return  inputParams;
	}
	
	public GetRoutingNumberBean constructNARoutingNumber(Integer requiredQty) {
		GetRoutingNumberBean getRoutingNumberBean = new GetRoutingNumberBean();
		List<String> routingNo = new ArrayList<String>();
		for (int i = 0; i < requiredQty; i++) {
			routingNo.add("N/A");
		}
		getRoutingNumberBean.setRoutingNo(routingNo);
		return getRoutingNumberBean;
	}
}
