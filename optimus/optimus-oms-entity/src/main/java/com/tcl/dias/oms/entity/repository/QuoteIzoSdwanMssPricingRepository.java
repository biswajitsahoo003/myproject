package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuoteIzoSdwanMssPricing;

/**
 * 
 * @author vpachava
 *
 */
@Repository
public interface QuoteIzoSdwanMssPricingRepository extends JpaRepository<QuoteIzoSdwanMssPricing, Integer>{
	
	List<QuoteIzoSdwanMssPricing> findBySolutionId(Integer solutionId);

}
