package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.oms.entity.entities.OrderPrice;

/**
 * This file contains the OrderPriceRepository.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface OrderPriceRepository extends JpaRepository<OrderPrice, Integer> {

	OrderPrice findByReferenceIdAndReferenceName(String referenceId, String referenceName);
	
	List<OrderPrice> findByQuoteId(Integer quoteId);

}
