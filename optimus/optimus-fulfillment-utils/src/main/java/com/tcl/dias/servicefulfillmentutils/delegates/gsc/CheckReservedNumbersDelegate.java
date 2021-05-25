package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.HashMap;
import java.util.Map;

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
import com.tcl.dias.servicefulfillmentutils.beans.gsc.GetVanityNumberBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/* This class is used for checking the reservation validity*/
@Component("CheckReservedNumbersDelegate")
public class CheckReservedNumbersDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(CheckReservedNumbersDelegate.class);
	
	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	RestClientService restClientService;

	@Autowired
	GscService gscService;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Value("${gsc.getvanitynumbers.url}")
	private String getvanityNumbersUrl;
	
	@Value("${gsc.getvanitynumbers.authorization}")
	private String getvanityNumbersAuthorization;

	public void execute(DelegateExecution execution) {
		try {
			Map<String, Object> executionVariables = execution.getVariables();
			logger.info("Inside check reserved number delegate variables {}", executionVariables);
			workFlowService.processServiceTask(execution);
			String serviceCode = (String) executionVariables.get(SERVICE_CODE);
			Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
			if(gscService.isReservedNoAvailable(serviceId)) {
				requestForCheckReserveNo(0, serviceCode, serviceId, execution);
			} else {
				logger.info("Check reserved number - no reservation found");
				execution.setVariable(GscConstants.KEY_IS_RESERVATION_EXPIRED, GscConstants.VALUE_NO_RESERVATION);
				execution.setVariable(GscConstants.KEY_IS_GET_NUMBER_FLOW_REQ, "yes");
				execution.setVariable(GscConstants.KEY_CHECK_RESERVED_NUMBERS_STATUS, GscConstants.VALUE_SUCCESS);
			}
			if(gscService.isPortingNoAvailable(serviceId)) {
				execution.setVariable(GscConstants.KEY_IS_SUPPLIER_FLOW_REQ, "yes");
			} else {
				execution.setVariable(GscConstants.KEY_IS_SUPPLIER_FLOW_REQ, "no");
			}
		} catch (Exception e) {
			//execution.setVariable("checkRezNoStatus", "failed");
			logger.error("Exception in check reserve number {}", e);
		}
		workFlowService.processServiceTaskCompletion(execution);
	}
	
	private void requestForCheckReserveNo(Integer count, String serviceCode, Integer serviceId, DelegateExecution execution) throws TclCommonException {
		if(count < GscConstants.API_MAX_RETRY_COUNT) {
			logger.info("Check reserved number - try count {}", count);
			
			Map<String, Object> executionVariables = execution.getVariables();
			
			HttpHeaders header = new HttpHeaders();
			header.set("Content-Type", "application/json");
			header.set("Authorization", getvanityNumbersAuthorization);
			header.set("Host", "");
			
			Map<String, String> inputParams = constructRequestForCheckReserveNo(serviceId, executionVariables);
			logger.info("Check reserved number - input {}", inputParams);
			AuditLog auditLog = gscService.saveAuditLog(Utils.convertObjectToJson(inputParams), null,
					serviceCode, "CheckReservedNumbers", execution.getProcessInstanceId());
			RestResponse response = restClientService.getWithQueryParam(getvanityNumbersUrl, inputParams, header);
			logger.info("Check reserved number - response {}", response.getData());
			gscService.updateAuditLog(auditLog, Utils.toJson(response));
			if (response.getStatus() == Status.SUCCESS) {
				GetVanityNumberBean getVanityNumberBean = Utils.fromJson(response.getData(), new TypeReference<GetVanityNumberBean>() {});
				if(getVanityNumberBean.getNumInv().isEmpty() || getVanityNumberBean.getMessage().equalsIgnoreCase("No number Found")) {
					logger.info("Check reserved number - reservation expired");
					execution.setVariable(GscConstants.KEY_IS_RESERVATION_EXPIRED, GscConstants.VALUE_RESERVATION_EXPIRED);
					execution.setVariable(GscConstants.KEY_IS_GET_NUMBER_FLOW_REQ, "yes");
				} else {
					//execution.setVariable(GscConstants.KEY_IS_RESERVATION_PROVISION_FLOW_REQ, "yes");
					execution.setVariable(GscConstants.KEY_IS_RESERVATION_EXPIRED, GscConstants.VALUE_RESERVATION_NOT_EXPIRED);
					Integer requiredQty = gscService.getRequiredNumberQty(serviceId);
					Integer blockedQty = getVanityNumberBean.getNumInv().size();
					logger.info("Check reserved number - requiredQty {} blockedQty {}", requiredQty, blockedQty);
					if(requiredQty > blockedQty)
						execution.setVariable(GscConstants.KEY_IS_GET_NUMBER_FLOW_REQ, "yes");
				}
				execution.setVariable(GscConstants.KEY_CHECK_RESERVED_NUMBERS_STATUS, GscConstants.VALUE_SUCCESS);
			} else {
				try {
					logger.info("Check reserved number - error log started");
					componentAndAttributeService.updateAdditionalAttributes(serviceId,
							"CheckReservedNumbersCallFailureReason",
							componentAndAttributeService.getErrorMessageDetails(response.getErrorMessage(), response.getStatus().toString()),
							AttributeConstants.ERROR_MESSAGE, "gsc-check-reserved-numbers");
				} catch (Exception e) {
					logger.error("Check reserved number - error message details {}", e);
				}
			}
		}	
	}
	
	private Map<String, String> constructRequestForCheckReserveNo(Integer serviceId, Map<String, Object> executionVariables) {
		Map<String, String> inputParams = new HashMap<>();
		inputParams.put("reservationId", gscService.getReservationId(serviceId));
		inputParams.put("sortBy", "OriginCountry");
		inputParams.put("sortOrder", "asc");
		if(executionVariables.get(GscConstants.CUSTOMER_ORG_ID) != null) {
			inputParams.put("orgId", (String) executionVariables.get(GscConstants.CUSTOMER_ORG_ID));
		}
		return inputParams;
	}
}
