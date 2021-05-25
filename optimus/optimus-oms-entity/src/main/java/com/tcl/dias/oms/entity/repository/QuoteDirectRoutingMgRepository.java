package com.tcl.dias.oms.entity.repository;

import java.util.List;

import com.tcl.dias.oms.entity.entities.QuoteDirectRoutingCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuoteDirectRoutingMediaGateways;

/**
 * This file contains the QuoteDirectRoutingRepository.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface QuoteDirectRoutingMgRepository extends JpaRepository<QuoteDirectRoutingMediaGateways, Integer> {
	List<QuoteDirectRoutingMediaGateways> findByQuoteDirectRoutingCityId(Integer quoteDirectRoutingCityId);
	

	List<QuoteDirectRoutingMediaGateways> findByQuoteDirectRoutingCityIn(List<QuoteDirectRoutingCity> quoteDirectRoutingCityId);
}
