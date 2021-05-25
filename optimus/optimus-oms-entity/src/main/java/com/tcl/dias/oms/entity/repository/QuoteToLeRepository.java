package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteToLe;

/**
 * This file contains the QuoteToLeRepository.java class. Repository class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteToLeRepository extends JpaRepository<QuoteToLe, Integer> {

	public List<QuoteToLe> findByQuote(Quote quote);

	public List<QuoteToLe> findByErfCusCustomerLegalEntityId(Integer customerLegalEntityId);

	public List<QuoteToLe> findByErfCusCustomerLegalEntityIdAndStageNot(Integer customerLegalEntityId, String stage);

	public List<QuoteToLe> findByErfCusCustomerLegalEntityIdIsNullAndQuote_CustomerAndStageNot(Customer customer,
			String stage);

	public List<QuoteToLe> findByQuote_Id(Integer quoteId);

	public Optional<QuoteToLe> findById(Integer quoteToLeId);

	public List<QuoteToLe> findByAmendmentParentOrderCode(String orderCode);

	public List<QuoteToLe> findByErfCusCustomerLegalEntityIdIn(List<Integer> customerLegalEntityIds);

	public List<QuoteToLe> findByQuote_QuoteCode(String quoteCode);

	public List<QuoteToLe> findByTpsSfdcOptyId(String sfdcId);
	
	public QuoteToLe findByQuoteAndId(Quote quote,Integer id);
	
	public long countByQuoteInAndStageIn(List<Quote> quotes,List<String> stages);
	
	public long countByErfCusCustomerLegalEntityIdInAndStageIn(List<Integer> customerLeIds,List<String> stages);
	
	public List<QuoteToLe> findByCancelledParentOrderCode(String orderCode);
	
	@Query(value="select quote_id from quote_to_le qle where qle.id = ?1",nativeQuery = true)
	Integer getQuoteId(Integer quoteLeId);

	public QuoteToLe findByQuoteLeCode(String quoteLeCode);
}
