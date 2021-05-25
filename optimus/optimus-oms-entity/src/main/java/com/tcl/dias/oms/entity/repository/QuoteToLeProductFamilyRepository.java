package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;

/**
 * 
 * This file contains the QuoteToLeProductFamilyRepository.java class.
 * Repository class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository

public interface QuoteToLeProductFamilyRepository extends JpaRepository<QuoteToLeProductFamily, Integer> {

	public QuoteToLeProductFamily findByQuoteToLeAndMstProductFamily(QuoteToLe quoteToLe,
			MstProductFamily mstProductFamily);

	QuoteToLeProductFamily findByQuoteToLe_Id(Integer quoteLeId);

	QuoteToLeProductFamily findByQuoteToLe_IdAndMstProductFamily_Name(Integer quoteToLeId, String familyName);

	@Query(value = "select * from quote_to_le_product_family where quote_to_le_id=:quoteToLeId", nativeQuery = true)
	List<QuoteToLeProductFamily> findByQuoteToLe(@Param(value = "quoteToLeId") Integer quoteToLeId);

	void deleteAllByQuoteToLe(QuoteToLe quoteToLe);

	List<QuoteToLeProductFamily> findByQuoteToLeInAndMstProductFamily(Iterable<QuoteToLe> quoteToLes,
			MstProductFamily mstProductFamily);

	List<QuoteToLeProductFamily> findByQuoteToLeIn(List<QuoteToLe> quoteToLes);
}
