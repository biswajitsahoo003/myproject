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

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("cancelServiceConfigurationDelegate")
public class CancelServiceConfigurationDelegate implements JavaDelegate {
		private static final Logger logger = LoggerFactory.getLogger(CancelServiceConfigurationDelegate.class);
	
		@Autowired
		MQUtils mqUtils;
	
		@Value("${activation.ip.configuration}")
		String ipConfigurationQueue;
		
		@Autowired
		TaskService taskService;
		
		@Autowired
		ScServiceDetailRepository scServiceDetailRepository;
		
		@Autowired
		ComponentAndAttributeService componentAndAttributeService;
		
		@Autowired
		WorkFlowService workFlowService;

		public void execute(DelegateExecution execution) {
			String errorMessage="";
			String errorCode="";
		    Map<String, Object> processMap = execution.getVariables();

			logger.info("CancelServiceConfigurationDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
		    try {
				Task task = workFlowService.processServiceTask(execution);

			    String serviceCode= (String) processMap.get(SERVICE_CODE);
			    Map<String,String> ipConfigRequestMapper=new HashMap<>();
			    ipConfigRequestMapper.put("SERVICE_ID", serviceCode);
			    ipConfigRequestMapper.put("ACTION_TYPE", "CANCEL");
			    
			    String productType=(String) processMap.get(PRODUCT_NAME);
			    String requestId=taskService.getRandomNumberForNetp(serviceCode, execution.getProcessInstanceId(), "ip_cancel_", productType==null?"":productType);
			    ipConfigRequestMapper.put("REQUEST_ID",requestId);
			    String request=Utils.convertObjectToJson(ipConfigRequestMapper);
			    String ipConfigurationResponse = (String) mqUtils.sendAndReceive(ipConfigurationQueue, request);
			    logger.info("ServiceConfigurationDelegate serviceCode={} serviceConfigurationMessageSent={}",serviceCode,ipConfigurationResponse);
			    if(StringUtils.isNotBlank(ipConfigurationResponse)) {
					Response response = Utils.convertJsonToObject(ipConfigurationResponse, Response.class);
					if (response !=null && response.getStatus())
						execution.setVariable("cancelIpConfigMessageSent", response.getStatus());
					else {
						execution.setVariable("cancelIpConfigMessageSent", false);
						execution.setVariable("ipConfigurationFailureReason", response.getErrorMessage());
						errorMessage=response.getErrorMessage();
						errorCode=response.getErrorCode();
					}
				}else {
					execution.setVariable("cancelIpConfigMessageSent", false);
					execution.setVariable("ipConfigurationFailureReason", CramerConstants.SYSTEM_ERROR);
					errorMessage=CramerConstants.SYSTEM_ERROR;
				}
			} catch (Exception e) {
				logger.error("CancelServiceConfigurationDelegate Exception {} ", e);
				execution.setVariable("cancelIpConfigMessageSent", false);
				execution.setVariable("ipConfigurationFailureReason", CramerConstants.SYSTEM_ERROR);
			}
		    String serviceCode= (String) processMap.get(SERVICE_CODE);

			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");

		if (scServiceDetail != null && StringUtils.isNotBlank(errorMessage)) {
			try {
				logger.info("CancelServiceConfigurationDelegate error log started");

				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "ipconfigFailureReason",
						componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
						AttributeConstants.ERROR_MESSAGE,"cancel-service-configuration");
			} catch (Exception e) {
				logger.error("cancelServiceConfigurationDelegate getting error message details----------->{}", e);
			}
		}
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);

	  }

}
