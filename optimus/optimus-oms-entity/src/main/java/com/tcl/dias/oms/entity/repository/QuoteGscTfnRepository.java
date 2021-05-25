package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.entity.entities.QuoteGscTfn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Jpa Repository for Quote GSC TFN numbers
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteGscTfnRepository extends JpaRepository<QuoteGscTfn, Integer> {

	/**
	 * Find TF numbers by quote gsc detail
	 * @param quoteGscDetail
	 * @return
	 */
	List<QuoteGscTfn> findByQuoteGscDetail(QuoteGscDetail quoteGscDetail);
}
