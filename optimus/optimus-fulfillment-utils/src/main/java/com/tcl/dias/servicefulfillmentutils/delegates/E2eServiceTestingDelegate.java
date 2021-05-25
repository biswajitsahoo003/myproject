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
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("e2eServiceTestingDelegate")
public class E2eServiceTestingDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(E2eServiceTestingDelegate.class);
	
		@Autowired
		MQUtils mqUtils;
	
		@Value("${activation.ip.configuration}")
		String ipConfigurationQueue;
		
		@Autowired
		TaskService taskService;
		
		@Autowired
		WorkFlowService workFlowService;

		@Value("${queue.rf.e2e.inventory}")
		String rfE2eInventoryQueue;

	  public void execute(DelegateExecution execution) {
		  logger.info("E2eServiceTestingDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
		  String errorMessage=null; 
		  try {
				Task task = workFlowService.processServiceTask(execution);

		    	
			    Map<String, Object> processMap = execution.getVariables();
			    String serviceCode= (String) processMap.get(SERVICE_CODE);
			    Map<String,String> ipConfigRequestMapper=new HashMap<>();
			    ipConfigRequestMapper.put("SERVICE_ID", serviceCode);
			    ipConfigRequestMapper.put("ACTION_TYPE", "POST_VALIDATION");
			    String productType=(String) processMap.get(PRODUCT_NAME);
			    String requestId=taskService.getRandomNumberForNetp(serviceCode, execution.getProcessInstanceId(), "post_validation_", productType==null?"":productType);
			    ipConfigRequestMapper.put("REQUEST_ID", requestId);
			    String request=Utils.convertObjectToJson(ipConfigRequestMapper);
			    String e2eServiceTestingMessageSent = (String) mqUtils.sendAndReceive(ipConfigurationQueue, request);
			    logger.info("E2eServiceTestingDelegate serviceCode={} e2eServiceTestingMessageSent={}",serviceCode,e2eServiceTestingMessageSent);
			    if(StringUtils.isNotBlank(e2eServiceTestingMessageSent)) {
					Response response = Utils.convertJsonToObject(e2eServiceTestingMessageSent, Response.class);
					if (response!=null && response.getStatus() != null && response.getStatus()) {
						execution.setVariable("e2eServiceTestingMessageSent", response.getStatus());
					}else {
						execution.setVariable("e2eServiceTestingMessageSent", false);
						if(response!=null && response.getErrorMessage()!=null) {
							execution.setVariable("e2eServiceTestingFailureReason",response.getErrorMessage());
						}else {
							execution.setVariable("e2eServiceTestingFailureReason",CramerConstants.SYSTEM_ERROR);
						}
					}
				}else {
					execution.setVariable("e2eServiceTestingMessageSent", false);
					execution.setVariable("e2eServiceTestingFailureReason",CramerConstants.SYSTEM_ERROR);					
				}
			    try {
			    	logger.info("E2eServiceTestingDelegate serviceCode={} rfE2eInventoryQueue={}", serviceCode, request);
			    	mqUtils.send(rfE2eInventoryQueue, request);
			    } catch (Exception e) {
			    	logger.error("E2eServiceTestingDelegate Exception {} ", e);
			    }			  
			    
			} catch (Exception e) {
				logger.error("E2eServiceTestingDelegate Exception {} ", e);
				execution.setVariable("e2eServiceTestingMessageSent", false);
				execution.setVariable("e2eServiceTestingFailureReason", CramerConstants.SYSTEM_ERROR);
				errorMessage=CramerConstants.SYSTEM_ERROR;
			}
		    
	        workFlowService.processServiceTaskCompletion(execution ,errorMessage);

	  
	  }

}
