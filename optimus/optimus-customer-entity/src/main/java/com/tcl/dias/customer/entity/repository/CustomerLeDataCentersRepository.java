package com.tcl.dias.customer.entity.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.CustomerLegalDataCenters;
/**
 * This file contains the CustomerLeDataCentersRepository.java class.
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CustomerLeDataCentersRepository extends JpaRepository<CustomerLegalDataCenters, Integer> {
	public List<CustomerLegalDataCenters> findByCustomerleId(Integer customerleId);
}
