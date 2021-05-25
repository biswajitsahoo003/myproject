package com.tcl.dias.productcatelog.entity.repository;

import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tcl.dias.productcatelog.entity.entities.WebexLnsSharedPayPerUsePricing;
import org.springframework.stereotype.Repository;

/**
 * The persistent class for the vw_ucaas_lns_shared_pay_per_use_price_book
 * database table.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface WebexLnsSharedPayPerUsePricingRepository
		extends JpaRepository<WebexLnsSharedPayPerUsePricing, Integer> {

	/**
	 * Get all LNS Shared Pay per seat countries and prices
	 *
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.country as country, s.nrc as nrc, s.mrc as mrc, s.ratePerMin as rate from WebexLnsSharedPayPerUsePricing s  order by s.country")
	Set<Map<String, Object>> findByCountry();
}