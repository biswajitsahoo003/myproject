package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.PRODUCT_NAME;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;;


/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("assignDummyIpServiceConfigurationDelegate")
public class AssignDummyIPServiceConfigurationDelegate implements JavaDelegate {
		private static final Logger logger = LoggerFactory.getLogger(AssignDummyIPServiceConfigurationDelegate.class);
	
		@Autowired
		MQUtils mqUtils;
	
		@Value("${activation.dummy.ip.configuration}")
		String assignDummyIpConfigurationQueue;
		
		@Autowired
		ScServiceDetailRepository scServiceDetailRepository;
		
		@Autowired
		ComponentAndAttributeService componentAndAttributeService;
		
		@Autowired
		TaskService taskService;
		
		@Autowired
		WorkFlowService workFlowService;

	public void execute(DelegateExecution execution) {
		logger.info("AssignDummyIPServiceConfigurationDelegate invoked for {} Id={}", execution.getCurrentActivityId(),
				execution.getId());

		String errorMessage = "";
		String errorCode="";

		try {
			Task task = workFlowService.processServiceTask(execution);

			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Map<String, String> ipConfigRequestMapper = new HashMap<>();
			ipConfigRequestMapper.put("SERVICE_ID", serviceCode);			
			logger.info("AssignDummyIPServiceConfigurationDelegate Order Type {}",task.getOrderType());
			ipConfigRequestMapper.put("ACTION_TYPE", "PE_PROV_CONFIG");
			String productType=(String) processMap.get(PRODUCT_NAME);
			String requestId=taskService.getRandomNumberForNetp(serviceCode, execution.getProcessInstanceId(), "assign_dummy_ip_config_", productType==null?"":productType);
			ipConfigRequestMapper.put("REQUEST_ID",requestId);
			String request = Utils.convertObjectToJson(ipConfigRequestMapper);
			String ipConfigurationResponse = (String) mqUtils.sendAndReceive(assignDummyIpConfigurationQueue, request);
			logger.info("AssignDummyIPServiceConfigurationDelegate serviceCode={} assignDummyIpServiceConfigurationMessageSent={}", serviceCode,
					ipConfigurationResponse);
			if (StringUtils.isNotBlank(ipConfigurationResponse)) {
				Response response = Utils.convertJsonToObject(ipConfigurationResponse, Response.class);
				if (response!=null && response.getStatus()) {
					execution.setVariable("assignDummyIpServiceConfigurationMessageSent", response.getStatus());
				}else {
					if (response ==null) {
						execution.setVariable("assignDummyIpServiceConfigurationMessageSent", false);
						execution.setVariable("serviceConfigFailureReason", CramerConstants.SYSTEM_ERROR);
					}else {
						execution.setVariable("assignDummyIpServiceConfigurationMessageSent", response.getStatus());
						execution.setVariable("serviceConfigFailureReason", response.getErrorMessage());
					}
					errorMessage = response.getErrorMessage();
                    errorCode=response.getErrorCode();

				}
			}
		} catch (Exception e) {
			logger.error("AssignDummyIPServiceConfigurationDelegate Exception {} ", e);
			execution.setVariable("assignDummyIpServiceConfigurationMessageSent", false);
			execution.setVariable("serviceConfigFailureReason", CramerConstants.SYSTEM_ERROR);
			errorMessage = CramerConstants.SYSTEM_ERROR;
			errorCode="500";

		}
		String serviceCode = (String) execution.getVariable(SERVICE_CODE);

		ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");

		if (scServiceDetail != null && StringUtils.isNotBlank(errorMessage)) {
			try {
	            logger.info("AssignDummyIPServiceConfigurationDelegate error log started");
	            
				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "assign-dummy-service-configuration",
						componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode), AttributeConstants.ERROR_MESSAGE,"assign-dummy-ip-service-configuration");
			} catch (TclCommonException e) {
				logger.error(
						"AssignDummyIPServiceConfigurationDelegate------------------- getting error message details----------->{}",
						e);
			}
		}
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);

	}

}
