package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.NotificationTrigger;

@Repository
public interface NotificationTriggerRepository extends JpaRepository<NotificationTrigger, Integer> {
	
}
