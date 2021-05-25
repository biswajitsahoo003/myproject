package com.tcl.dias.networkaugment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.NotificationMailJob;

/**
 * 
 * This file contains the NotificationMailJobRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface NotificationMailJobRepository extends JpaRepository<NotificationMailJob, Integer> {

	List<NotificationMailJob> findByStatus(String status);
	
	List<NotificationMailJob> findByStatusAndEmail(String status,String email);

	
	List<NotificationMailJob> findByScOrderIdAndStatus(Integer scOrderId,String status);

    List<NotificationMailJob> findByServiceCodeAndStatus(String serviceCode, String status);

	List<NotificationMailJob> findByServiceCodeAndStatusAndEmail(String serviceCode, String status, String email);

	List<NotificationMailJob> findByServiceCodeAndStatusAndType(String serviceCode, String status, String type);

	List<NotificationMailJob> findByServiceCodeAndStatusAndEmailAndType(String serviceCode, String aNew, String customerMail, String internal);
}
