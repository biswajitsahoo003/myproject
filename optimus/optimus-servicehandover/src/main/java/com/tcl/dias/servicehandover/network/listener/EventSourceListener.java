package com.tcl.dias.servicehandover.network.listener;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.servicehandover.repository.GnvOrderEntryTabRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * EventSourceListener for Usage Event Source
 * @author yomagesh
 *
 */
@Service
public class EventSourceListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventSourceListener.class);
	
	@Autowired
	GnvOrderEntryTabRepository gnvOrderEntryTabRepository;
	
	/**
	 * Listener for Usage Event Source
	 * @param serviceCode
	 * @return
	 */

	@RabbitListener(queuesToDeclare = { @Queue("${queue.usage.eventsource}") })
	public String getEventSource(String serviceCode) throws TclCommonException {
		LOGGER.info("inside getEventSource started..");
		String eventSource = "";
		try {
			if (serviceCode != null) {
				eventSource = gnvOrderEntryTabRepository.getEventSource(serviceCode);
				if(StringUtils.isEmpty(eventSource)) {
					eventSource="Not Available";
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error in getEventSource", e);
		}
		LOGGER.info("event source for serviceCode {} eventSource{}", serviceCode, eventSource);
		LOGGER.info("inside getEventSource completed..");
		return eventSource;
	}
}
;