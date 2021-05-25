package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;

/**
 * 
 * This file contains the IllSiteRepository.java class. Repository class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface IllSiteRepository extends JpaRepository<QuoteIllSite, Integer> {

	public QuoteIllSite findByIdAndStatus(Integer id, byte status);

	public List<QuoteIllSite> findByIdInAndStatus(List<Integer> ids, byte status);
	
	public void deleteByProductSolution(ProductSolution solution);

	public List<QuoteIllSite> findBySiteCodeAndStatus(String code, byte status);
	
	public Integer countByProductSolution(ProductSolution solution);

	List<QuoteIllSite> findByProductSolutionAndStatus(ProductSolution productSolution, 
			byte status);

	@Query(value = "SELECT qill.* FROM quote_ill_sites qill,product_solutions ps where qill.product_solutions_id=ps.id and qill.status=:status and ps.id=:productSolutionId", nativeQuery = true)
	List<QuoteIllSite> findByProductSolutionIdAndStatus(
			@Param("productSolutionId") Integer productSolutionId,
			@Param("status") byte status);

	/**
	 * This query joins then product family .solutions to get the IllSite
	 * findByQuoteId
	 * 
	 * @param quoteId
	 * @param productFamilyId
	 * @param productOfferingId
	 * @return List<IllSite>
	 */
	@Query(value = "SELECT il.* FROM quote qte,quote_to_le qle,quote_to_le_product_family qlepf,product_solutions ps,ill_sites il\r\n"
			+ "where qle.quote_id=qte.id and qte.id=:quoteId and qlepf.quote_to_le_id and qlepf.product_family_id=:productFamilyId\r\n"
			+ "and ps.quote_le_product_family_id=qlepf.id and ps.product_offering_id=:productOfferingId\r\n"
			+ "and ps.id=il.product_solutions_id", nativeQuery = true)
	public List<QuoteIllSite> findByQuoteId(@Param("quoteId") Integer quoteId,
			@Param("productFamilyId") Integer productFamilyId, @Param("productOfferingId") Integer productOfferingId);
	
	/**
	 * 
	 * Get Location IDs by QuoteID
	 * @param quoteId
	 * @return
	 */
	@Query(value = "SELECT il.erf_loc_siteb_location_id FROM quote_ill_sites as il where product_solutions_id in(SELECT id FROM product_solutions where quote_le_product_family_id in(SELECT id FROM quote_to_le_product_family where quote_to_le_id in(SELECT id FROM quote_to_le where quote_id in(select id from quote where id=:quoteId))))", nativeQuery = true)
	public List<Integer> getLocationIdsByQuoteId(@Param("quoteId") Integer quoteId);
	
	/**
	 * 
	 * Get task triggered sites by QuoteID
	 * @param quoteId
	 * @return
	 */
	@Query(value = "SELECT il.* FROM quote_ill_sites as il where product_solutions_id in(SELECT id FROM product_solutions where quote_le_product_family_id in(SELECT id FROM quote_to_le_product_family where quote_to_le_id in(SELECT id FROM quote_to_le where quote_id in(select id from quote where id=:quoteId)))) and is_task_triggered=1", nativeQuery = true)
	public List<QuoteIllSite> getTaskTriggeredSites(@Param("quoteId") Integer quoteId);

	QuoteIllSite findByIdAndMfStatus(Integer siteId, String mfStatus);

	/**
	 * Get sites by quote code
	 *
	 * @param quoteCode
	 * @return {@link QuoteIllSite}
	 */
	@Query(value = "select qls.* from quote_ill_sites qls left join product_solutions ps on qls.product_solutions_id = ps.id " +
			"left join quote_to_le_product_family qtlpf on ps.quote_le_product_family_id = qtlpf.id " +
			"left join quote_to_le qtl on qtlpf.quote_to_le_id = qtl.id left join quote q on qtl.quote_id = q.id " +
			"where q.quote_code = :quoteCode and qls.feasibility=1", nativeQuery = true)
	List<QuoteIllSite> findSites(@Param("quoteCode") String quoteCode);
	

	/**
	 * Get sites by quote code
	 *
	 * @param quoteCode
	 * @return {@link QuoteIllSite}
	 */
	@Query(value = "select qls.* from quote_ill_sites qls left join product_solutions ps on qls.product_solutions_id = ps.id " +
			"left join quote_to_le_product_family qtlpf on ps.quote_le_product_family_id = qtlpf.id " +
			"left join quote_to_le qtl on qtlpf.quote_to_le_id = qtl.id left join quote q on qtl.quote_id = q.id " +
			"where q.quote_code = :quoteCode", nativeQuery = true)
	List<QuoteIllSite> findIllSites(@Param("quoteCode") String quoteCode);


	public List<QuoteIllSite> findByProductSolution_Id(Integer productSolutionId);
	
	
	/**
	 * 
	 * Get task inprogress sites by QuoteID
	 * @param quoteId
	 * @return
	 */
	@Query(value = "SELECT il.* FROM quote_ill_sites as il where product_solutions_id in(SELECT id FROM product_solutions where quote_le_product_family_id in(SELECT id FROM quote_to_le_product_family where quote_to_le_id in(SELECT id FROM quote_to_le where quote_id in(select id from quote where id=:quoteId)))) and is_task_triggered=1 or is_task_triggered=2 ", nativeQuery = true)
	public List<QuoteIllSite> getTaskInprogressSites(@Param("quoteId") Integer quoteId);


}
