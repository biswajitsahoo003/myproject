package com.tcl.dias.sfdc.consumer;

import com.tcl.dias.sfdc.services.SfdcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  This file contains the WaiverListener.java class.
 *
 */
@Service
public class WaiverListener {

    @Autowired
    SfdcService sfdcService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServices.class);

    @RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.sfdc.create.waiver}") })
    public void createWaiverRequestInSfdc(String responseBody) {
        try {
            LOGGER.info("Input payload for waiver create payload {}", responseBody);
            sfdcService.processSdfcWaiver(responseBody);
        } catch (Exception e) {
            LOGGER.error("Error in createWaiverRequestInSfdc", e);
        }
    }

    @RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.sfdc.update.waiver}") })
    public void updateWaiverRequestInSfdc(String responseBody) {
        try {
            LOGGER.info("Input payload for waiver create payload {}", responseBody);
            sfdcService.processSdfcUpdateWaiver(responseBody);
        } catch (Exception e) {
            LOGGER.error("Error in createWaiverRequestInSfdc", e);
        }
    }
}
