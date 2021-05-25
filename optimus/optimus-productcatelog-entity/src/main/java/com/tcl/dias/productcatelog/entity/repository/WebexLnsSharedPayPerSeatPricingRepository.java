package com.tcl.dias.productcatelog.entity.repository;

import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tcl.dias.productcatelog.entity.entities.WebexLnsSharedPayPerSeatPricing;
import org.springframework.stereotype.Repository;

/**
 * The persistent class for the vw_ucaas_lns_shared_pay_per_seat_price_book
 * database table.
 * 
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface WebexLnsSharedPayPerSeatPricingRepository
		extends JpaRepository<WebexLnsSharedPayPerSeatPricing, Integer> {

	/**
	 * Get all LNS Shared Pay per seat countries and prices
	 * 
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.country as country, s.nrc as nrc, s.mrc as mrc, s.ratePerMin as rate from WebexLnsSharedPayPerSeatPricing s  order by s.country")
	Set<Map<String, Object>> findByCountry();
}