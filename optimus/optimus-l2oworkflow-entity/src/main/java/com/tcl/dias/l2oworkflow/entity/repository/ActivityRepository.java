package com.tcl.dias.l2oworkflow.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.Activity;

/**
 * 
 * This file contains the ActivityRepository.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
	
}
