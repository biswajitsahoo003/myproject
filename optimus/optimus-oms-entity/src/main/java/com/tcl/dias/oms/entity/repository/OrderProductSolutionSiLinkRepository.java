package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderProductSolutionSiLink;
import com.tcl.dias.oms.entity.entities.ProductSolutionSiLink;

/**
 * This file contains the ProductSolutionRepository.java class. Repository class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderProductSolutionSiLinkRepository extends JpaRepository<OrderProductSolutionSiLink, Integer> {

	OrderProductSolutionSiLink findFirstByServiceIdOrderByIdDesc(String serviceId);
	OrderProductSolutionSiLink findFirstByProductSolutionId(Integer prodSolutionId);
	OrderProductSolutionSiLink findFirstByServiceIdAndQuoteToLeId(String serviceId, Integer quoteToleId);
	
	List<OrderProductSolutionSiLink> findByQuoteToLeId(Integer quoteToleId);
		
}
