package com.tcl.dias.notification.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.notification.entity.entities.NotificationTemplate;
@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Integer>{
	
	public NotificationTemplate findByCode(String code); 
}
