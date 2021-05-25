package com.tcl.dias.productcatelog.entity.repository;

import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.GscItfsDedicatedPriceView;

/**
 * Repository for Gsc ITFS Dedicated Price View
 * 
 * @author srraghav
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface GscItfsDedicatedPriceViewRepository extends JpaRepository<GscItfsDedicatedPriceView, Integer> {

	/**
	 * Get all GSC ITFS Dedicated countries and prices
	 * 
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.originCountry as country, s.phoneType as phoneType, s.implementFeePerNumberNrc as nrc, s.recurringFeePerNumberMrc as mrc, s.enterpriseSalesFloor as rate from GscItfsDedicatedPriceView s order by s.originCountry")
	Set<Map<String, Object>> findByOriginCountry();

}
