package com.tcl.dias.servicefulfillmentutils.delegates;


import java.util.HashMap;
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
import com.tcl.dias.servicefulfillment.entity.repository.MstActivityDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.DownTimeBean;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskDataService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component("getTxDownTimeDelegate")
public class GetTxDownTimeDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(GetTxDownTimeDelegate.class);

    @Autowired
    MQUtils mqUtils;

    @Value("${queue.txdowntimesync}")
    String txDownTimeQueue;

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

    @Override
    public void execute(DelegateExecution execution) {
        logger.info("configureTxDownTimeDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
        String errorMessage="";
        String errorCode="";

        Map<String, Object> taskDataMap = new HashMap<>();
        Task task =null;

        try {
        	task= workFlowService.processServiceTask(execution);
            if (Objects.nonNull(task)) {
                taskDataMap = taskDataService.getTaskData(task);
                logger.info("taskDataMap {} ",taskDataMap);
            } else {
                logger.info("Task is null in get down time call delegate");
            }

            DownTimeBean downTimeBean = getDownTimeBean(taskService.getRandomNumberForCrammer()+execution.getProcessInstanceId(), taskDataMap);
            String req = Utils.convertObjectToJson(downTimeBean);
            logger.info("getTxDownTimeDelegate {} ", req);

            String downTimeResponse = (String) mqUtils.sendAndReceive(txDownTimeQueue, req);
            logger.info("txDownTimeResponse {}", downTimeResponse);


            if (StringUtils.isBlank(downTimeResponse)) {
            	logger.info("txDownTimeResponse is empty");
                execution.setVariable("isTxDownTimeCallSuccess", false);
                execution.setVariable("txDownTimeErrorMessage", CramerConstants.SYSTEM_ERROR);
                errorMessage = CramerConstants.SYSTEM_ERROR;
            } else {
            	logger.info("txDownTimeResponse not empty");
                Response response = Utils.convertJsonToObject(downTimeResponse, Response.class);
                if (Boolean.valueOf(response.getStatus())) {
                	logger.info("Success response");
                    execution.setVariable("isTxDownTimeCallSuccess", true);
                } else {
                	logger.info("Failure response");
                    execution.setVariable("isTxDownTimeCallSuccess", false);
                    execution.setVariable("txDownTimeErrorMessage", response.getErrorMessage());
                    errorMessage=response.getErrorMessage();
                    errorCode=response.getErrorCode();

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            execution.setVariable("isTxDownTimeCallSuccess", false);
            execution.setVariable("txDownTimeErrorMessage", CramerConstants.SYSTEM_ERROR);
            errorMessage = CramerConstants.SYSTEM_ERROR;
        }
        errorMessage = StringUtils.trimToEmpty(errorMessage);
        if (StringUtils.isNotBlank(errorMessage)) {

            Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(task.getServiceId());
            if (scServiceDetail.isPresent()) {
                try {
                    logger.info("GetTxDownTimeDelegate Error Message started");

                    componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
                            "txDownTimeAsyncErrorMessage",
                            componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
                            AttributeConstants.ERROR_MESSAGE, "get-tx-downtime");
                } catch (TclCommonException e) {
                    logger.error(
                            "GetTxDownTimeDelegate------------------- getting error message details----------->{}",
                            e);
                }
            }
        }



        workFlowService.processServiceTaskCompletion(execution,errorMessage);
    }

    /**
     * private method to construct down time bean for downtime info cramer call.
     *
     * @param processInstanceId
     * @param taskDataMap
     * @return
     */
    private DownTimeBean getDownTimeBean(String processInstanceId, Map<String, Object> taskDataMap) {
        DownTimeBean downTimeBean = new DownTimeBean();
        downTimeBean.setServiceId(StringUtils.trimToEmpty((String)(taskDataMap.get(CramerConstants.SERVICE_CODE))));
        downTimeBean.setRequestID(processInstanceId);
        downTimeBean.setRequestingSystem(CramerConstants.OPTIMUS);
        return downTimeBean;
    }
}


