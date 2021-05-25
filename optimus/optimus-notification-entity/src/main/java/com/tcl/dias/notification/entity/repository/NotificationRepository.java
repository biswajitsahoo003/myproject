package com.tcl.dias.notification.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.notification.entity.entities.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>{

}
