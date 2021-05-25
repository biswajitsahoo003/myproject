package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.ProductSolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcl.dias.oms.entity.entities.QuoteCloud;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This file contains the QuoteCloudRepository.java class. Repository class
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface QuoteCloudRepository extends JpaRepository<QuoteCloud, Integer>, JpaSpecificationExecutor<QuoteCloud> {

   List<QuoteCloud> findByProductSolutionAndStatus(ProductSolution productSolution, byte status);

   List<QuoteCloud> findByProductSolution(ProductSolution productSolution);

   QuoteCloud findByIdAndQuoteToLeIdAndStatus(Integer id, Integer quoteToLeId, byte status);

   List<QuoteCloud> findByQuoteToLeIdAndStatusAndFpStatus(Integer quoteToLeId, byte status, String fpStatus);

   List<QuoteCloud> findByQuoteToLeIdAndStatus(Integer quoteToLeId, byte status);

   Optional<QuoteCloud> findByQuoteToLeIdAndResourceDisplayNameAndStatus(Integer quoteToLeId, String resourceDisplayName, byte status);
   
   Optional<QuoteCloud> findByQuoteToLeIdAndResourceDisplayNameAndStatusAndFpStatusNotIn(Integer quoteToLeId, String resourceDisplayName, byte status, String fpStatus);
   
   List<QuoteCloud> findByQuoteToLeIdAndResourceDisplayNameInAndStatus(Integer quoteToLeId, List<String> resourceDisplayName, byte status);
   
   /**
	 * Find productSolutionId based on QuoteCloud
	 * @param quoteCloudId
	 * @return
	 * 
	 */
	@Query(value="SELECT distinct qco.product_solutions_id FROM quote_cloud qco "
			+ "WHERE qco.quote_id=(SELECT quote_id FROM quote_cloud qc WHERE qc.id=:quoteCloudId) and qco.resource_display_name=:resourceDisplayName and qco.status=:status",nativeQuery=true)
   Integer findByQuoteCloudIdAndResourceDisplayNameAndStatus(Integer quoteCloudId, String resourceDisplayName, byte status);
   
   /**
	 * Find productName based on QuoteCloud
	 * @param quoteCloudId
	 * @return
	 * 
	 */
	@Query(value="SELECT distinct msp.product_name FROM quote_cloud qco JOIN product_solutions ps "
			+ "ON ps.id=qco.product_solutions_id JOIN mst_product_offerings msp ON ps.product_offering_id=msp.id "
			+ "WHERE qco.quote_id=(SELECT quote_id FROM quote_cloud qc WHERE qc.id=:quoteCloudId)",nativeQuery=true)
	List<String> getProductNameBasedOnQuoteCloudId(@Param("quoteCloudId") Integer quoteCloudId);
	
	/**
	 * Find productSolutionIds based on QuoteCloud
	 * @param quoteCloudId
	 * @return
	 * 
	 */
	@Query(value="SELECT distinct qco.product_solutions_id FROM quote_cloud qco "
			+ "WHERE qco.quote_id=(SELECT quote_id FROM quote_cloud qc WHERE qc.id=:quoteCloudId) and qco.status=:status",nativeQuery=true)
    List<Integer> findByQuoteCloudIdAndStatus(Integer quoteCloudId, byte status);
   
	QuoteCloud findFirstByParentCloudCodeOrderByIdDesc(String cloudCode);
	
	QuoteCloud findFirstByCloudCodeOrderByIdDesc(String cloudCode);
}