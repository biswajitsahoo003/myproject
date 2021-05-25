package com.tcl.dias.serviceactivation.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * Feasibility listener class for O2C workflow.
 */
@Component
public class FeasibilityListener {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FeasibilityListener.class);

    /**
     * This method receives the feasibility input request.
     *
     * @param requestBody
     * @return feasibilityResponse
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.o2c.feasibility}")})
    public String triggerFeasibility(final Message<String> requestBody) {
        LOGGER.info("triggerFeasibility invoked");
        return "";
    }
}
