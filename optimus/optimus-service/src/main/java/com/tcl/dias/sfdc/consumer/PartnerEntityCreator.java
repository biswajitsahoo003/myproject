package com.tcl.dias.sfdc.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.sfdc.services.SfdcService;

/**
 * Create Partner Entity in SFDC
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class PartnerEntityCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartnerEntityCreator.class);

    @Autowired
    private SfdcService sfdcService;

    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.partner.entity.create}")})
    public void createPartnerEntity(String requestBody) {
        try {
            LOGGER.info("Creating partner entity input payload {}", requestBody);
            sfdcService.processSdfcPartnerEntity(requestBody);
        } catch (Exception e) {
            LOGGER.warn("Error in createPartnerEntity", e);
        }
    }

    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.partner.entity.contact.create}")})
    public void createPartnerEntityContact(String requestBody) {
        try {
            LOGGER.info("Creating partner entity contact input payload {}", requestBody);
            sfdcService.processSdfcPartnerEntityContact(requestBody);
        } catch (Exception e) {
            LOGGER.warn("Error in createPartnerEntityContact", e);
        }
    }
}
