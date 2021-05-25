package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.QuoteDirectRouting;
import com.tcl.dias.oms.entity.entities.QuoteDirectRoutingCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This file contains the QuoteDirectRoutingCityRepository.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface QuoteDirectRoutingCityRepository extends JpaRepository<QuoteDirectRoutingCity, Integer> {
	List<QuoteDirectRoutingCity> findByQuoteDirectRoutingId(Integer quoteDirectRoutingId);
	

	List<QuoteDirectRoutingCity> findByQuoteDirectRoutingIn(List<QuoteDirectRouting> quoteDirectRoutingId);

}
