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
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@Component("txMPLSPostValidationDelegate")
public class TxMPLSPostValidationDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(TxMPLSConfigurationDelegate.class);

	@Autowired
	MQUtils mqUtils;

	@Value("${activation.tx.configuration}")
	String txConfigurationQueue;

	@Autowired
	TaskService taskService;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	WorkFlowService workFlowService;

	public void execute(DelegateExecution execution) {

		logger.info("txMPLSPostValidationDelegate invoked for {} Id={}", execution.getCurrentActivityId(),
				execution.getId());
		String errorMessage = "";
		String errorCode="";

		try {
			Task task = workFlowService.processServiceTask(execution);

			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Map<String, String> ipConfigRequestMapper = new HashMap<>();
			ipConfigRequestMapper.put("SERVICE_ID", serviceCode);
			ipConfigRequestMapper.put("ACTION_TYPE", "POST_VALIDATION");
			 String productType=(String) processMap.get(PRODUCT_NAME);
			 String requestId=taskService.getRandomNumberForNetp(serviceCode, execution.getProcessInstanceId(), "txmp_", productType==null?"":productType);
			ipConfigRequestMapper.put("REQUEST_ID",requestId);
			ipConfigRequestMapper.put("TX_TYPE", "mpls");
			String request = Utils.convertObjectToJson(ipConfigRequestMapper);
			String txConfigurationResponse = (String) mqUtils.sendAndReceive(txConfigurationQueue, request);
			logger.info("txMPLSPostValidationDelegate serviceCode={} txMPLSPostValidationAckSuccess={}", serviceCode,
					txConfigurationResponse);
			if (StringUtils.isNotBlank(txConfigurationResponse)) {
				Response response = Utils.convertJsonToObject(txConfigurationResponse, Response.class);
				if (response.getStatus() != null && response.getStatus())
					execution.setVariable("txMPLSPostValidationAckSuccess", txConfigurationResponse);
				else {
					execution.setVariable("txMPLSPostValidationAckSuccess", false);
					
					if(response!=null && response.getErrorMessage()!=null) {
						execution.setVariable("txMplsPostValidationFailureReason ", response.getErrorMessage());
						errorMessage = response.getErrorMessage();
						errorCode=response.getErrorCode();
					}else {
						execution.setVariable("txMplsPostValidationFailureReason", CramerConstants.SYSTEM_ERROR);
						errorMessage = CramerConstants.SYSTEM_ERROR;
					}

				}
			}
		} catch (Exception e) {
			logger.error("txMPLSPostValidationAckSuccess Exception {} ", e);
			execution.setVariable("txMPLSPostValidationAckSuccess", false);
			execution.setVariable("txMplsPostValidationFailureReason", CramerConstants.SYSTEM_ERROR);
			errorMessage = CramerConstants.SYSTEM_ERROR;
			errorCode = "500";
		}

		String serviceCode = (String) execution.getVariable(SERVICE_CODE);

		ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
		try {

			if (StringUtils.isNotBlank(errorMessage)) {
				logger.info("txMPLSPostValidationDelegate error log started");
				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,
						"txMplsPostValidationFailureReason",
						componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
						AttributeConstants.ERROR_MESSAGE, "tx-post-validation-mpls");
			}
		} catch (TclCommonException e) {
			logger.error("txMplsPostValidationFailureReason------------------- getting error message details----------->{}",
					e);
		}
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);

	}

}
