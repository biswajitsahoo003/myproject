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
import com.tcl.dias.servicefulfillmentutils.beans.MuxInfoAsyncBeans;
/**
 * MuxInfoAsyncListener - Listener class to consumer rabbitmq mux info asynchronous response 
 * @author Naveen G
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class MuxInfoAsyncListener {
	private static final Logger logger = LoggerFactory.getLogger(MuxInfoAsyncListener.class);

	@Autowired
	RuntimeService runtimeService;

	@RabbitListener(queuesToDeclare = { @Queue("${queue.muxinfoasync}") })
	public String muxInfoAsyncListener(Message<String> responseBody) {
		logger.info("muxInfoAsyncListener invoked {} ", responseBody.getPayload());
		String response = "false";
		String processInstanceId = "";
		try {
			MuxInfoAsyncBeans muxInfoAsyncBean = (MuxInfoAsyncBeans) Utils
					.convertJsonToObject(responseBody.getPayload(), MuxInfoAsyncBeans.class);
			logger.info("MuxInfoAsyncBeans {} ", muxInfoAsyncBean);
				processInstanceId = muxInfoAsyncBean.getRequestId();
				if (Objects.nonNull(processInstanceId)) {
                	String[] instanceId=processInstanceId.split("#");

					Execution execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
							.activityId("get-mux-info-async").singleResult();
					if (Objects.nonNull(muxInfoAsyncBean.getAendMuxDetails())
							&& !muxInfoAsyncBean.getAendMuxDetails().isEmpty()) {
						if (Objects.nonNull(muxInfoAsyncBean.getAendMuxDetails().get(0)) && muxInfoAsyncBean.getAendMuxDetails().get(0).getMuxprovisionStatus()
								.equalsIgnoreCase("Ready - In-Service")) {
							logger.info("mux provision status is ready");
							runtimeService.setVariable(execution.getId(), "isMuxInfoAvailable", true);
						}else {
							logger.info("mux provision status is not ready {}",muxInfoAsyncBean.getAendMuxDetails());
							runtimeService.setVariable(execution.getId(), "isMuxInfoAvailable", false);
						}
						runtimeService.trigger(execution.getId());
					}
					logger.info("muxInfoAsyncListener triggered the process successfully  {} ", execution.getId());
					response = "true";
				} else {
					logger.info("processId is empty! " + responseBody.getPayload());
				}
			logger.info("muxInfoAsyncListener response to network inventory service {} ", response);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in Mux info async listener {}",e.getMessage());
        	String[] instanceId=processInstanceId.split("#");

			Execution execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
					.activityId("get-mux-info-async").singleResult();
			runtimeService.setVariable(execution.getId(), "isMuxInfoAvailable", false);
			runtimeService.trigger(execution.getId());
			return response;
		}
	}
}
