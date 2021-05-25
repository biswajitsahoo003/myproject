package com.tcl.dias.productcatelog.entity.repository;

import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tcl.dias.productcatelog.entity.entities.WebexItfsDedicatedAllCountryPricing;
import org.springframework.stereotype.Repository;

/**
 * The persistent class for the vw_ucaas_itfs_dedicated_all_ctry_price_book
 * database table.
 * 
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface WebexItfsDedicatedAllCountryPricingRepository
		extends JpaRepository<WebexItfsDedicatedAllCountryPricing, Integer> {

	/**
	 * Get all countries and prices of dedicated ITFS
	 * 
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.originCountry as country, s.phoneType as phoneType, s.implementFeePerNumberNrc as nrc, s.recurringFeePerNumberMrc as mrc, s.ratePerMin as rate from WebexItfsDedicatedAllCountryPricing s order by s.originCountry")
	Set<Map<String, Object>> findByOriginCountry();
}