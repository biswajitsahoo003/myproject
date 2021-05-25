package com.tcl.dias.servicehandover.network.listener;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ServiceFulfillmentJob;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceFulfillmentJobRepository;

@Component
public class ServiceHandoverCompletionListener {

    @Autowired
    ServiceFulfillmentJobRepository serviceFulfillmentJobRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHandoverCompletionListener.class);

    @RabbitListener(queuesToDeclare = {@Queue("${service.handover.complete.queue}") })
    public void serviceHandoverCompletion(String request) {
        try {
            String rmRequest = request;

            LOGGER.info("Service Handover Completion Request : {}", rmRequest);

            List<String> filters = Arrays.asList(rmRequest.split(","));

            ServiceFulfillmentJob serviceFulfillmentJob = serviceFulfillmentJobRepository
                    .findFirstByServiceCodeAndServiceIdAndTypeAndStatus(filters.get(0), Integer.valueOf(filters.get(1)) , "SERVICE HANDOVER", "INPROGRESS");

            if(serviceFulfillmentJob!=null){
                serviceFulfillmentJob.setStatus("SUCCESS");
                serviceFulfillmentJobRepository.save(serviceFulfillmentJob);
            }

        } catch (Exception e) {
            LOGGER.error("Error in serviceHandoverCompletion", e);
        }
    }

}

