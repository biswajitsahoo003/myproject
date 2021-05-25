package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteNplLinkSla;
import com.tcl.dias.oms.entity.entities.SlaMaster;
/**
 * This file contains the QuoteNplLinkSlaRepository.java class.
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteNplLinkSlaRepository extends JpaRepository<QuoteNplLinkSla, Integer> {
	List<QuoteNplLinkSla> findByQuoteNplLinkAndSlaMaster(QuoteNplLink quoteNplLink, SlaMaster slaMaster);

	List<QuoteNplLinkSla> findByQuoteNplLink(QuoteNplLink quoteNplLink);
	
	List<QuoteNplLinkSla> findByQuoteNplLink_Id(Integer linkId);
	
	
}
