package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstBillingStatesAndCities;


/**
 * This file contains the TaskRepository.java class.
 * 
 *
 * @author MAYANSHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface MstBillingStatesAndCitiesRepository extends JpaRepository<MstBillingStatesAndCities, Integer> {
	
	
														  
}
