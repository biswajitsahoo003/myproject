package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.QuoteToLe;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteTnc;

/**
 * 
 * Repository Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public interface QuoteTncRepository extends JpaRepository<QuoteTnc, Integer> {

	QuoteTnc findByQuote(Quote quote);
	
	QuoteTnc findByQuoteId(Integer quoteId);

	QuoteTnc findByQuoteToLe(QuoteToLe quoteToLe);

	QuoteTnc findByQuoteToLe_Id(Integer quoteLeId);
}
