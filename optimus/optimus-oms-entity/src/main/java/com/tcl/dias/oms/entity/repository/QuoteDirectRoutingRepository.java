package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.QuoteDirectRouting;
import com.tcl.dias.oms.entity.entities.QuoteTeamsDR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This file contains the QuoteDirectRoutingRepository.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface QuoteDirectRoutingRepository extends JpaRepository<QuoteDirectRouting, Integer> {
    List<QuoteDirectRouting> findByQuoteTeamsDR(QuoteTeamsDR quoteTeamsDR);
    Optional<QuoteDirectRouting> findByQuoteTeamsDRAndCountry(QuoteTeamsDR quoteTeamsDR,String country);
}
