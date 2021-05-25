package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;

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
import com.tcl.dias.servicefulfillmentutils.beans.GetCLRSyncBean;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("getCLRDelegate")
public class GetCLRDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(GetCLRDelegate.class);

	@Autowired
	MQUtils mqUtils;

	@Value("${queue.getclrsync}")
	String getCLRSyncQueue;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	TaskService taskService;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Value("${application.env:DEV}")
	String appEnv;

	public void execute(DelegateExecution execution) {
		logger.info("GetCLRDelegate invoked for {} ", execution.getCurrentActivityId());
		String errorMessage="";
		String errorCode="";

		Task task = null;
		try {
			task = workFlowService.processServiceTask(execution);

			Map<String, Object> processMap = execution.getVariables();

			
            GetCLRSyncBean getCLRSyncBean = getClrBean(taskService.getRandomNumberForCrammer()+execution.getProcessInstanceId(), processMap);
            String req = Utils.convertObjectToJson(getCLRSyncBean);
            logger.info("GetCLRDelegate {} ", req);

            String getCLRResponse = (String) mqUtils.sendAndReceive(getCLRSyncQueue, req,120000);
            logger.info("GetCLRDelegate Response {}", getCLRResponse);

            if (StringUtils.isBlank(getCLRResponse)) {
                execution.setVariable("getCLRSyncCallCompleted", false);
                execution.setVariable("serviceDesignCallFailureReason", CramerConstants.SYSTEM_ERROR);
                errorMessage = CramerConstants.SYSTEM_ERROR;
            } else {
                Response response = Utils.convertJsonToObject(getCLRResponse, Response.class);
                if (Boolean.valueOf(response.getStatus())) {
                    execution.setVariable("getCLRSyncCallCompleted", true);
                } else {
                    execution.setVariable("getCLRSyncCallCompleted", false);
                    execution.setVariable("serviceDesignCallFailureReason", response.getErrorMessage());
                    errorMessage = response.getErrorMessage();
                    errorCode=response.getErrorCode();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            execution.setVariable("getCLRSyncCallCompleted", true);
            execution.setVariable("serviceDesignCallFailureReason", CramerConstants.SYSTEM_ERROR);
            errorMessage = CramerConstants.SYSTEM_ERROR;
            errorCode="500";
        }
		errorMessage = StringUtils.trimToEmpty(errorMessage);
		if (StringUtils.isNotBlank(errorMessage)) {
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(task.getServiceId());
			if (scServiceDetail.isPresent()) {

				try {
					logger.info("GetCLRDelegate error log started");

					componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
							"serviceDesignTxCallFailureReason",
							componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
							AttributeConstants.ERROR_MESSAGE, "get-clr");
				} catch (TclCommonException e) {
					logger.error("createServiceDelegate------------------- getting error message details----------->{}",
							e);

				}
			}
		}
		if (appEnv != null && "DEV".equalsIgnoreCase(appEnv)) {
			execution.setVariable("getCLRSyncCallCompleted", true);
		}
        workFlowService.processServiceTaskCompletion(execution,errorMessage);
	}
	
	

	/**
	 * private method to construct get clr bean for getclr sync cramer call.
	 * 
	 * @param processInstanceId
	 * @param processMap
	 * @return
	 */
	private GetCLRSyncBean getClrBean(String processInstanceId, Map<String, Object> processMap) {
		GetCLRSyncBean getCLRSyncBean = new GetCLRSyncBean();
		CramerServiceHeader cramerServiceHeader = new CramerServiceHeader();
		cramerServiceHeader.setAuthUser("");
		cramerServiceHeader.setClientSystemIP("");
		cramerServiceHeader.setOriginatingSystem(CramerConstants.OPTIMUS);
		cramerServiceHeader.setOriginationTime(String.valueOf(new Date()));
		cramerServiceHeader.setRequestID(processInstanceId);
		getCLRSyncBean.setCramerServiceHeader(cramerServiceHeader);
		getCLRSyncBean.setObjectType(CramerConstants.SERVICE);
		String serviceCode = (String) processMap.get(SERVICE_CODE);
		getCLRSyncBean.setObjectName(serviceCode);
		String orderType = (String) processMap.get(MasterDefConstants.ORDER_TYPE);
		if (orderType != null && "Termination".equalsIgnoreCase(orderType)) {
			getCLRSyncBean.setRelationshipType(CramerConstants.ACTIVE);

		} else {
			getCLRSyncBean.setRelationshipType(CramerConstants.ISSUED);

		}
		return getCLRSyncBean;
	}

}
