package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIzoSdwanAttributeValues;

/**
 * 
 * This file contains the QuoteIzoSdwanAttributeValuesRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteIzoSdwanAttributeValuesRepository extends JpaRepository<QuoteIzoSdwanAttributeValues, Integer>{

	List<QuoteIzoSdwanAttributeValues> findByDisplayValueAndQuote(String displayValue,Quote quote);
		
	List<QuoteIzoSdwanAttributeValues> findByQuote(Quote quote);
	
	List<QuoteIzoSdwanAttributeValues> findByDisplayValueAndQuote_id(String displayValue,Integer quoteId);
	
	List<QuoteIzoSdwanAttributeValues> findByAttributeValue(String attributeValue);
	
	List<QuoteIzoSdwanAttributeValues> findByDisplayValueInAndQuote(List<String> displayValue,Quote quote);

}
