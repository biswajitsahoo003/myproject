package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import com.tcl.dias.servicefulfillmentutils.beans.gsc.NumInvBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.ReserveInputBodyBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.ReserveVanityNumberBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/*This delegate is used to get available number from REPC number inventory and reserve it*/
@Component("GetAndReserveNumbersDelegate")
public class GetAndReserveNumbersDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(GetAndReserveNumbersDelegate.class);
	
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

	@Value("${gsc.getvanitynumbers.url}")
	private String getvanityNumbersUrl;
	
	@Value("${gsc.getvanitynumbers.authorization}")
	private String getvanityNumbersAuthorization;
	
	@Value("${gsc.putvanitynumbers.url}")
	private String putvanityNumbersUrl;
	
	@Value("${gsc.putvanitynumbers.authorization}")
	private String putvanityNumbersAuthorization;

	public void execute(DelegateExecution execution) {
		try {
			Map<String, Object> executionVariables = execution.getVariables();
			logger.info("Inside get and reserve numbers delegate variables {}", executionVariables);
			workFlowService.processServiceTask(execution);
			String serviceCode = (String) executionVariables.get(SERVICE_CODE);
			Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
			Integer requiredNumberQty = gscService.getRequiredNumberQty(serviceId);
			if(requiredNumberQty > 0) {
				if(executionVariables.get(GscConstants.SERVICE_TYPE) != null) {
					String serviceType = (String) executionVariables.get(GscConstants.SERVICE_TYPE);
					if(serviceType.equals("ACANS") || serviceType.equals("ACDTFS")) {
						execution.setVariable(GscConstants.KEY_GET_AND_RESERVED_NUMBERS_STATUS, GscConstants.VALUE_SUCCESS);
						execution.setVariable(GscConstants.KEY_IS_SUPPLIER_FLOW_REQ, "yes");
					} else if(serviceType.equals("LNS") || serviceType.equals("ACLNS")) {
						requestForGetNumbersCityWise(0, serviceCode, serviceId, execution, requiredNumberQty);
					} else {
						requestForGetNumbers(0, serviceCode, serviceId, execution, requiredNumberQty, null);
					}
				}
			} else {
				logger.info("Get and reserve numbers - no number is required from inventory");
				execution.setVariable(GscConstants.KEY_GET_AND_RESERVED_NUMBERS_STATUS, GscConstants.VALUE_SUCCESS);
			}
		} catch (Exception e) {
			//execution.setVariable("getRezNoStatus ", "failed");
			logger.error("Exception in get and reserve numbers {}", e);
		}
		workFlowService.processServiceTaskCompletion(execution);
	}
	
	private void requestForGetNumbersCityWise(Integer count, String serviceCode, Integer serviceId, DelegateExecution execution, Integer requiredNumberQty) throws TclCommonException {
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId,
				AttributeConstants.COMPONENT_GSCLICENSE, "A", Arrays.asList("cityWiseQuantityOfNumbers", "cityWiseAreaCode"));
		/*HashMap<String, String> cityWiseAreaCodeMap = new HashMap<String, String>();
		String cityWiseAreaCode = scComponentAttributesmap.getOrDefault("cityWiseAreaCode", null);
		if(cityWiseAreaCode != null && !cityWiseAreaCode.isEmpty()) {
			List<String> attrValueList = new ArrayList<String>();
			if(cityWiseAreaCode.startsWith("[")) {
				attrValueList = Utils.convertJsonToObject(cityWiseAreaCode, ArrayList.class);
			} else {
				attrValueList.add(cityWiseAreaCode);
			}
			for(String attribute : attrValueList) {
				String[] split = attribute.split(":");
				for (int i = 1; i < split.length; i++) {
					cityWiseAreaCodeMap.put(split[0], split[1]);
				}
			}
		}*/
		
		String cityWiseQuantityOfNumbers = scComponentAttributesmap.getOrDefault("cityWiseQuantityOfNumbers", null);
		if(cityWiseQuantityOfNumbers != null && !cityWiseQuantityOfNumbers.isEmpty()) {
			List<String> attrValueList = new ArrayList<String>();
			if(cityWiseQuantityOfNumbers.startsWith("[")) {
				attrValueList = Utils.convertJsonToObject(cityWiseQuantityOfNumbers, ArrayList.class);
			} else {
				attrValueList.add(cityWiseQuantityOfNumbers);
			}
			for(String attribute : attrValueList) {
				String[] split = attribute.split(":");
				if(split.length > 1) {
					//requestForGetNumbers(0, serviceCode, serviceId, execution, split.length, split[0], cityWiseAreaCodeMap.get(split[0]));
					requestForGetNumbers(0, serviceCode, serviceId, execution, (split.length - 1), split[0]);
				}
			}
		}
	}
	
	private void requestForGetNumbers(Integer count, String serviceCode, Integer serviceId, DelegateExecution execution, Integer requiredNumberQty, String cityCode) throws TclCommonException {
		if(count < GscConstants.API_MAX_RETRY_COUNT) {
			logger.info("Get and reserve numbers - try count {}", count);
			Map<String, Object> executionVariables = execution.getVariables();
			
			HttpHeaders header = new HttpHeaders();
			header.set("Content-Type", "application/json");
			header.set("Authorization", getvanityNumbersAuthorization);
			header.set("Host", "");
	
			Map<String, String> inputParams = constructRequestForGetNumbers(serviceId, executionVariables, requiredNumberQty, cityCode);
			logger.info("Get and reserve numbers - request {}", inputParams);
			AuditLog auditLog = gscService.saveAuditLog(Utils.convertObjectToJson(inputParams), null,
					serviceCode, "GetAndReserveNumbers-1", execution.getProcessInstanceId());
			RestResponse response = restClientService.getWithQueryParam(getvanityNumbersUrl, inputParams, header);
			logger.info("Get and reserve numbers - response {}", response);
			gscService.updateAuditLog(auditLog, Utils.toJson(response));
			if (response.getStatus() == Status.SUCCESS) {
				GetVanityNumberBean getVanityResponse = Utils.fromJson(response.getData(), new TypeReference<GetVanityNumberBean>() {});
				if(getVanityResponse.getNumInv() != null && !getVanityResponse.getNumInv().isEmpty()) {
					requestForReserveNumbers(0, serviceCode, serviceId, getVanityResponse, execution, requiredNumberQty, cityCode);
				} else {
					execution.setVariable(GscConstants.KEY_GET_AND_RESERVED_NUMBERS_STATUS, GscConstants.VALUE_SUCCESS);
					execution.setVariable(GscConstants.KEY_IS_SUPPLIER_FLOW_REQ, "yes");
				}
			} else {
				try {
					logger.info("Get and reserve numbers - error log started");
					componentAndAttributeService.updateAdditionalAttributes(serviceId,
							"GetAndReserveNumbersCallFailureReason",
							componentAndAttributeService.getErrorMessageDetails(response.getErrorMessage(), response.getStatus().toString()),
							AttributeConstants.ERROR_MESSAGE, "gsc-get-reserve-number");
				} catch (Exception e) {
					logger.error("Get and reserve numbers - error message details {}", e);
				}
			}
		}
	}
	
	private void requestForReserveNumbers(Integer count, String serviceCode, Integer serviceId, GetVanityNumberBean getVanityResponse, DelegateExecution execution, Integer requiredNumberQty, String cityCode) throws TclCommonException {
		if(count < GscConstants.API_MAX_RETRY_COUNT) {
			logger.info("Get and reserve numbers - try count {}", count);
			
			Map<String, Object> executionVariables = execution.getVariables();
			
			Map<String, String> header = new HashMap<>();
			header.put("Content-Type", "application/json");
			header.put("Authorization", putvanityNumbersAuthorization);
			header.put("SourceConsumer", "TATA-OPTIMUS_SYSTEM2020");
			header.put("Host", "");
			
			ReserveInputBodyBean reserveInputBodyBean = constructRequestForReserveNumbers(getVanityResponse, executionVariables);
			String inputBody = Utils.convertObjectToJson(reserveInputBodyBean);
			logger.info("Get and reserve numbers - request {}", inputBody);
			AuditLog auditLog = gscService.saveAuditLog(inputBody, null,
					serviceCode, "GetAndReserveNumbers-2", execution.getProcessInstanceId());
			RestResponse response = restClientService.put(getUrl(putvanityNumbersUrl, new Object[] {executionVariables.get(GscConstants.CUSTOMER_ORG_ID)}), inputBody, header);
			logger.info("Get and reserve numbers - response {}", response);
			gscService.updateAuditLog(auditLog, Utils.toJson(response));
			if (response.getStatus() == Status.SUCCESS) {
				ReserveVanityNumberBean reserveVanityNumberBean = Utils.fromJson(response.getData(), new TypeReference<ReserveVanityNumberBean>() {});
				execution.setVariable("reserveNumberRes", response.getData());
				execution.setVariable(GscConstants.KEY_GET_AND_RESERVED_NUMBERS_STATUS, GscConstants.VALUE_SUCCESS);
				execution.setVariable(GscConstants.KEY_IS_RESERVATION_PROVISION_FLOW_REQ, "yes");
				gscService.persistReservedVanityNumber(serviceId, reserveVanityNumberBean, "admin", cityCode);
				Integer blockedQty = reserveVanityNumberBean.getNumberDetails().size();
				logger.info("Get and reserve numbers - requiredQty {} blockedQty {}", requiredNumberQty, blockedQty);
				if(requiredNumberQty > blockedQty) {
					execution.setVariable(GscConstants.KEY_IS_SUPPLIER_FLOW_REQ, "yes");
				}
			} else {
				try {
					logger.info("Get and reserve numbers - error log started");
					componentAndAttributeService.updateAdditionalAttributes(serviceId,
							"GetAndReserveNumbersCallFailureReason",
							componentAndAttributeService.getErrorMessageDetails(response.getErrorMessage(), response.getStatus().toString()),
							AttributeConstants.ERROR_MESSAGE, "gsc-get-reserve-number");
				} catch (Exception e) {
					logger.error("Get and reserve numbers - error message details {}", e);
				}
			}
		}
	}
	
	public Map<String, String> constructRequestForGetNumbers(Integer serviceId, Map<String, Object> executionVariables, Integer requiredNumberQty, String cityCode) {
		Map<String, String> inputParams = new HashMap<>();
		inputParams.put("sortBy", "e164");
		inputParams.put("sortOrder", "asc");
		inputParams.put("status", "Available");
		//inputParams.put("offset", (String) variables.get("offset"));
		//inputParams.put("offset", "100");
		inputParams.put("limit", requiredNumberQty+"");
		if(executionVariables.get(GscConstants.ORIGIN_COUNTRY_CODE) != null) {
			inputParams.put("originCountry", (String) executionVariables.get(GscConstants.ORIGIN_COUNTRY_CODE));
		}
		
		if(executionVariables.get(GscConstants.SERVICE_TYPE) != null) {
			inputParams.put("serviceType", (String) executionVariables.get(GscConstants.SERVICE_TYPE));
		}
		
		if(executionVariables.get(GscConstants.CUSTOMER_ORG_ID) != null) {
			inputParams.put("orgId", (String) executionVariables.get(GscConstants.CUSTOMER_ORG_ID));
		}
		
		/*if(npaCode != null && !npaCode.isEmpty()) {
			inputParams.put("NPA", npaCode);
		}*/
		
		if(cityCode != null && !cityCode.isEmpty()) {
			inputParams.put("originCity", cityCode);
		}
		return inputParams;
	}
	
	public ReserveInputBodyBean constructRequestForReserveNumbers(GetVanityNumberBean getVanityNumberBean, Map<String, Object> executionVariables) {
		ReserveInputBodyBean reserveInputBodyBean = new ReserveInputBodyBean();
		if(executionVariables.get(GscConstants.SERVICE_TYPE) != null) {
			reserveInputBodyBean.setServiceType((String) executionVariables.get(GscConstants.SERVICE_TYPE));
		}
		List<String> e164 = new ArrayList<>();
		for(NumInvBean numInvBean : getVanityNumberBean.getNumInv()) {
			e164.add(numInvBean.getE164());
			if(reserveInputBodyBean.getOriginCountryCode() == null)
				reserveInputBodyBean.setOriginCountryCode(numInvBean.getOriginCountryCode());
		}
		reserveInputBodyBean.setE164(e164);
		return reserveInputBodyBean;
	}
	
	public String getUrl(String baseUrl, Object[] args) {
		return MessageFormat.format(baseUrl, args);
	}
}
