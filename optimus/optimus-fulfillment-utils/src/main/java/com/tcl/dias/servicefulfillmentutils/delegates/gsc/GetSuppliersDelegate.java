package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;

import java.util.HashMap;
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
import com.tcl.dias.servicefulfillment.entity.entities.AuditLog;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.VasSuppliersBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component("GetSuppliersDelegate")
public class GetSuppliersDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(GetSuppliersDelegate.class);
	
	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	RestClientService restClientService;

	@Autowired
	GscService gscService;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Value("${gsc.getsupplier.url}")
	private String getSupplierUrl;
	
	@Value("${gsc.getsupplier.authorization}")
	private String getSupplierAuthorization;

	public void execute(DelegateExecution execution) {
		try {
			Map<String, Object> executionVariables = execution.getVariables();
			logger.info("Inside get suppliers delegate variables {}", executionVariables);
			workFlowService.processServiceTask(execution);
			String serviceCode = (String) executionVariables.get(SERVICE_CODE);
			Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
			requestForGetSuppliers(0, serviceCode, serviceId, execution);
		} catch (Exception e) {
			logger.error("Exception in get suppliers {}", e);
		}
		workFlowService.processServiceTaskCompletion(execution);
	}
	
	private void requestForGetSuppliers(Integer count, String serviceCode, Integer serviceId, DelegateExecution execution) throws TclCommonException {
		if(count < GscConstants.API_MAX_RETRY_COUNT) {	
			HttpHeaders header = new HttpHeaders();
			header.set("Content-Type", "application/json");
			header.set("Authorization", getSupplierAuthorization);
			header.set("Host", "");
			
			Map<String, String> inputParams = constructRequestForGetSuppliers(execution.getVariables());
			logger.info("Get Suppliers - request {}", inputParams);
			AuditLog auditLog = gscService.saveAuditLog(Utils.convertObjectToJson(inputParams), null,
					serviceCode, "GetSuppliers", execution.getProcessInstanceId());
			RestResponse response = restClientService.getWithQueryParam(getSupplierUrl, inputParams, header);
			logger.info("Get Suppliers - response {}", response);
			gscService.updateAuditLog(auditLog, Utils.toJson(response));
			if (response.getStatus() == Status.SUCCESS) {
				@SuppressWarnings("unused")
				VasSuppliersBean vasSupplierBean = Utils.fromJson(response.getData(), new TypeReference<VasSuppliersBean>() {});
				gscService.persistGetSuppliersResponse(serviceId, response.getData());
				execution.setVariable(GscConstants.KEY_GET_SUPPLIERS_STATUS, GscConstants.VALUE_SUCCESS);
			} else {
				try {
					logger.info("Get Suppliers - error log started");
					componentAndAttributeService.updateAdditionalAttributes(serviceId,
							"GetSuppliersCallFailureReason",
							componentAndAttributeService.getErrorMessageDetails(response.getErrorMessage(), response.getStatus().toString()),
							AttributeConstants.ERROR_MESSAGE, "gsc-select-suppliers");
				} catch (Exception e) {
					logger.error("Get Suppliers - error message details {}", e);
				}
			}
		}
	}
	
	public Map<String, String> constructRequestForGetSuppliers(Map<String, Object> variables) {
		Map<String, String> inputParams = new HashMap<>();
		if(Objects.nonNull(variables.get(GscConstants.ORIGIN_COUNTRY_CODE))) {
			inputParams.put("countryAbbr", (String) variables.get(GscConstants.ORIGIN_COUNTRY_CODE));
		}
		if(Objects.nonNull(variables.get(GscConstants.CUSTOMER_ORG_ID))) {
			inputParams.put("customerId", (String) variables.get(GscConstants.CUSTOMER_ORG_ID));
		}
		if(Objects.nonNull(variables.get(GscConstants.SERVICE_TYPE_REPC))) {
			inputParams.put("contractServiceAbbr", (String) variables.get(GscConstants.SERVICE_TYPE_REPC));
		}
		if(Objects.nonNull(variables.get(MasterDefConstants.OFFERING_NAME))) {
			//inputParams.put("acBridgePlatform", (String) variables.get(GscConstants.SERVICE_TYPE_REPC));
		}
		if (Objects.nonNull(variables.get(GscConstants.IS_PARTNER_ORDER))
				&& ((String) variables.get(GscConstants.IS_PARTNER_ORDER)).equals("yes")) {
			inputParams.put("ownerType",
					gscService.getRepcPartnerAbbreviationName((Integer) variables.get(MasterDefConstants.SC_ORDER_ID)));
		} else {
			inputParams.put("ownerType", "TCL");
		}
		
		return  inputParams;
	}
}
