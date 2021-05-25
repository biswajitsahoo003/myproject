package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.PRODUCT_NAME;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;;


/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("serviceConfigurationTerminateDelegate")
public class ServiceConfigurationTerminateDelegate implements JavaDelegate {
		private static final Logger logger = LoggerFactory.getLogger(ServiceConfigurationTerminateDelegate.class);
	
		@Autowired
		MQUtils mqUtils;
	
		@Value("${activation.ip.terminate.configuration}")
		String ipTerminateConfigurationQueue;
		
		@Autowired
		ScServiceDetailRepository scServiceDetailRepository;
		
		@Autowired
		ComponentAndAttributeService componentAndAttributeService;
		
		@Autowired
		TaskService taskService;
		
		@Autowired
		WorkFlowService workFlowService;

	public void execute(DelegateExecution execution) {
		logger.info("ServiceConfigurationTerminateDelegate invoked for {} Id={}", execution.getCurrentActivityId(),
				execution.getId());

		String errorMessage = "";
		String errorCode="";
		String parentServiceCode=null;
		Map<String, Object> processMap = execution.getVariables();

		try {
			Task task = workFlowService.processServiceTask(execution);

			if(processMap.get("terminationFlowTriggered")!=null && "Yes".equalsIgnoreCase((String)processMap.get("terminationFlowTriggered"))) {
				parentServiceCode=(String)processMap.get(MasterDefConstants.SERVICE_CODE);
				
				logger.info("ServiceConfigurationTerminateDelegate terminationFlowTriggered invoked for {} Id={} and service code:{}", execution.getCurrentActivityId(),
						execution.getId(),parentServiceCode);
			}
			else {
				parentServiceCode= (String) processMap.get("parentServiceCode");

			}
			Map<String, String> ipConfigRequestMapper = new HashMap<>();
			ipConfigRequestMapper.put("SERVICE_ID", parentServiceCode);			
			ipConfigRequestMapper.put("ACTION_TYPE", "TERMINATE");
			String productType=(String) processMap.get(PRODUCT_NAME);
			String requestId=taskService.getRandomNumberForNetp(parentServiceCode, execution.getProcessInstanceId(), "ip_terminate_config_", productType==null?"":productType);
			ipConfigRequestMapper.put("REQUEST_ID",requestId);
			String request = Utils.convertObjectToJson(ipConfigRequestMapper);
			String ipConfigurationResponse = (String) mqUtils.sendAndReceive(ipTerminateConfigurationQueue, request);
			logger.info("ServiceConfigurationTerminateDelegate parentServiceCode={} serviceConfigurationMessageSent={}", parentServiceCode,
					ipConfigurationResponse);
			if (StringUtils.isNotBlank(ipConfigurationResponse)) {
				Response response = Utils.convertJsonToObject(ipConfigurationResponse, Response.class);
				if (response!=null && response.getStatus()) {
					execution.setVariable("serviceConfigurationTerminateMessageSent", response.getStatus());
					if(Objects.nonNull(response.getData()) && "true".equalsIgnoreCase(response.getData())){
						logger.info("RF Terminate exists");
						execution.setVariable("isRFExists", true);
					}else{
						logger.info("RF Terminate doesn't exists");
						execution.setVariable("isRFExists", false);
					}
				}else {
					if (response ==null) {
						execution.setVariable("serviceConfigurationTerminateMessageSent", false);
						execution.setVariable("serviceConfigFailureReason", CramerConstants.SYSTEM_ERROR);
					}else {
						execution.setVariable("serviceConfigurationTerminateMessageSent", response.getStatus());
						execution.setVariable("serviceConfigFailureReason", response.getErrorMessage());
					}
					errorMessage = response.getErrorMessage();
                    errorCode=response.getErrorCode();

				}
			}else {
				logger.info("ServiceConfigurationTerminateDelegate parentServiceCode={} no serviceConfigurationMessageSent={}", parentServiceCode,
						ipConfigurationResponse);
				execution.setVariable("serviceConfigurationTerminateMessageSent", false);
				execution.setVariable("serviceConfigFailureReason", CramerConstants.SYSTEM_ERROR);		
				errorMessage = CramerConstants.SYSTEM_ERROR;
				errorCode="500";
			}
		} catch (Exception e) {
			logger.error("ServiceConfigurationTerminateDelegate Exception {} ", e);
			execution.setVariable("serviceConfigurationTerminateMessageSent", false);
			execution.setVariable("serviceConfigFailureReason", CramerConstants.SYSTEM_ERROR);
			errorMessage = CramerConstants.SYSTEM_ERROR;
			errorCode="500";

		}
		String serviceCode = (String) execution.getVariable(SERVICE_CODE);
		ScServiceDetail scServiceDetail=null;

		
		if (processMap.get("terminationFlowTriggered") != null
				&& "Yes".equalsIgnoreCase((String) processMap.get("terminationFlowTriggered"))) {

			logger.info(
					"ServiceConfigurationTerminateDelegate terminationFlowTriggered invoked for primary key{} Id={} and service code:{}",
					execution.getCurrentActivityId(), execution.getId(), parentServiceCode);
			scServiceDetail = scServiceDetailRepository
					.findById((Integer) processMap.get(MasterDefConstants.SERVICE_ID)).get();

		}
		else {
			scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode, "INPROGRESS");

		}

		if (scServiceDetail != null && StringUtils.isNotBlank(errorMessage)) {
			try {
	            logger.info("ServiceConfigurationTerminateDelegate error log started for service id::{}",scServiceDetail.getId());

				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "ipTerminateConfigFailureReason",
						componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode), AttributeConstants.ERROR_MESSAGE,"ip-terminate-configuration");
			} catch (TclCommonException e) {
				logger.error(
						"ServiceConfigurationTerminateDelegate------------------- getting error message details----------->{}",
						e);
			}
		}
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);

	}

}
