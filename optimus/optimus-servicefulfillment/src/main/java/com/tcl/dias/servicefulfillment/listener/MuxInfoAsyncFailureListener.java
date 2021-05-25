package com.tcl.dias.servicefulfillment.listener;

import java.util.Objects;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.beans.getmuxinfoasync.SubmitMuxDetailFailureResponse;


/**
 * MuxInfoAsyncFailureListener - Listener class to consumer rabbitmq mux info asynchronous submit failure soap call response
 * @author Naveen G
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class MuxInfoAsyncFailureListener {
	private static final Logger logger = LoggerFactory.getLogger(MuxInfoAsyncFailureListener.class);

	@Autowired
	RuntimeService runtimeService;

	@RabbitListener(queuesToDeclare = { @Queue("${queue.muxinfoasync.submitfailure}") })
	public String muxInfoAsyncFailureListener(Message<String> responseBody) {
		logger.info("MuxInfoAsyncFailureListener invoked {} ", responseBody.getPayload());
		String response = "false";
		try {
			SubmitMuxDetailFailureResponse muxInfoAsyncFailureBean = Utils
					.convertJsonToObject(responseBody.getPayload(), SubmitMuxDetailFailureResponse.class);

			if (Objects.nonNull(muxInfoAsyncFailureBean.getRequestId())) {			
				String requestId = muxInfoAsyncFailureBean.getRequestId();
				if (Objects.nonNull(requestId)) {
					String processInstanceId = requestId.split("_")[0];
                	String[] instanceId=processInstanceId.split("#");
                	
					Execution execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
							.activityId("get_mux_info_async").singleResult();
					if(execution!=null) {
						runtimeService.setVariable(execution.getId(), "isMuxInfoAvailable", false);
						runtimeService.trigger(execution.getId());
						logger.info("MuxInfoAsyncFailureListener triggered the process successfully  {} ", execution.getId());
					}
					response = "true";
				} else {
					logger.info("processId is empty! Response {}" , responseBody.getPayload());
				}
			}
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return response;
		}
	}
}
