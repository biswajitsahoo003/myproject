package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderCwbAuditTrailDetails;
import com.tcl.dias.oms.entity.entities.OrderSiteCategory;

/**
 * This file contains the OrderAuditCwbTrailDetailsRepository.java class.
 * 
 *
 * @author Santosh.Tidke
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface OrderSiteCategoryRepository extends JpaRepository<OrderSiteCategory, Integer>{
	
	public List<OrderSiteCategory> findAllByOrderId(Integer orderId);
}
