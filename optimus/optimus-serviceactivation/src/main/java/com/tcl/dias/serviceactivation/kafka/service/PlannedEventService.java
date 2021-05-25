package com.tcl.dias.serviceactivation.kafka.service;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.serviceactivation.integratemux.beans.SiaInfoResponseBean;
import com.tcl.dias.servicefulfillment.entity.entities.PlannedEvent;
import com.tcl.dias.servicefulfillment.entity.repository.PlannedEventRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlannedEventService {

    private static final Logger log = LoggerFactory.getLogger(PlannedEventService.class);

    @Autowired
    NetworkInventoryRepository networkInventoryRepository;

    @Autowired
    PlannedEventRepository plannedEventRepository;

    @Transactional(readOnly = false)
    public void saveResponseToNetworkInventory(SiaInfoResponseBean response) throws TclCommonException {
        Optional<NetworkInventory> networkInventoryOptional = networkInventoryRepository.findByRequestId(response.getRequestid());
        if (networkInventoryOptional.isPresent()) {
            NetworkInventory networkInventory = networkInventoryOptional.get();
            networkInventory.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            networkInventory.setResponse(Utils.convertObjectToJson(response));
            networkInventoryRepository.save(networkInventory);
        }
    }

    @Transactional(readOnly = false)
    public PlannedEvent updatePlannedEventDetails(SiaInfoResponseBean siaInfoResponseBean) {
        log.info("SiaInfoResponseBean:RequestId : {}", siaInfoResponseBean.getRequestid());
        Optional<PlannedEvent> plannedEventOptional = Optional.ofNullable(plannedEventRepository.findByPlannedEventId(siaInfoResponseBean.getRequestid()));
        log.info("Planned Event record --> {}", plannedEventOptional.isPresent());
        if (plannedEventOptional.isPresent()) {
            PlannedEvent plannedEvent = plannedEventOptional.get();
            log.info("update plannedEvent staretd for --> {}", plannedEvent.getPlannedEventId());
            plannedEvent.setOptimusStatus("C-CLOSED");
            plannedEvent.setPlannedEventStatus(siaInfoResponseBean.getStatusSummary().getSANSAstatus());
            plannedEvent.setPlannedEventConflictStatus(siaInfoResponseBean.getStatusSummary().getConflictingPEstatus());
            if (!plannedEvent.getPreFetched() && !StringUtils.isEmpty(siaInfoResponseBean.getStatus()))
                plannedEvent.setErrorStatus(siaInfoResponseBean.getStatus());
            return plannedEventRepository.save(plannedEvent);
        }
        return null;
    }

}
