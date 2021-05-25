package com.tcl.dias.serviceactivation.kafka.consumer;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.serviceactivation.integratemux.beans.SiaInfoResponseBean;
import com.tcl.dias.serviceactivation.kafka.service.PlannedEventService;
import com.tcl.dias.servicefulfillment.entity.entities.PlannedEvent;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.PlannedEventRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * Kafka listener used to consume messages from kafka queues.
 *
 * @author VISHESH AWASTHI
 */
@Component
public class KafkaListener {

    @Autowired
    RuntimeService runtimeService;

    private static final Logger log = LoggerFactory.getLogger(KafkaListener.class);
    @Autowired
    NetworkInventoryRepository networkInventoryRepository;

    @Autowired
    PlannedEventRepository plannedEventRepository;

    @Autowired
    PlannedEventService plannedEventService;

    @Autowired
    ComponentAndAttributeService componentAndAttributeService;

    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;

    @org.springframework.kafka.annotation.KafkaListener(topics = "PullSIAInfo", groupId = "optimus-resp146")
    public void consumer(String response) throws TclCommonException {
        log.info("Consumed message ... {}", response);
        SiaInfoResponseBean siaInfoResponseBean = null;
        if (Objects.nonNull(response))
            siaInfoResponseBean = Utils.convertJsonToObject(response, SiaInfoResponseBean.class);
        try {
            plannedEventService.saveResponseToNetworkInventory(siaInfoResponseBean);
            PlannedEvent plannedEvent = plannedEventService.updatePlannedEventDetails(siaInfoResponseBean);
            if(!StringUtils.isEmpty(siaInfoResponseBean.getStatus())){
                //Status field will have error message if any
                SiaInfoResponseBean finalSiaInfoResponseBean = siaInfoResponseBean;
                ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(plannedEvent.getServiceCode(),"INPROGRESS");
                if(Objects.nonNull(scServiceDetail)){
                    componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,"preparePlannedEventErrorReason",
                            componentAndAttributeService.getErrorMessageDetails(finalSiaInfoResponseBean.getStatus(), "0000"),
                            AttributeConstants.ERROR_MESSAGE,"prepare-planned-event"
                            );
                }
            }

            if (plannedEvent != null && plannedEvent.getProcessInstanceId()!=null) {
                long totalEvents = plannedEventRepository.findAllByServiceCodeAndProcessInstanceIdAndPreFetched(plannedEvent.getServiceCode(), plannedEvent.getProcessInstanceId(), true)
                        .stream()
                        .filter(event -> "C-PROGRESS".equalsIgnoreCase(event.getOptimusStatus()))
                        .count();
                if (totalEvents == 0) {
                    Execution execution = runtimeService.createExecutionQuery().processInstanceId(plannedEvent.getProcessInstanceId())
                            .activityId("prepare-planned-event-async").singleResult();
                    if (execution != null) {
                        log.info("WfProcessInstId={} executionId={}", execution.getProcessInstanceId(), execution.getId());
                        runtimeService.trigger(execution.getId());
                    }
                }
            }
        } catch (TclCommonException e) {
            log.error("Exception occurred in kafka listener -> {}", e);
        }
    }

}