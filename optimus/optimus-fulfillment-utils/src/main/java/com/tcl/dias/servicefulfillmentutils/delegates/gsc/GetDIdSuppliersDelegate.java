package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

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
import com.tcl.dias.servicefulfillment.entity.entities.AuditLog;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DIDSuppliersBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component("GetDIdSuppliersDelegate")
public class GetDIdSuppliersDelegate implements JavaDelegate{

	private static final Logger logger = LoggerFactory.getLogger(GetDIdSuppliersDelegate.class);
	
	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	RestClientService restClientService;

	@Autowired
	GscService gscService;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Value("${gsc.getdidsupplier.url}")
	private String getSupplierUrl;	
	
	@Value("${gsc.getdidsupplier.authorization}")
	private String getSupplierAuthorization;

	@Override
	public void execute(DelegateExecution execution) {
		try {
			Map<String, Object> executionVariables = execution.getVariables();
			logger.info("Inside get DID suppliers delegate variables {}", executionVariables);
			if(GscConstants.YES.equalsIgnoreCase(String.valueOf(executionVariables.get(GscConstants.CONTAINS_DID_SERVICE)))) {
				workFlowService.processServiceTask(execution);
				String serviceCode = (String) executionVariables.get(SERVICE_CODE);
				Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
				requestForGetDIDSuppliers(0, serviceCode, serviceId, execution);
			}else {
				logger.info("No DID order Found..!");
			}
		} catch (Exception e) {
			logger.error("Exception in get DID suppliers {}", e);
		}
		workFlowService.processServiceTaskCompletion(execution);
	}

	private void requestForGetDIDSuppliers(int count, String serviceCode, Integer serviceId, DelegateExecution execution) throws TclCommonException {
		if(count < GscConstants.API_MAX_RETRY_COUNT) {	
			HttpHeaders header = new HttpHeaders();
			header.set("Content-Type", "application/json");
			header.set("Authorization", getSupplierAuthorization);
			header.set("Host", "");
			
			Map<String, String> inputParams = constructRequestForGetDIDSuppliers(serviceId, execution.getVariables());
			if(!inputParams.isEmpty()) {
				logger.info("Get DID Suppliers - request {}", inputParams);
				AuditLog auditLog = gscService.saveAuditLog(Utils.convertObjectToJson(inputParams), null,
						serviceCode, "GetDIDSuppliers", execution.getProcessInstanceId());
				RestResponse response = restClientService.getWithQueryParam(getSupplierUrl, inputParams, header);
				logger.info("Get DID Suppliers - response {}", response);
				gscService.updateAuditLog(auditLog, Utils.toJson(response));
				if (response.getStatus() == Status.SUCCESS) {
					@SuppressWarnings("unused")
					DIDSuppliersBean didSuppliersBean = Utils.fromJson(response.getData(), new TypeReference<DIDSuppliersBean>() {});
					gscService.persistGetDIDSuppliersResponse(serviceId, response.getData());
					execution.setVariable(GscConstants.KEY_GET_DID_SUPPLIER_STATUS, GscConstants.VALUE_SUCCESS);
				} else {
					try {
						logger.info("Get DID Suppliers - error log started");
						componentAndAttributeService.updateAdditionalAttributes(serviceId,
								"GetDIDSuppliersCallFailureReason",
								componentAndAttributeService.getErrorMessageDetails(response.getErrorMessage(), response.getStatus().toString()),
								AttributeConstants.ERROR_MESSAGE, "gsc-select-suppliers");
					} catch (Exception e) {
						logger.error("Get DID Suppliers - error message details {}", e);
					}
				}
			}
		}
	}

	private Map<String, String> constructRequestForGetDIDSuppliers(Integer serviceId, Map<String, Object> variables) {
		Map<String, String> inputParams = new HashMap<>();
	//	String serviceTypeRepc = "";
		String originCountryCode = "";
		
		List<ScServiceDetail> scServiceDetails = gscService.getChildServiceDetailForDID(GscConstants.DOMESTIC_VOICE, String.valueOf(serviceId));
		ScServiceDetail scServiceDetail = null;
		if(Objects.nonNull(scServiceDetails) && !scServiceDetails.isEmpty()) {
			scServiceDetail = scServiceDetails.get(0);
			originCountryCode = scServiceDetail.getSourceCountryCodeRepc();
	//		serviceTypeRepc = gscService.getServiceAttrValue(scServiceDetail, GscConstants.SERVICE_TYPE_REPC);
		
		
			if(Objects.nonNull(originCountryCode)) {
				inputParams.put("countryAbbr", originCountryCode);
			}
			if(Objects.nonNull(variables.get(GscConstants.CUSTOMER_ORG_ID))) {
				inputParams.put("customerId", (String) variables.get(GscConstants.CUSTOMER_ORG_ID)); 
			}
		
			inputParams.put("contractServiceAbbr", GscConstants.DID_SERV_ABBR);
			
			if(Objects.nonNull(variables.get(GscConstants.SERVICE_TYPE_REPC))) {
				String serviceType = (String) variables.get(GscConstants.SERVICE_TYPE_REPC);
				if(!serviceType.isEmpty() && serviceType.toUpperCase().contains("EUCDRDD")) {
					inputParams.put("contractServiceAbbr", "EUCDRDD");				
				}
			}
			
			inputParams.put("includeSharedInCircuitGroups", GscConstants.Y);
		}
		return  inputParams;
	}
	
}
