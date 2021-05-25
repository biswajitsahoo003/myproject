package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;

import java.util.Date;
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
import com.tcl.dias.servicefulfillment.entity.repository.ActivityRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstActivityDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.CramerInfoBean;
import com.tcl.dias.servicefulfillmentutils.beans.CramerServiceHeader;
import com.tcl.dias.servicefulfillmentutils.beans.IPServiceSyncBean;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskDataService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("assignDummyWANIPDelegate")
public class AssignDummyWANIPDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(AssignDummyWANIPDelegate.class);

	@Autowired
	MQUtils mqUtils;
	@Value("${queue.assigndummywanipsync}")
	String assignDummyWANIPSyncQueue;

	@Autowired
	MstTaskDefRepository mstTaskDefRepository;

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	TaskCacheService taskCacheService;

	@Autowired
	MstActivityDefRepository mstActivityDefRepository;

	@Autowired
	TaskDataService taskDataService;

	@Autowired
	TaskService taskService;

	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	public void execute(DelegateExecution execution) {
		logger.info("assignDummyWANIPDelegate invoked for {} ", execution.getCurrentActivityId());
		String errorMessage = "";
		String errorCode="";

		Task task = null;
		try {
			task = workFlowService.processServiceTask(execution);
			Map<String, Object> processMap = execution.getVariables();

            IPServiceSyncBean ipServiceSyncBean = getAssignDummyIPServiceBean(taskService.getRandomNumberForCrammer()+execution.getProcessInstanceId(), processMap);

            String req = Utils.convertObjectToJson(ipServiceSyncBean);
            logger.info("assignDummyWANIPRequest {} ", req);

			String assignDummyWANIPSyncResponse = (String) mqUtils.sendAndReceive(assignDummyWANIPSyncQueue, req,90000);
			logger.info("assignDummyWANIPSyncResponse= {} ", assignDummyWANIPSyncResponse);
			if (StringUtils.isBlank(assignDummyWANIPSyncResponse)) {
				execution.setVariable("assignDummyWANIPDummySyncCallSuccess", false);
                errorMessage = CramerConstants.SYSTEM_ERROR;
            } else {
                Response response = Utils.convertJsonToObject(assignDummyWANIPSyncResponse, Response.class);
                if(response.getStatus()!=null && response.getStatus()) {                  
                    execution.setVariable("assignDummyWANIPDummySyncCallSuccess", true);
                }else{
                    execution.setVariable("assignDummyWANIPDummySyncCallSuccess", false);
                    errorMessage = response.getErrorMessage();
                    errorCode=response.getErrorCode();
                }
			}
		} catch (Exception e) {
            logger.error("assignDummyWANIPDelegate Exception", e);
            execution.setVariable("assignDummyWANIPDummySyncCallSuccess", false);
            execution.setVariable("serviceDesignCallFailureReason", CramerConstants.SYSTEM_ERROR);
            errorMessage = CramerConstants.SYSTEM_ERROR;
        }
		String message = StringUtils.trimToEmpty(errorMessage);
		if (StringUtils.isNotBlank(message)) {			
				try {
					logger.info("assignDummyWANIPDelegate error log started");

					componentAndAttributeService.updateAdditionalAttributes(task.getServiceId(),
							"serviceDesignAssignIpInfoCallFailureReason",
							componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
							AttributeConstants.ERROR_MESSAGE, "assign-dummy-wan-ip");

				} catch (Exception e) {
					logger.error("assignDummyWANIPDelegate getting error message details----------->{}", e);
				}		

		}
				workFlowService.processServiceTaskCompletion(execution,errorMessage);
    }
	
	/**
	 * private method to construct dummyipservice info bean for dummyipservice sync cramer
	 * call.
	 * 
	 * @param processInstanceId
	 * @param processMap
	 * @return IPServiceSyncBean
	 */
	private IPServiceSyncBean getAssignDummyIPServiceBean(String processInstanceId, Map<String, Object> processMap) {
		String serviceCode = (String) processMap.get(SERVICE_CODE);
		IPServiceSyncBean ipServiceSyncBean = new IPServiceSyncBean();
		CramerServiceHeader cramerServiceHeader = new CramerServiceHeader();
		cramerServiceHeader.setAuthUser("");
		cramerServiceHeader.setClientSystemIP("");
		cramerServiceHeader.setOriginatingSystem(CramerConstants.OPTIMUS);
		cramerServiceHeader.setOriginationTime(String.valueOf(new Date()));
		cramerServiceHeader.setRequestID(processInstanceId+"#"+serviceCode);
		ipServiceSyncBean.setCramerServiceHeader(cramerServiceHeader);
		ipServiceSyncBean.setServiceID(serviceCode);
		ipServiceSyncBean.setVpnID("VPNID_"+serviceCode);
		String productName = (String) processMap.get("productName");
		if (productName.equalsIgnoreCase(CramerConstants.IAS) ||productName.equalsIgnoreCase(CramerConstants.ILL)) {
			ipServiceSyncBean.setServiceType(CramerConstants.ILL);
		}else if(productName.equalsIgnoreCase("GVPN")) {
			ipServiceSyncBean.setServiceType(CramerConstants.GVPN);
		}	

		ipServiceSyncBean.setRelationshipType(CramerConstants.ISSUED);
		return ipServiceSyncBean;
	}

}
