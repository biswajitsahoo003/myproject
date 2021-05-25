package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteSla;
import com.tcl.dias.oms.entity.entities.SlaMaster;

/**
 * This file contains the QuoteIllSiteSlaRepository.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteIllSiteSlaRepository  extends JpaRepository<QuoteIllSiteSla, Integer>{
	
	
	List<QuoteIllSiteSla> findByQuoteIllSiteAndSlaMaster(QuoteIllSite quoteIllSite,SlaMaster slaMaster);
	
	List<QuoteIllSiteSla> findByQuoteIllSite(QuoteIllSite quoteIllSite);


}
