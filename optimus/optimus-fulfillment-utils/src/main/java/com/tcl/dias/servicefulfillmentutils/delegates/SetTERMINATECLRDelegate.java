package com.tcl.dias.servicefulfillmentutils.delegates;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
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
@Component("setTERMINATECLRDelegate")
public class SetTERMINATECLRDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(SetTERMINATECLRDelegate.class);
    public static final String SET_TREMINATE_MFD_CLR_SUCCESS = "setTERMINATECLRSuccess";

    @Autowired
	MQUtils mqUtils;

	@Value("${queue.setterminateclrsync}")
	String setTERMINATECLRSyncQueue;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	TaskService taskService;

	@Autowired
    ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	

	public void execute(DelegateExecution execution) {
        logger.info("SetTERMINATECLRDelegate  invoked for {} ", execution.getCurrentActivityId());
        String errorMessage="";
		String errorCode="";
        String req = "";
        Task task = null;
        try {
            task = workFlowService.processServiceTask(execution);
    		Map<String, Object> varibleMap = execution.getVariables();
    		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
    		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
    		String serviceCode=null;

			if (varibleMap.get("terminationFlowTriggered") != null
					&& "Yes".equalsIgnoreCase((String) varibleMap.get("terminationFlowTriggered"))) {

				serviceCode = scServiceDetail.getUuid();

				logger.info("terminationFlowTriggered SetTerminateCLR: ParentUUID exists for servicecode:{}",
						serviceCode);

			} else {
				serviceCode = scServiceDetail.getParentUuid();
				logger.info(" SetTerminateCLR: ParentUUID exists for servicecode:{}", serviceCode);

			}
    		if(Objects.nonNull(scServiceDetail) && serviceCode!=null){
    			 logger.info("SetTerminateCLR: ParentUUID exists");
    			 SetCLRSyncBean setCLRSyncBean = getTERMINATESetCLRSyncBean(taskService.getRandomNumberForCrammer()+execution.getProcessInstanceId()+"#"+"TER"+"#"+serviceCode,
    					 serviceCode);
    	            setCLRSyncBean.setServiceId(serviceId);

    	            req = Utils.convertObjectToJson(setCLRSyncBean);

    	            String setCLRSyncResponse = (String) mqUtils.sendAndReceive(setTERMINATECLRSyncQueue, req);
    	            logger.info("setTERMINATECLRResponse => {} ", setCLRSyncResponse);

    	            if (StringUtils.isBlank(setCLRSyncResponse)) {
    	                execution.setVariable(SET_TREMINATE_MFD_CLR_SUCCESS, false);
    	                errorMessage = CramerConstants.SYSTEM_ERROR;
    	            }else {
    	                Response response = Utils.convertJsonToObject(setCLRSyncResponse, Response.class);
    	                if (response.getStatus()!=null && response.getStatus() ) {
    	                    execution.setVariable(SET_TREMINATE_MFD_CLR_SUCCESS, true);
    	                } else {
    	                    execution.setVariable(SET_TREMINATE_MFD_CLR_SUCCESS, false);
    	                    errorCode=response.getErrorCode();
    	                    if(response.getErrorMessage()!=null) {
    	                        errorMessage = response.getErrorMessage();
    	                    }else {
    	                        errorMessage = CramerConstants.SYSTEM_ERROR;
    	                    }
    	                }
    	            }
    		}else{
    			 logger.info("SetTerminateCLR: ParentUUID not exists");
    			 execution.setVariable(SET_TREMINATE_MFD_CLR_SUCCESS, false);
    		}
        } catch (Exception e) {
            logger.error("SetTERMINATECLRDelegate Exception {} ", e);
            execution.setVariable(SET_TREMINATE_MFD_CLR_SUCCESS, false);
            errorMessage = CramerConstants.SYSTEM_ERROR;
        }
        errorMessage = StringUtils.trimToEmpty(errorMessage);
        if (StringUtils.isNotBlank(errorMessage)) {
        	Optional<ScServiceDetail> scServiceDetail=	scServiceDetailRepository.findById(task.getServiceId());
			if (scServiceDetail.isPresent()&& StringUtils.isNotBlank(errorMessage)) {
				
				try {
					logger.info("SetTERMINATECLRDelegate error log started");


					componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
							"setCLRCallFailureReason",
							componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
							AttributeConstants.ERROR_MESSAGE, "set-terminate-clr");

				} catch (Exception e) {
					logger.error("checkTerminateCLRDelegate getting error message details----------->{}", e);
				}
			}
        }
        workFlowService.processServiceTaskCompletion(execution,errorMessage);
    }


    private SetCLRSyncBean getTERMINATESetCLRSyncBean(String processInstanceId, String serviceCode) {
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
