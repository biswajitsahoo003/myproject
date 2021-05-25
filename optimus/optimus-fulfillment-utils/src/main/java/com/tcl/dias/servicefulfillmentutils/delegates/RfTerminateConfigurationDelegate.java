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
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("rfTerminateConfigurationDelegate")
public class RfTerminateConfigurationDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(RfTerminateConfigurationDelegate.class);

	@Autowired
	MQUtils mqUtils;

	@Value("${activation.rf.terminate.configuration}")
	String rfConfigurationQueue;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	TaskService taskService;
	
	@Autowired
	WorkFlowService workFlowService;

	public void execute(DelegateExecution execution) {
		String errorMessage = "";
		String errorCode="";

		logger.info("RfTerminateConfigurationDelegate invoked for {} Id={}", execution.getCurrentActivityId(),
				execution.getId());
		try {
			Task task = workFlowService.processServiceTask(execution);

			Map<String, Object> processMap = execution.getVariables();
			String parentServiceCode = (String) processMap.get("parentServiceCode");
			Map<String, String> ipConfigRequestMapper = new HashMap<>();
			ipConfigRequestMapper.put("SERVICE_ID", parentServiceCode);
			ipConfigRequestMapper.put("ACTION_TYPE", "TERMINATE");
			 String productType=(String) processMap.get(PRODUCT_NAME);
			 String requestId=taskService.getRandomNumberForNetp(parentServiceCode, execution.getProcessInstanceId(), "rf_terminate_config_", productType==null?"":productType);
			ipConfigRequestMapper.put("REQUEST_ID",requestId);
			String request = Utils.convertObjectToJson(ipConfigRequestMapper);
			String rfConfigurationResponse = (String) mqUtils.sendAndReceive(rfConfigurationQueue, request);
			logger.info("RfTerminateConfigurationDelegate serviceCode={} rfTerminateConfigurationMessageSent={}", parentServiceCode,
					rfConfigurationResponse);
			if (StringUtils.isNotBlank(rfConfigurationResponse)) {
				Response response = Utils.convertJsonToObject(rfConfigurationResponse, Response.class);
				if (response.getStatus() != null && response.getStatus())
					execution.setVariable("rfTerminateConfigurationMessageSent", rfConfigurationResponse);
				else {
					execution.setVariable("rfTerminateConfigurationMessageSent", false);
					if(response!=null && response.getErrorMessage()!=null) {
						execution.setVariable("rFCallFailureReason", response.getErrorMessage());
						errorMessage = response.getErrorMessage();
	                    errorCode = response.getErrorCode();
					}else {
						execution.setVariable("rFCallFailureReason", CramerConstants.SYSTEM_ERROR);
						errorMessage = CramerConstants.SYSTEM_ERROR;
						errorCode="500";
					}

				}
			}
		} catch (Exception e) {
			logger.error("RfTerminateConfigurationDelegate Exception {} ", e);
			execution.setVariable("rfTerminateConfigurationMessageSent", false);
			execution.setVariable("rFCallFailureReason", CramerConstants.SYSTEM_ERROR);
			errorMessage = CramerConstants.SYSTEM_ERROR;
			errorCode="500";

		}
		String serviceCode = (String) execution.getVariable(SERVICE_CODE);

		ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");

		if (scServiceDetail != null && StringUtils.isNotBlank(errorMessage)) {

			try {
	            logger.info("RfTerminateConfigurationDelegate error log started");

				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "rFCallFailureReason",
						componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode), AttributeConstants.ERROR_MESSAGE,"rf-terminate-configuration");
			} catch (TclCommonException e) {
				logger.error(
						"RfTerminateConfigurationDelegate------------------- getting error message details----------->{}",
						e);
			}
		}
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);

	}

}
