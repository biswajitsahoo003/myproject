package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.NotificationMailJob;

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
	
	List<NotificationMailJob> findByScOrderIdAndStatus(Integer scOrderId,String status);
}
