package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuotePriceAudit;

/**
 * 
 * Repository class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuotePriceAuditRepository extends JpaRepository<QuotePriceAudit, Integer> {

	List<QuotePriceAudit> findByQuotePrice(QuotePrice quotePrice);
	
	List<QuotePriceAudit> findByQuotePriceOrderByCreatedTimeDesc(QuotePrice quotePrice);
}
