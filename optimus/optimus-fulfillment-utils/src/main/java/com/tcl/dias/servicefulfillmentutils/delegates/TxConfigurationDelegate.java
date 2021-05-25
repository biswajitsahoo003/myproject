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
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("txConfigurationDelegate")
public class TxConfigurationDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(TxConfigurationDelegate.class);

	@Autowired
	MQUtils mqUtils;

	@Value("${activation.tx.configuration}")
	String txConfigurationQueue;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	TaskService taskService;
	
	@Autowired
	WorkFlowService workFlowService;

	public void execute(DelegateExecution execution) {
		logger.info("TxConfigurationDelegate invoked for {} Id={}", execution.getCurrentActivityId(),
				execution.getId());
		String errorMessage = "";
		String errorCode="";

		try {
			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Task task = workFlowService.processServiceTask(execution);

			Map<String, String> ipConfigRequestMapper = new HashMap<>();
			ipConfigRequestMapper.put("SERVICE_ID", serviceCode);
			ipConfigRequestMapper.put("ACTION_TYPE", "CONFIG");
			
			 String productType=(String) processMap.get(PRODUCT_NAME);
			 String requestId=taskService.getRandomNumberForNetpTx(serviceCode, execution.getProcessInstanceId(), "txs_", productType==null?"":productType);

			ipConfigRequestMapper.put("REQUEST_ID",requestId);
			ipConfigRequestMapper.put("TX_TYPE", "tx");

			String request = Utils.convertObjectToJson(ipConfigRequestMapper);
			String txConfigurationResponse = (String) mqUtils.sendAndReceive(txConfigurationQueue, request);
			logger.info("TxConfigurationDelegate serviceCode={} txSDHConfigurationAckSuccess={}", serviceCode,
					txConfigurationResponse);
			if (StringUtils.isNotBlank(txConfigurationResponse)) {
				Response response = Utils.convertJsonToObject(txConfigurationResponse, Response.class);
				if (response!=null && response.getStatus()) {
					execution.setVariable("txSDHConfigurationAckSuccess", true);
				}else {
					execution.setVariable("txSDHConfigurationAckSuccess", false);
					execution.setVariable("txSdhConfigFailureReason", response.getErrorMessage());
					errorMessage = response.getErrorMessage();
				}
			}else {
				execution.setVariable("txSDHConfigurationAckSuccess", false);
				execution.setVariable("txSdhConfigFailureReason", CramerConstants.SYSTEM_ERROR);
				errorMessage = CramerConstants.SYSTEM_ERROR;
			}
		} catch (Exception e) {
			logger.error("TxConfigurationDelegate Exception {} ", e);
			execution.setVariable("txSdhConfigFailureReason", CramerConstants.SYSTEM_ERROR);
			execution.setVariable("txSDHConfigurationAckSuccess", false);
			errorMessage = CramerConstants.SYSTEM_ERROR;

		}
		String serviceCode = (String) execution.getVariable(SERVICE_CODE);

		ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");

		if (scServiceDetail != null && StringUtils.isNotBlank(errorMessage)) {
			try {
	            logger.info("TxConfigurationDelegate error log started");

				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "txSdhConfigFailureReason",
						componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode), AttributeConstants.ERROR_MESSAGE,"tx-configuration-sdh");
			} catch (TclCommonException e) {
				logger.error(
						"txConfigurationDelegate------------------- getting error message details----------->{}",
						e);
			}
		}
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);


	}

}
