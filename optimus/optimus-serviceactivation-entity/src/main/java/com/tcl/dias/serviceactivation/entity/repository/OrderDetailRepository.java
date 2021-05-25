package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
/**
 * This file contains the OrderDetailRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
	
	OrderDetail findByOrderId(Integer orderId);
	
	OrderDetail findByExtOrderrefId(String erfOrderId);

	
}
