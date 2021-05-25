package com.tcl.dias.notification.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.notification.entity.entities.NotificationAction;
@Repository
public interface NotificationActionRepository extends JpaRepository<NotificationAction, Integer>{
	
	public List<NotificationAction> findByIdNotIn(List<Integer> ids);
	
	public NotificationAction findByNameAndErfPrdCatalogProductName(String name, String productName);
	
	public List<NotificationAction> findByIsActive(Integer flag);
}
