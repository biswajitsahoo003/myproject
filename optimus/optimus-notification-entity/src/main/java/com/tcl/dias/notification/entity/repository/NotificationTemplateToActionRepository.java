package com.tcl.dias.notification.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.notification.entity.entities.NotificationTemplateToAction;

@Repository
public interface NotificationTemplateToActionRepository extends JpaRepository<NotificationTemplateToAction, Integer>{

}
