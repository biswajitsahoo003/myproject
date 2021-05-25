package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;

/**
 * This file contains the ProductSolutionRepository.java class. Repository class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ProductSolutionRepository extends JpaRepository<ProductSolution, Integer> {

	public ProductSolution findByQuoteToLeProductFamilyAndMstProductOffering(
			QuoteToLeProductFamily quoteToLeProductFamily, MstProductOffering mstProductOffering);

	List<ProductSolution> findByQuoteToLeProductFamily(QuoteToLeProductFamily quoteToLeProductFamily);

	List<ProductSolution> findBySolutionCode(String solutionCode);
	
	Optional<ProductSolution> findById(Integer id);
	
	List<ProductSolution> findByQuoteToLeProductFamilyAndMstProductOffering_Id(
			QuoteToLeProductFamily quoteToLeProductFamily, Integer mstProductOfferingId);
	
	

	/**
	 * Find Product Solution by given reference code
	 *
	 * @param referenceCode
	 * @return {@link List<ProductSolution>}
	 */
	@Query(value = "select p.* from quote q, quote_to_le qle, quote_to_le_product_family qlf, product_solutions p " +
			"where q.id = qle.quote_id and qle.id = qlf.quote_to_le_id and qlf.id = p.quote_le_product_family_id and " +
			"q.quote_code=:referenceCode", nativeQuery = true)
	List<ProductSolution> findByReferenceCode(@Param("referenceCode") String referenceCode);
	
	
	List<ProductSolution> findByQuoteToLeProductFamilyAndIdNotIn(
			QuoteToLeProductFamily quoteToLeProductFamily, List<Integer> productSolutionIds);

	List<ProductSolution> findByQuoteToLeProductFamilyIn(Iterable<QuoteToLeProductFamily> quoteToLeProductFamily);
	
	/**
	 * Find Product Solution of IZO_SDWAN by given reference code
	 *
	 * @param referenceId
	 * @return {@link List<ProductSolution>}
	 */
	@Query(value = "select p.* from quote q, quote_to_le qle, quote_to_le_product_family qlf, product_solutions p " +
			"where q.id = qle.quote_id and qle.id = qlf.quote_to_le_id and qlf.id = p.quote_le_product_family_id and "
			+ "qlf.product_family_id=(select id from mst_product_family where name='IZO SDWAN')and " +
			"q.id=:referenceCode", nativeQuery = true)
	ProductSolution findByReferenceIdForIzoSdwan(@Param("referenceCode") Integer referenceCode);
	
	@Query(value = "select p.* from quote q, quote_to_le qle, quote_to_le_product_family qlf, product_solutions p " +
			"where q.id = qle.quote_id and qle.id = qlf.quote_to_le_id and qlf.id = p.quote_le_product_family_id and "
			+ "qlf.product_family_id=(select id from mst_product_family where name='vProxy')and " +
			"q.id=:referenceCode", nativeQuery = true)
	List<ProductSolution> findByReferenceIdForVproxy(@Param("referenceCode") Integer referenceCode);
	
	@Query(value = "select p.* from quote q, quote_to_le qle, quote_to_le_product_family qlf, product_solutions p " +
			"where q.id = qle.quote_id and qle.id = qlf.quote_to_le_id and qlf.id = p.quote_le_product_family_id and "
			+ "qlf.product_family_id=(select id from mst_product_family where name='vUTM')and " +
			"q.id=:referenceCode", nativeQuery = true)
	ProductSolution findByReferenceIdForVutm(@Param("referenceCode") Integer referenceCode);
	
}
