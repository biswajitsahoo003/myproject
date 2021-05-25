package com.tcl.dias.oms.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.MfNplResponseDetailBean;
import com.tcl.dias.common.beans.MfResponseDetailBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.service.v1.ManualSiteFeasibilityService;

/**
 * Contains all listeners related to MF workflow
 *
 * @author ANUSHA UNNI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
@Transactional
public class WorkFlowListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowListener.class);

    @Autowired
    ManualSiteFeasibilityService manualSiteFeasibilityService;
    @Async
    @RabbitListener(queuesToDeclare = {@Queue("${save.mf.response.in.oms.mq}")})
    @Transactional
    public void saveMFResponseInOMS(String request) {
        try {
            List<MfResponseDetailBean> mfResponseDetailBeans = Utils.fromJson(request, new TypeReference<List<MfResponseDetailBean>>() {
            });
            LOGGER.info("Received {} responses from MF",mfResponseDetailBeans);
            manualSiteFeasibilityService.saveMfResponseInSiteFeasibile(mfResponseDetailBeans);
        } catch (Exception e) {
            LOGGER.warn("Error in saving mf response details", e);
        }
    }
    
    @Async
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.save.mf.npl.response.in.oms.mq}")})
    public void saveMFNplResponseInOMS(String request) {
        try {
            List<MfNplResponseDetailBean> mfResponseDetailBeans = Utils.fromJson(request, new TypeReference<List<MfNplResponseDetailBean>>() {
            });
            LOGGER.info("Received {} link responses from MF",mfResponseDetailBeans);
            manualSiteFeasibilityService.saveMfNplResponseInLinkFeasible(mfResponseDetailBeans);
        } catch (Exception e) {
            LOGGER.warn("Error in saving mf response details", e);
        }
    }
}
