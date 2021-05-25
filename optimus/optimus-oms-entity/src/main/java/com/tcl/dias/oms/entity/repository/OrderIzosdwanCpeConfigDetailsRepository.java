package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderCwbAuditTrailDetails;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanCpeConfigDetails;

/**
 * This file contains the OrderIzosdwanCpeConfigDetailsRepository.java class.
 * 
 *
 * @author Santosh.Tidke
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface OrderIzosdwanCpeConfigDetailsRepository extends JpaRepository<OrderIzosdwanCpeConfigDetails, Integer> {
	
	public List<OrderIzosdwanCpeConfigDetails> findAllByOrderLeId(Integer orderLeId);
	public List<OrderIzosdwanCpeConfigDetails> findAllByOrderLeIdAndLocationId(Integer orderLeId, Integer locationId);
}
