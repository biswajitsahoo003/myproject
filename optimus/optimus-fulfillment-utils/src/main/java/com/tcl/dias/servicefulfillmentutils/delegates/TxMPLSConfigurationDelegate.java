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
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("txMPLSConfigurationDelegate")
public class TxMPLSConfigurationDelegate implements JavaDelegate {
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

		logger.info("TxMPLSConfigurationDelegate invoked for {} Id={}", execution.getCurrentActivityId(),
				execution.getId());
		String errorMessage = "";
		String errorCode="";

		try {
			Task task = workFlowService.processServiceTask(execution);

			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Map<String, String> ipConfigRequestMapper = new HashMap<>();
			ipConfigRequestMapper.put("SERVICE_ID", serviceCode);
			ipConfigRequestMapper.put("ACTION_TYPE", "CONFIG");
			 String productType=(String) processMap.get(PRODUCT_NAME);
			 String requestId=taskService.getRandomNumberForNetpTx(serviceCode, execution.getProcessInstanceId(), "txm_", productType==null?"":productType);
			ipConfigRequestMapper.put("REQUEST_ID",requestId);
			ipConfigRequestMapper.put("TX_TYPE", "mpls");
			String request = Utils.convertObjectToJson(ipConfigRequestMapper);
			String txConfigurationResponse = (String) mqUtils.sendAndReceive(txConfigurationQueue, request);
			logger.info("TxMPLSConfigurationDelegate serviceCode={} txMPLSConfigurationAckSuccess={}", serviceCode,
					txConfigurationResponse);
			if (StringUtils.isNotBlank(txConfigurationResponse)) {
				Response response = Utils.convertJsonToObject(txConfigurationResponse, Response.class);
				if (response !=null && response.getStatus()) {
					execution.setVariable("txMPLSConfigurationAckSuccess", true);
				}else {
					execution.setVariable("txMPLSConfigurationAckSuccess", false);
					execution.setVariable("txMplsConfigFailureReason ", response.getErrorMessage());
					errorMessage = response.getErrorMessage();
					errorCode=response.getErrorCode();
				}
			}else {
				execution.setVariable("txMPLSConfigurationAckSuccess", false);
				execution.setVariable("txMplsConfigFailureReason ",CramerConstants.SYSTEM_ERROR);
				errorMessage = CramerConstants.SYSTEM_ERROR;
				errorCode="500";
			}
		} catch (Exception e) {
			logger.error("TxMPLSConfigurationDelegate Exception {} ", e);
			execution.setVariable("txMPLSConfigurationAckSuccess", false);
			execution.setVariable("txMplsConfigFailureReason ", CramerConstants.SYSTEM_ERROR);
			errorMessage = CramerConstants.SYSTEM_ERROR;
			errorCode="500";
		}

		String serviceCode = (String) execution.getVariable(SERVICE_CODE);

		ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
		try {
			if (StringUtils.isNotBlank(errorMessage)) {
				logger.info("TxMPLSConfigurationDelegate error log started");
				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "txMplsConfigFailureReason",
						componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
						AttributeConstants.ERROR_MESSAGE, "tx-configuration-mpls");
			}
		} catch (TclCommonException e) {
			logger.error("txMPLSConfigurationDelegate------------------- getting error message details----------->{}",
					e);
		}
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);

	}

}
