package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuoteSiteDifferentialCommercial;
import com.tcl.dias.oms.entity.entities.QuoteToLe;

/**
 * This file contains the QuoteRepository.java class. Repository class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteSiteDifferentialCommercialRepository  extends JpaRepository<QuoteSiteDifferentialCommercial, Integer> {
	
	public List<QuoteSiteDifferentialCommercial> findByQuoteSiteId(Integer quoteSiteId);
	
	public List<QuoteSiteDifferentialCommercial> findByQuoteToLe(QuoteToLe quoteToLe);
	
	public List<QuoteSiteDifferentialCommercial> findByQuoteSiteIdAndServiceType(Integer quoteSiteId, String serviceType);
	
	public List<QuoteSiteDifferentialCommercial> findByQuoteLinkId(Integer quoteLinkId);
	
	public List<QuoteSiteDifferentialCommercial> findByQuoteSiteCodeAndServiceType(String quoteSiteCode, String serviceType);
	
	public List<QuoteSiteDifferentialCommercial> findByQuoteLinkCodeAndServiceType(String quoteLinkCode, String serviceType);
	
	public List<QuoteSiteDifferentialCommercial> findByQuoteSiteCodeAndQuoteCode(String siteCode, String quoteCode);
	
	public List<QuoteSiteDifferentialCommercial> findByQuoteLinkCodeAndQuoteCode(String linkCode, String quoteCode);
	

}
