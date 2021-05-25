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
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
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
@Component("previewServiceConfigurationDelegate")
public class PreviewServiceConfigurationDelegate implements JavaDelegate {
		private static final Logger logger = LoggerFactory.getLogger(PreviewServiceConfigurationDelegate.class);
	
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
		logger.info("PreviewServiceConfigurationDelegate invoked for {} Id={}", execution.getCurrentActivityId(),
				execution.getId());
		String errorMessage = "";
		String errorCode="";
		try {
			Task task = workFlowService.processServiceTask(execution);

			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Map<String, String> ipConfigRequestMapper = new HashMap<>();
			ipConfigRequestMapper.put("SERVICE_ID", serviceCode);
			String orderSubCategory=null;
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
			if(Objects.nonNull(scServiceDetail) && Objects.nonNull(scServiceDetail.getScOrder()) 
					&& Objects.nonNull(scServiceDetail.getOrderSubCategory())){
				logger.info("OrderSubCateegory exists",scServiceDetail.getOrderSubCategory());
				orderSubCategory= OrderCategoryMapping.getOrderSubCategory(scServiceDetail.getOrderSubCategory());
			}
			
			if("MACD".equals(task.getOrderType()) && !"ADD_SITE".equals(task.getOrderCategory())
					&& (Objects.isNull(orderSubCategory) || (Objects.nonNull(orderSubCategory) && !orderSubCategory.toLowerCase().contains("parallel")))){
				logger.info("Preview Order Type {}",task.getOrderType());
				ipConfigRequestMapper.put("ACTION_TYPE", "MODIFY_SAVE");
			}else{
				logger.info("Preview Order Type {}",task.getOrderType());
				ipConfigRequestMapper.put("ACTION_TYPE", "PE_PROV_SAVE");
			}
			String productType = (String) processMap.get(PRODUCT_NAME);
			String requestId = taskService.getRandomNumberForNetp(serviceCode, execution.getProcessInstanceId(),
					"ip_preview_", productType == null ? "" : productType);
			ipConfigRequestMapper.put("REQUEST_ID", requestId);
			String request = Utils.convertObjectToJson(ipConfigRequestMapper);
			String ipConfigurationResponse = (String) mqUtils.sendAndReceive(ipConfigurationQueue, request);
			if (StringUtils.isNotBlank(ipConfigurationResponse)) {
				Response response = Utils.convertJsonToObject(ipConfigurationResponse, Response.class);
				logger.info("ServiceConfigurationDelegate serviceCode={} serviceConfigurationMessageSent={}",
						serviceCode, ipConfigurationResponse);
				if (response!=null && response.getStatus()) {
					execution.setVariable("previewIpConfigMessageSent", response.getStatus());
				} else {
					if (response ==null) {
						execution.setVariable("previewIpConfigMessageSent", false);
						execution.setVariable("previewConfigurationFailureReason", CramerConstants.SYSTEM_ERROR);
					}else {
						execution.setVariable("previewIpConfigMessageSent", response.getStatus());
						execution.setVariable("previewConfigurationFailureReason", response.getErrorMessage());
					}
					errorMessage = response.getErrorMessage();
                    errorCode=response.getErrorCode();

				}
			}

		} catch (Exception e) {
			logger.error("PreviewServiceConfigurationDelegate Exception {} ", e);
			execution.setVariable("previewIpConfigMessageSent", false);
			execution.setVariable("previewConfigurationFailureReason", CramerConstants.SYSTEM_ERROR);
		}
		String serviceCode = (String) execution.getVariable(SERVICE_CODE);

		try {
			if (StringUtils.isNotBlank(errorMessage)) {
	            logger.info("PreviewServiceConfigurationDelegate error log started");

				ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");

				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "ipconfigFailureReason",
						componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
						AttributeConstants.ERROR_MESSAGE, "preview-service-configuration");
			}
		} catch (TclCommonException e) {
			logger.error(
					"previewServiceConfigurationDelegate------------------- getting error message details----------->{}",
					e);
		}
		
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);

	}

}
