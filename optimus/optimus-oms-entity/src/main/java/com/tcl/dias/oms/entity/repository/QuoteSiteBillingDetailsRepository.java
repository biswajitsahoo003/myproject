package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuoteSiteBillingDetails;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;

/**
 * 
 * This file contains the QuoteSiteBillingDetailsRepository.java class. Repository class
 *
 * @author Nithya S
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
@Repository
public interface QuoteSiteBillingDetailsRepository extends JpaRepository<QuoteSiteBillingDetails, Integer> {

	QuoteSiteBillingDetails findByQuoteIdAndQuoteIllSiteAndProductName(Integer quoteId, QuoteIllSite quoteIllSite, String productName);

	QuoteSiteBillingDetails findByQuoteIdAndQuoteNplLinkAndProductName(Integer quoteId, QuoteNplLink quoteNplLink, String productName);

	List<QuoteSiteBillingDetails> findByQuoteId(Integer quoteId);

	QuoteSiteBillingDetails findByQuoteIllSite(QuoteIllSite quoteIllSite);
	
	List<QuoteSiteBillingDetails> findByQuoteCode(String quoteCode);
}
