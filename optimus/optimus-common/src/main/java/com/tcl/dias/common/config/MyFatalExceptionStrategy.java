package com.tcl.dias.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;

public class MyFatalExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyFatalExceptionStrategy.class);

	@Override
	public boolean isFatal(Throwable t) {
		if (t instanceof ListenerExecutionFailedException) {
			ListenerExecutionFailedException lefe = (ListenerExecutionFailedException) t;
			LOGGER.error("Failed to process inbound message from queue "
					+ lefe.getFailedMessage().getMessageProperties().getConsumerQueue() + "; failed message: "
					+ lefe.getFailedMessage(), t);
		}
		return true;
	}

}
