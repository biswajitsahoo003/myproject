package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscSla;
import com.tcl.dias.oms.entity.entities.SlaMaster;

/**
 * Jpa Repository class of Quote Gsc Sla Table and its entity
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteGscSlaRepository extends JpaRepository<QuoteGscSla, Integer> {

	/**
	 * Get QuoteGscSla based on quotegsc and slamaster
	 * 
	 * @param quoteGsc
	 * @param slaMaster
	 * @return List<QuoteGscSla>
	 */
	List<QuoteGscSla> findByQuoteGscAndSlaMaster(final QuoteGsc quoteGsc, final SlaMaster slaMaster);

	/**
	 * Get QuoteGscSla by QuoteGsc
	 * 
	 * @param quotegsc
	 * @return List<QuoteGscSla>
	 */
	List<QuoteGscSla> findByQuoteGsc(QuoteGsc quotegsc);

}