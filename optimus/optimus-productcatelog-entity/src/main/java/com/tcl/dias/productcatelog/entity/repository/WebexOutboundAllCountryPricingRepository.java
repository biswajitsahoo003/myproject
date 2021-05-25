package com.tcl.dias.productcatelog.entity.repository;

import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tcl.dias.productcatelog.entity.entities.WebexOutboundAllCountryPricing;
import org.springframework.stereotype.Repository;

/**
 * The persistent class for the vw_ucaas_ob_all_ctry_price_book database table.
 * 
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface WebexOutboundAllCountryPricingRepository
		extends JpaRepository<WebexOutboundAllCountryPricing, Integer> {

	/**
	 * Get all Global Outbound all countries and prices
	 * 
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.country as country, s.phoneType as phoneType, s.destId as destId, s.destinationName as destName, s.ratePerMin as rate from WebexOutboundAllCountryPricing s")
	Set<Map<String, Object>> findByCountry();
}