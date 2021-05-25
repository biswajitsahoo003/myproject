package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;

/**
 * Jpa Repository class of Quote Gsc Details Table and its entity
 *
 * @author Kusuma Kumar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteGscDetailsRepository extends JpaRepository<QuoteGscDetail, Integer> {

	/**
	 * Find quote gsc details by quote gsc
	 *
	 * @param quotegsc
	 * @return {@link List<QuoteGscDetail>}
	 */
	List<QuoteGscDetail> findByQuoteGscIn(List<QuoteGsc> quotegsc);

	List<QuoteGscDetail> findByQuoteGsc(QuoteGsc quoteGsc);
}