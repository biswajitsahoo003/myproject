package com.tcl.dias.servicefulfillment.entity.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommericalComponent;

/**
 * 
 * This file contains the ScServiceCommercialComponentRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ScServiceCommercialComponentRepository extends JpaRepository<ScServiceCommericalComponent, Integer> {

}
