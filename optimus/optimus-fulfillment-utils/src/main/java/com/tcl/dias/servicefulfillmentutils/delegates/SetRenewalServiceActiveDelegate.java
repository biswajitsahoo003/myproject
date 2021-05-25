package com.tcl.dias.servicefulfillmentutils.delegates;

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
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.beans.SetCLRSyncBean;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.CriticalPathService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
@Component("setRenewalServiceActiveDelegate")
public class SetRenewalServiceActiveDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(SetRenewalServiceActiveDelegate.class);

    @Autowired
	MQUtils mqUtils;

	@Value("${queue.setrenewalservice.active}")
	String setRenewalServiceActiveQueue;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	TaskService taskService;

	@Autowired
    ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	CriticalPathService criticalPathService;

	public void execute(DelegateExecution execution) {
        logger.info("setRenewalServiceActiveDelegate  invoked for {} ", execution.getCurrentActivityId());
        String errorMessage="";
		String errorCode="";
        String req = "";
        Task task = null;
        try {
            task = workFlowService.processServiceTask(execution);
    		Map<String, Object> varibleMap = execution.getVariables();
    		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
    		String serviceCode = (String) varibleMap.get(MasterDefConstants.SERVICE_CODE);
    		String orderCode = (String) varibleMap.get(MasterDefConstants.ORDER_CODE);
            SetCLRSyncBean setCLRSyncBean =new SetCLRSyncBean();
            setCLRSyncBean.setServiceId(serviceId);
            setCLRSyncBean.setObjectName(serviceCode);
            setCLRSyncBean.setOrderCode(orderCode);
            req = Utils.convertObjectToJson(setCLRSyncBean);

            String setServiceActiveResponse = (String) mqUtils.sendAndReceive(setRenewalServiceActiveQueue, req);
            logger.info("setRenewalServiceActiveResponse => {} ", setServiceActiveResponse);

            if (StringUtils.isBlank(setServiceActiveResponse)) {
                errorMessage = CramerConstants.SYSTEM_ERROR;
            }else {
                Response response = Utils.convertJsonToObject(setServiceActiveResponse, Response.class);
                if (response.getStatus()!=null && response.getStatus() ) {
                	
                } else {
                    errorCode=response.getErrorCode();
                    if(response.getErrorMessage()!=null) {
                        errorMessage = response.getErrorMessage();
                    }else {
                        errorMessage = CramerConstants.SYSTEM_ERROR;
                    }
                }
            }
            criticalPathService.computeCriticalPath(serviceId);
        } catch (Exception e) {
            logger.error("setRenewalServiceActiveDelegate Exception {} ", e);
            errorMessage = CramerConstants.SYSTEM_ERROR;
        }
        workFlowService.processServiceTaskCompletion(execution,errorMessage);
    }

}
