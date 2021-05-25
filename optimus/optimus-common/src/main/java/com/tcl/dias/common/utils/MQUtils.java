package com.tcl.dias.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.mq.beans.MqRequestResource;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * @author Manojkumar R
 *
 */
@Component
public class MQUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MQUtils.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private final long DEFAULT_TIMEOUT = 60000;
	
	@Value("${optimus.audit.save.queue}")
	String auditSaveQueue;

	/**
	 * This method is used for synchronous publish
	 * 
	 * @param queue
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public Object sendAndReceive(String queue, String request) throws TclCommonException {
		LOGGER.info("Request being send synchronously to the queue {}  is {}",queue,request);
		MqRequestResource<String> mqRequestResource = new MqRequestResource<>(request);
		rabbitTemplate.setReplyTimeout(DEFAULT_TIMEOUT);
		Object response =rabbitTemplate.convertSendAndReceive(queue, Utils.convertObjectToJson(mqRequestResource));
		LOGGER.info("response being received synchronously to the queue {}  is {}",queue,response);
		return response;
	}

	/**
	 * This method is used for synchronous publish with timeout
	 * 
	 * @param queue
	 * @param request
	 * @param timeout
	 * @return
	 * @throws TclCommonException
	 */
	public Object sendAndReceive(String queue, String request, long timeout) throws TclCommonException {
		LOGGER.info("Request being send synchronously to the queue {} with timeout {} is {}",queue,timeout,request);
		MqRequestResource<String> mqRequestResource = new MqRequestResource<>(request);
		rabbitTemplate.setReplyTimeout(timeout);
		Object response=rabbitTemplate.convertSendAndReceive(queue, Utils.convertObjectToJson(mqRequestResource));
		LOGGER.info("Response being received synchronously to the queue {} with timeout {} is {}",queue,timeout,response);
		return response;
	}

	/**
	 * This is used for asynchronous publish
	 * 
	 * @param queue
	 * @param request
	 * @throws TclCommonException
	 */
	public void send(String queue, String request) throws TclCommonException {
		if (!queue.equals(auditSaveQueue))//Ignoring the logs for audit , as it is not needed
			LOGGER.info("Request being send asynchronously to the queue {}  is {}", queue, request);
		MqRequestResource<String> mqRequestResource = new MqRequestResource<>(request);
		rabbitTemplate.convertAndSend(queue, Utils.convertObjectToJson(mqRequestResource));
	}

}
