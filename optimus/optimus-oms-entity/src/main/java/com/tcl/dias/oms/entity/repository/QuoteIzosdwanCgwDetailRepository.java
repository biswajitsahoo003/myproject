package com.tcl.dias.oms.entity.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanCgwDetail;

/**
 * 
 * This is the repository class for QuoteIzosdwanCgwDetail entity
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteIzosdwanCgwDetailRepository extends JpaRepository<QuoteIzosdwanCgwDetail, Integer>{
	
	public List<QuoteIzosdwanCgwDetail> findByQuote(Quote quote);
	
	public List<QuoteIzosdwanCgwDetail> findByQuote_Id(Integer quoteId);
}
