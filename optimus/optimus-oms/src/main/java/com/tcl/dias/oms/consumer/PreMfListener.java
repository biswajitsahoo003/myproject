package com.tcl.dias.oms.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.Mf3DSiteStatus;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.service.v1.ManualSiteFeasibilityService;

/**
 * Contains all listeners related to pre MF workflow
 *
 * @author krutsrin
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Service
@Transactional
public class PreMfListener {


    private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowListener.class);

    @Autowired
    ManualSiteFeasibilityService manualSiteFeasibilityService;
    @Async
    @RabbitListener(queuesToDeclare = {@Queue("${save.pre.mf.response.in.oms.mq}")})
    public void saveMFResponseInOMS(String request) {
        try {
            List<Mf3DSiteStatus> mfSiteStatuses = Utils.fromJson(request, new TypeReference<List<Mf3DSiteStatus>>() {
            });
            LOGGER.info("Received {} site statuses key value pair",mfSiteStatuses);
            manualSiteFeasibilityService.saveMfResponseInPreMfRequest(mfSiteStatuses);
        } catch (Exception e) {
            LOGGER.warn("Error in saving pre mf site status details details", e);
        }
    }


}
