package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.LinkedHashMap;
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
import com.tcl.dias.servicefulfillmentutils.beans.gsc.ConfigureNmbrMappingBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;

/**
 * ConfigureNumberMappingDelegate class is used to call the NAS to provision the
 * number in Sonus
 *
 * @author Venkata Naga Sai S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Component("ConfigureNumberMappingDelegate")
public class ConfigureNumberMappingDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(ConfigureNumberMappingDelegate.class);

	@Autowired
	RestClientService restClientService;

	@Autowired
	GscService gscService;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Value("${gsc.confignmbrmapping.url}")
	private String configNmbrMappingUrl;
	
	@Value("${gsc.confignmbrmapping.authorization}")
	private String confignmbrMappingAuthorization;
	
	@Value("${gsc.confignmbrmapping.zauthorization}")
	private String confignmbrMappingzAuthorization;

	@Override
	public void execute(DelegateExecution execution) {
		try {
			Map<String, Object> executionVariables = execution.getVariables();
			logger.info("Inside Configure Number Mapping delegate variables {}", executionVariables);
			String serviceCode = (String) executionVariables.get(SERVICE_CODE);
			Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);

			String nasUrl = configNmbrMappingUrl;
			//String basicAuth = GscConstants.CONFIG_NMBR_MAPPING_URL_BASIC_AUTH;
			//String zAuth = GscConstants.CONFIG_NMBR_MAPPING_URL_Z_AUTH;

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			headers.set("Accept", "application/json");
			headers.set("Authorization", confignmbrMappingAuthorization);
			headers.set("Host", "");
			//headers.set("Authorization", basicAuth);
			headers.set("zAuth", confignmbrMappingzAuthorization);

			Map<String, String> putReqHeader = new LinkedHashMap<>();
			putReqHeader.put("Content-Type", "application/json");
			putReqHeader.put("Accept", "application/json");
			putReqHeader.put("Authorization", confignmbrMappingAuthorization);
			putReqHeader.put("Host", "");
			//putReqHeader.put("Authorization", basicAuth);
			putReqHeader.put("zAuth", confignmbrMappingzAuthorization);

			Map<String, Object> configNmbrMappingBeansByCallType = gscService
					.createConfigNmbrMappingRequest(executionVariables, serviceCode, serviceId);
			boolean status = true;
			for (String key : configNmbrMappingBeansByCallType.keySet()) {

				List<Map<String, Object>> configureNmbrMappingBeans = (List<Map<String, Object>>) configNmbrMappingBeansByCallType.get(key);
				String request = Utils.convertObjectToJson(configureNmbrMappingBeans);
				
				String type = ("ConfigureNumberMapping");
				if (key.contains("POST")) {
					type = type + "_POST";
				} else {
					type = type + "_PUT";
				}
				
				if(executionVariables.get(GscConstants.KEY_GSC_FLOW_GROUP_ID) != null) {
					type = type + "-" + executionVariables.get(GscConstants.KEY_GSC_FLOW_GROUP_ID);
				}
				
				AuditLog auditLog = gscService.saveAuditLog(request, null,
						serviceCode, type, execution.getProcessInstanceId());

				logger.info(" Request ::::::::::::::::: {} ", request);

				RestResponse response = null;
				if (key.contains("POST")) {
					response = restClientService.postWithProxy(nasUrl, request, headers);
				} else {
					response = restClientService.put(nasUrl, request, putReqHeader);
				}
				logger.info("NAS Response for serviceCode : {}", response);
				gscService.updateAuditLog(auditLog, Utils.toJson(response));

				if (response != null && Status.SUCCESS.equals(response.getStatus())) {
					logger.info("Configure Number Mapping Response from NAS - SUCCESS :: {}", response.getData());
					List<ConfigureNmbrMappingBean> configureNmbrMappingBean = Utils.fromJson(response.getData(),
							new TypeReference<List<ConfigureNmbrMappingBean>>() {
							});
				} else {
					status = false;
					try {
						logger.info("Configure Number Mapping - error log started");
						componentAndAttributeService.updateAdditionalAttributes(serviceId,
								"ConfigureNumberMappingCallFailureReason",
								componentAndAttributeService.getErrorMessageDetails(response.getErrorMessage(), response.getStatus().toString()),
								AttributeConstants.ERROR_MESSAGE, "gsc-config-number");
					} catch (Exception e) {
						logger.error("Configure Number Mapping - error message details {}", e);
					}
					logger.info("Configure Number Mapping Response from NAS - Failed :: {}", serviceCode);
				}
			}
			if(status) {
				execution.setVariable(GscConstants.KEY_CONFIGURE_NUMBER_MAPPING_STATUS, GscConstants.VALUE_SUCCESS);
			}
		} catch (Exception e) {
			logger.error("Exception in Configure Number Mapping {}", e);
		}
	}
}
