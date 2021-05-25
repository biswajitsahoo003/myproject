package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteToLe;

/**
 * Jpa Repository class of Quote Gsc Table and its entity
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteGscRepository extends JpaRepository<QuoteGsc, Integer> {

	/**
	 * Get All QuoteGsc by Product Solution and Status
	 *
	 * @param productSolution
	 * @param status
	 * @return {@link List<QuoteGsc>}
	 */
	List<QuoteGsc> findByProductSolutionAndStatus(final ProductSolution productSolution, final byte status);

	/**
	 * Get QuoteGsc By ID
	 *
	 * @param gscId
	 * @param status
	 * @return {@link Optional<QuoteGsc>}
	 */
	Optional<QuoteGsc> findByIdAndStatus(Integer gscId, byte status);

	/**
	 * Delete Quote GSC by Product Solution
	 *
	 * @param productSolution
	 */
	void deleteByProductSolution(final ProductSolution productSolution);
	
	/**
	 * Get QuoteGsc by QuoteToLe Id And QuoteVersion
	 *
	 * @param quoteToLeId
	 * @param quoteVersion
	 * @return
	 */

	/**
	 * Get QuoteGsc by QuoteToLe And QuoteVersion
	 *
	 * @param quoteToLe
	 * @param quoteVersion
	 * @return
	 */
	List<QuoteGsc> findByQuoteToLeId(Integer quoteToLeId);

	List<QuoteGsc> findByQuoteToLe(QuoteToLe quoteToLe);

	/**
	 * Get Product Locations by QuoteToLe ID
	 * @param quoteLeId
	 * @return
	 */
	@Query(value = "select gsc.product_name as product_name, gsc_details.src as origin, gsc_details.dest as destination " +
			"from quote_gsc gsc, quote_gsc_details gsc_details where gsc.id=gsc_details.quote_gsc_id and gsc" +
			".quote_to_le_id =:quoteLeId", nativeQuery = true)
    List<Map<String, Object>> findProductLocations(@Param("quoteLeId")Integer quoteLeId);

	List<QuoteGsc> findByProductSolutionInAndStatus(final Iterable<ProductSolution> productSolution, final byte status);

	List<QuoteGsc> findByQuoteToLeIn(Iterable<QuoteToLe> quoteToLe);
}