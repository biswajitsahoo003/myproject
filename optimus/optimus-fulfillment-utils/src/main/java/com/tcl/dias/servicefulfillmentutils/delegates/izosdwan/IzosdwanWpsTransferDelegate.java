package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.sap.response.SapQuantityAvailableRequest;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.IzosdwanSapService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

@Component("izosdwanWpsTransferDelegate")
public class IzosdwanWpsTransferDelegate implements JavaDelegate  {
	private static final Logger logger = LoggerFactory.getLogger(IzosdwanWpsTransferDelegate.class);

	@Autowired
	MQUtils mqUtils;

	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	TaskService taskService;

	@Autowired
    ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	IzosdwanSapService sapService;

	public void execute(DelegateExecution execution) {

		String errorMessage = "";
		String errorCode = "";

		try {
			Task task = workFlowService.processServiceTask(execution);
			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			Integer componentId = (Integer)processMap.get("cpeOverlayComponentId");
			String cpeType = (String) processMap.get("cpeType");
			String vendorCode = (String) processMap.get("vendorCode");
			String typeOfExpenses = (String) processMap.get("typeOfExpenses");
			
			
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");

			String  request = componentAndAttributeService.getAdditionalAttributesByScComponentAttr(componentId,
					"SAPINVENTORYCHECKCPE",vendorCode);

			logger.info("wpsTransferDelegate invoked for {} serviceCode={}, serviceId={}, cpeType={}",
					execution.getCurrentActivityId(), serviceCode, serviceId, cpeType);
			boolean transferred = sapService.placeWbsTransfer(
					Utils.convertJsonToObject(request, SapQuantityAvailableRequest.class), serviceCode, cpeType,
					typeOfExpenses,scServiceDetail,execution.getProcessInstanceId(),"CPE",componentId,vendorCode);
			
			logger.info("wpsTransferDelegate transferred={}",transferred);
			
			if (!transferred) {
				execution.setVariable("cpeHardwarePRCompleted", false);
			}

			
		} catch (Exception e) {
			execution.setVariable("cpeHardwarePRCompleted", false);

			logger.error("wpsTransferDelegate Exception {} ", e);
		}
		workFlowService.processServiceTaskCompletion(execution, errorMessage);
	}
}
