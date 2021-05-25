package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcl.dias.oms.entity.entities.QuotePrice;

/**
 * This file contains the QuotePriceRepository.java class. Repository class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface QuotePriceRepository extends JpaRepository<QuotePrice, Integer> {

	QuotePrice findByReferenceId(String referenceId);

	QuotePrice findByReferenceIdAndReferenceName(String referenceId, String referenceName);

	QuotePrice findFirstByReferenceIdAndReferenceName(String referenceId, String referenceName);

	List<QuotePrice> findByReferenceNameAndReferenceId(String referenceName, String referenceId);

	List<QuotePrice> findByQuoteId(Integer quoteId);
	
	@Query(value ="select * from quote_price where quote_id=:quoteId order by id desc" ,nativeQuery=true)
	List<QuotePrice> findByQuoteIdOrderByDesc(@Param("quoteId") Integer quoteId);

	List<QuotePrice> findByReferenceNameAndReferenceIdIn(String referenceName, List<String> referenceIds);
	
	QuotePrice findByReferenceNameAndReferenceIdAndQuoteId(String refName,String referenceId,Integer quoteId);
	
	QuotePrice findByReferenceIdAndQuoteId(String referenceId,Integer quoteId);
	
	List<QuotePrice> findByReferenceIdAndQuoteIdOrderByIdDesc(String referenceId,Integer quoteId);
	
	QuotePrice findFirstByReferenceNameAndReferenceIdAndQuoteId(String refName,String referenceId,Integer quoteId);
	
	@Query(value = "select sum(effective_arc) as arc, sum(effective_mrc) as mrc, sum(effective_nrc) "
			+ "as nrc from quote_price where reference_id in(select id from quote_product_component where "
			+ "product_component_id in(select id from mst_product_component where name=:componentName) and "
			+ "reference_id in(select id from quote_izosdwan_sites where product_solutions_id=:productSolutionId) and "
			+ "reference_name=:referenceName) and reference_name=:pricingReferenceName", nativeQuery = true)
	Map<String, Object> getSummaryPriceComponentWiseForSoution(String componentName, Integer productSolutionId,
			String referenceName, String pricingReferenceName);

	List<QuotePrice> findByReferenceIdIn(List<String> referenceIds);
}
