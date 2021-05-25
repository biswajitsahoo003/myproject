package com.tcl.dias.oms.entity.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteVrfSites;



/**
 * This file contains the QuoteVrfSitesRepository.java class.
 * 
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteVrfSitesRepository  extends JpaRepository<QuoteVrfSites, Integer>{
	
	QuoteVrfSites findByQuoteIllSiteAndVrfNameAndSiteType(QuoteIllSite quoteIllSite,String vrfNmae,String siteType);
	List<QuoteVrfSites> findByQuoteIllSite(QuoteIllSite quoteIllSite);
	


}
