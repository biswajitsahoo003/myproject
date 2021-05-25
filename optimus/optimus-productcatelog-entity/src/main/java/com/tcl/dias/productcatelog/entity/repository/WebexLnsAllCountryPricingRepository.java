package com.tcl.dias.productcatelog.entity.repository;

import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.WebexLnsAllCountryPricing;

/**
 * The persistent class for the vw_ucaas_lns_all_ctry_price_book database table.
 * 
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface WebexLnsAllCountryPricingRepository extends JpaRepository<WebexLnsAllCountryPricing, Integer> {

	/**
	 * Get all LNS countries and prices
	 * 
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.country as country, s.nrc as nrc, s.mrc as mrc, s.ratePerMin as rate from WebexLnsAllCountryPricing s order by s.country")
	Set<Map<String, Object>> findByCountry();

	/**
	 * Find by country name
	 *
	 * @param country
	 * @return
	 */
	WebexLnsAllCountryPricing findByCountry(String country);
}