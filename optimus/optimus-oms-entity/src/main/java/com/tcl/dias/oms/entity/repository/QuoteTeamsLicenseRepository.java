package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.QuoteTeamsDR;
import com.tcl.dias.oms.entity.entities.QuoteTeamsLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This file contains the QuoteTeamsLicenseRepository.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface QuoteTeamsLicenseRepository extends JpaRepository<QuoteTeamsLicense, Integer> {
	List<QuoteTeamsLicense> findByQuoteTeamsDR(QuoteTeamsDR quoteTeamsDR);
}
