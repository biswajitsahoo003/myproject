package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.ProductSolutionSiLink;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;

/**
 * This file contains the ProductSolutionRepository.java class. Repository class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ProductSolutionSiLinkRepository extends JpaRepository<ProductSolutionSiLink, Integer> {

	ProductSolutionSiLink findFirstByServiceIdOrderByIdDesc(String serviceId);
	ProductSolutionSiLink findFirstByProductSolutionId(Integer prodSolutionId);
	ProductSolutionSiLink findFirstByServiceIdAndQuoteToLeId(String serviceId, Integer quoteToleId);
	
	List<ProductSolutionSiLink> findByQuoteToLeId(Integer quoteToleId);
		
}
