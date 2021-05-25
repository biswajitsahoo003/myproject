package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderSiteBillingDetails;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteSiteBillingDetails;


/**
 * 
 * This file contains the OrderSiteBillingDetailsRepository.java class. Repository class

 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
@Repository
public interface OrderSiteBillingDetailsRepository extends JpaRepository<OrderSiteBillingDetails, Integer> {
	
	OrderSiteBillingDetails findByOrderIllSite(OrderIllSite orderIllSite);
	
}
