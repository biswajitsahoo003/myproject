package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderCwbAuditTrailDetails;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanCpeConfigDetails;

/**
 * This file contains the OrderAuditCwbTrailDetailsRepository.java class.
 * 
 *
 * @author Santosh.Tidke
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface OrderAuditCwbTrailDetailsRepository extends JpaRepository<OrderCwbAuditTrailDetails, Integer> {
	
	public List<OrderCwbAuditTrailDetails> findAllByOrderId(Integer orderId);
}
