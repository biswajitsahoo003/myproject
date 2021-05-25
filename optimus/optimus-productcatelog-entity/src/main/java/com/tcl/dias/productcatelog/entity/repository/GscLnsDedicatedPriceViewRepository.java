package com.tcl.dias.productcatelog.entity.repository;

import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.GscLnsDedicatedPriceView;

/**
 * Repository for Gsc LNS Dedicated Price View
 * 
 * @author srraghav
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface GscLnsDedicatedPriceViewRepository extends JpaRepository<GscLnsDedicatedPriceView, Integer> {

	/**
	 * Get all GSC LNS Dedicated countries and prices
	 * 
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.originCountry as country, s.implementFeePerNumberNrc as nrc, s.feePerNumberMrcUsd as mrc, s.enterpriseSalesFloor as rate from GscLnsDedicatedPriceView s  order by s.originCountry")
	Set<Map<String, Object>> findByCountry();

}
