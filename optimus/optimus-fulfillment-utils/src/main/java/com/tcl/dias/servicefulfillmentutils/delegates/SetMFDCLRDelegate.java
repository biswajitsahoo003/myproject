package com.tcl.dias.servicefulfillmentutils.delegates;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

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
import com.tcl.dias.servicefulfillmentutils.beans.CramerServiceHeader;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.beans.SetCLRSyncBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("setMFDCLRDelegate")
public class SetMFDCLRDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(SetMFDCLRDelegate.class);
    public static final String SET_MFD_CLR_SUCCESS = "setMFDCLRSuccess";

    @Autowired
	MQUtils mqUtils;

	@Value("${queue.setmfdclrsync}")
	String setMFDCLRSyncQueue;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	TaskService taskService;

	@Autowired
    ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	

	public void execute(DelegateExecution execution) {
        logger.info("setMFDCLRDelegate  invoked for {} ", execution.getCurrentActivityId());
        String errorMessage="";
		String errorCode="";
        String req = "";
        Task task = null;
        try {
            task = workFlowService.processServiceTask(execution);
    		Map<String, Object> varibleMap = execution.getVariables();
    		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
            SetCLRSyncBean setCLRSyncBean = getMFDSetCLRSyncBean(taskService.getRandomNumberForCrammer()+execution.getProcessInstanceId()+"#"+"MFD"+"#"+task.getServiceCode(),
                    task.getServiceCode());
            setCLRSyncBean.setServiceId(serviceId);

            req = Utils.convertObjectToJson(setCLRSyncBean);

            String setCLRSyncResponse = (String) mqUtils.sendAndReceive(setMFDCLRSyncQueue, req);
            logger.info("setMFDCLRResponse => {} ", setCLRSyncResponse);

            if (StringUtils.isBlank(setCLRSyncResponse)) {
                execution.setVariable(SET_MFD_CLR_SUCCESS, false);
                errorMessage = CramerConstants.SYSTEM_ERROR;
            }else {
                Response response = Utils.convertJsonToObject(setCLRSyncResponse, Response.class);
                if (response.getStatus()!=null && response.getStatus() ) {
                    execution.setVariable(SET_MFD_CLR_SUCCESS, true);
                } else {
                    execution.setVariable(SET_MFD_CLR_SUCCESS, false);
                    errorCode=response.getErrorCode();
                    if(response.getErrorMessage()!=null) {
                        errorMessage = response.getErrorMessage();
                    }else {
                        errorMessage = CramerConstants.SYSTEM_ERROR;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("SetCLRDelegate Exception {} ", e);
            execution.setVariable(SET_MFD_CLR_SUCCESS, false);
            errorMessage = CramerConstants.SYSTEM_ERROR;
        }
        errorMessage = StringUtils.trimToEmpty(errorMessage);
        if (StringUtils.isNotBlank(errorMessage)) {
        	Optional<ScServiceDetail> scServiceDetail=	scServiceDetailRepository.findById(task.getServiceId());
			if (scServiceDetail.isPresent()&& StringUtils.isNotBlank(errorMessage)) {
				
				try {
					logger.info("SetCLRDelegate error log started");


					componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
							"setMFDCLRCallFailureReason",
							componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
							AttributeConstants.ERROR_MESSAGE, "set-mfd-clr");

				} catch (Exception e) {
					logger.error("checkCLRDelegate getting error message details----------->{}", e);
				}
			}
        }
        workFlowService.processServiceTaskCompletion(execution,errorMessage);
    }


    private SetCLRSyncBean getMFDSetCLRSyncBean(String processInstanceId, String serviceCode) {
        SetCLRSyncBean setCLRSyncBean = new SetCLRSyncBean();
        CramerServiceHeader cramerServiceHeader = new CramerServiceHeader();
        cramerServiceHeader.setAuthUser("");
        cramerServiceHeader.setClientSystemIP("");
        cramerServiceHeader.setOriginatingSystem(CramerConstants.OPTIMUS);
        cramerServiceHeader.setOriginationTime(String.valueOf(new Date()));
        cramerServiceHeader.setRequestID(processInstanceId);
        setCLRSyncBean.setCramerServiceHeader(cramerServiceHeader);
        setCLRSyncBean.setObjectName(serviceCode);
        setCLRSyncBean.setObjectType("SERVICE");
        setCLRSyncBean.setInitialRelationship("ACTIVE");
        setCLRSyncBean.setFinalRelationship("MARKEDFORDELETE");
        return setCLRSyncBean;
    }
}
