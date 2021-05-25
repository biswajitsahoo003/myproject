package com.tcl.dias.notification.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.notification.entity.entities.NotificationRecipient;
@Repository
public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, Integer>{

}
