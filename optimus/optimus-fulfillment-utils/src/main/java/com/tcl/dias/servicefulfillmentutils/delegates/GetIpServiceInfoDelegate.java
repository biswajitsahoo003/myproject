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
import com.tcl.dias.servicefulfillmentutils.beans.IPServiceSyncBean;
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
@Component("getIpServiceInfoDelegate")
public class GetIpServiceInfoDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(GetIpServiceInfoDelegate.class);

	@Autowired
	MQUtils mqUtils;

	@Value("${queue.ipservicesync}")
	String ipServiceSyncQueue;

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
		logger.info("GetIpServiceInfoDelegate invoked for {} ", execution.getCurrentActivityId());
		String errorMessage = "";
		String errorCode="";

		Task task = null;
		try {
			task = workFlowService.processServiceTask(execution);
			Map<String, Object> processMap = execution.getVariables();

            IPServiceSyncBean ipServiceSyncBean = getIPServiceBean(taskService.getRandomNumberForCrammer()+execution.getProcessInstanceId(), processMap);

            String req = Utils.convertObjectToJson(ipServiceSyncBean);
            logger.info("getIPServiceSyncRequest {} ", req);

            String ipServiceSyncResponse = (String) mqUtils.sendAndReceive(ipServiceSyncQueue, req,120000);
            logger.info("get ip service sync queue call response {}", ipServiceSyncResponse);
            
            if(processMap.get("getIpServiceRetryAttempt")==null) {
				logger.info("GetIpServiceInfoDelegate serviceId :{} getIpServiceRetryAttempt 0",processMap.get(SERVICE_CODE));
				execution.setVariable("getIpServiceRetryAttempt", 0);
			}else {
				Integer getIpServiceRetryAttempt = (Integer)processMap.get("getIpServiceRetryAttempt");
				getIpServiceRetryAttempt = getIpServiceRetryAttempt +1;
				execution.setVariable("getIpServiceRetryAttempt", getIpServiceRetryAttempt);
			}

            if (StringUtils.isBlank(ipServiceSyncResponse)) {
                execution.setVariable("getIpServiceSyncCallCompleted", false);
                execution.setVariable("serviceDesignCallFailureReason", CramerConstants.SYSTEM_ERROR);
                errorMessage = CramerConstants.SYSTEM_ERROR;
            } else {
                Response response = Utils.convertJsonToObject(ipServiceSyncResponse, Response.class);
                if (Boolean.valueOf(response.getStatus())) {
                    execution.setVariable("getIpServiceSyncCallCompleted", true);
                } else {
                    execution.setVariable("getIpServiceSyncCallCompleted", false);
                    execution.setVariable("serviceDesignCallFailureReason", response.getErrorMessage());
                    errorMessage = response.getErrorMessage();
                    errorCode=response.getErrorCode();

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            execution.setVariable("getIpServiceSyncCallCompleted", false);
            execution.setVariable("serviceDesignCallFailureReason", CramerConstants.SYSTEM_ERROR);
            errorMessage = CramerConstants.SYSTEM_ERROR;
        }
		 errorMessage = StringUtils.trimToEmpty(errorMessage);
		if (StringUtils.isNotBlank(errorMessage)) {

			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(task.getServiceId());
			if (scServiceDetail.isPresent()) {
				try {
					logger.info("GetIpServiceInfoDelegate started");

					componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
							"serviceDesignIpInfoCallFailureReason",
							componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
							AttributeConstants.ERROR_MESSAGE, "get-ip-service-info");
				} catch (TclCommonException e) {
					logger.error(
							"getIpServiceInfoDelegate------------------- getting error message details----------->{}",
							e);

				}
			}
		}
		if (appEnv != null && "DEV".equalsIgnoreCase(appEnv)) {
			execution.setVariable("getIpServiceSyncCallCompleted", true);
		}
		  workFlowService.processServiceTaskCompletion(execution,errorMessage);
    }

	/**
	 * private method to construct ipservice info bean for ipservice sync cramer
	 * call.
	 * 
	 * @param processInstanceId
	 * @param processMap
	 * @return IPServiceSyncBean
	 */
	private IPServiceSyncBean getIPServiceBean(String processInstanceId, Map<String, Object> processMap) {
		IPServiceSyncBean ipServiceSyncBean = new IPServiceSyncBean();
		CramerServiceHeader cramerServiceHeader = new CramerServiceHeader();
		cramerServiceHeader.setAuthUser("");
		cramerServiceHeader.setClientSystemIP("");
		cramerServiceHeader.setOriginatingSystem(CramerConstants.OPTIMUS);
		cramerServiceHeader.setOriginationTime(String.valueOf(new Date()));
		cramerServiceHeader.setRequestID(processInstanceId);
		ipServiceSyncBean.setCramerServiceHeader(cramerServiceHeader);
		String serviceCode = (String) processMap.get(SERVICE_CODE);
		ipServiceSyncBean.setServiceID(serviceCode);
		
		String productName = (String) processMap.get("productName");
		if (productName.equalsIgnoreCase(CramerConstants.IAS) ||productName.equalsIgnoreCase(CramerConstants.ILL)) {
			ipServiceSyncBean.setServiceType(CramerConstants.ILL);
		}else if(productName.equalsIgnoreCase("GVPN") || productName.equalsIgnoreCase("IZOPC")) {
			ipServiceSyncBean.setServiceType(CramerConstants.GVPN);
		}

		if (processMap.get(CramerConstants.ORDERTYPETERMINATION) != null && CramerConstants.ORDERTYPETERMINATION
				.equalsIgnoreCase((String) processMap.get(CramerConstants.ORDERTYPETERMINATION))) {
			ipServiceSyncBean.setRelationshipType(CramerConstants.ACTIVE);

		} else {
			ipServiceSyncBean.setRelationshipType(CramerConstants.ISSUED);

		}
		return ipServiceSyncBean;
	}
}
