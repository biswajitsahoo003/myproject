package com.tcl.dias.servicefulfillment.listener;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.ordertocash.beans.ResponseWrapper;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.CreateClrAsyncBean;
import com.tcl.dias.servicefulfillment.beans.SubmitCLRCreationFailureResponseBean;

@Component
public class CreateClrAsyncListener {

    public static final Logger LOGGER = LoggerFactory.getLogger(CreateClrAsyncListener.class);
    public static final String CREATE_CLR_ASYNC_ACTIVITY_ID = "enrich-service-design-async";

    @Autowired
    RuntimeService runtimeService;

    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.createclrasync}")})
    public String processCreateClrAsyncRespone(Message<String> responseBody) {
        LOGGER.info("CreateClrAsyncListener invoked : Response received -> {}", responseBody.getPayload());
        String response = "false";
        String processInstanceId= StringUtils.EMPTY;
        try {
            ResponseWrapper<CreateClrAsyncBean, SubmitCLRCreationFailureResponseBean> responseWrapper = Utils.convertJsonToObject((String) responseBody.getPayload(), ResponseWrapper.class);
            if (Objects.equals(Boolean.TRUE, responseWrapper.getIsSucess())) {
                CreateClrAsyncBean createCLRAyncBean = responseWrapper.getSuccessRespose();
                processInstanceId = createCLRAyncBean.getRequestId();
                if (Objects.nonNull(processInstanceId)) {
                	String[] instanceId=processInstanceId.split("#");

                    Execution execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
                            .activityId(CREATE_CLR_ASYNC_ACTIVITY_ID).singleResult();
                    runtimeService.setVariable(execution.getId(), "serviceDesignCompleted", true);
                    runtimeService.trigger(execution.getId());
                    LOGGER.info("CreateClrAsyncListener success triggered the process successfully  {} ", execution.getId());
                    return "true";
                }
            } else if (Objects.equals(Boolean.FALSE, responseWrapper.getIsSucess())) {
                SubmitCLRCreationFailureResponseBean failureResponse = responseWrapper.getFailureResponse();
                processInstanceId = failureResponse.getRequestId().split("_")[0];
                if (Objects.nonNull(processInstanceId)) {
                	String[] instanceId=processInstanceId.split("#");

                    Execution execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
                            .activityId(CREATE_CLR_ASYNC_ACTIVITY_ID).singleResult();
                    runtimeService.setVariable(execution.getId(), "serviceDesignCompleted", false);
                    runtimeService.trigger(execution.getId());
                    LOGGER.info("CreateClrAsyncListener failure triggered the process successfully  {} ", execution.getId());
                    response = "true";
                }
            }
        } catch (Exception e) {
            LOGGER.info("Exception in CreateClrAsyncListener {}", e.getMessage());
        	String[] instanceId=processInstanceId.split("#");

            Execution execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
                    .activityId(CREATE_CLR_ASYNC_ACTIVITY_ID).singleResult();
            //runtimeService.setVariable(execution.getId(), "isMuxInfoAvailable", false);
            runtimeService.setVariable(execution.getId(), "serviceDesignCompleted", false);
            runtimeService.trigger(execution.getId());
            return response;
        }
        return response;
    }

}
