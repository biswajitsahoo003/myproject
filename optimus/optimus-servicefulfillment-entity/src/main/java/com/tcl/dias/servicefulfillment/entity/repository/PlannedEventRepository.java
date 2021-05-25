package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.PlannedEvent;

import java.util.List;

@Repository
public interface PlannedEventRepository extends JpaRepository<PlannedEvent, Integer> {

	PlannedEvent findByPlannedEventId(String plannedEventId);

    List<PlannedEvent> findAllByServiceCode(String seviceCode);

    List<PlannedEvent> findAllByServiceCodeAndProcessInstanceIdAndPreFetched(String serviceCode,String processInstanceId,Boolean isPreFetched);
}
