package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.WebexItfsSharedPayPerUsePricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;

/**
 * The persistent class for the vw_ucaas_itfs_shared_pay_per_use_price_book
 * database table.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface WebexItfsSharedPayPerUsePricingRepository extends JpaRepository<WebexItfsSharedPayPerUsePricing,Integer> {

	/**
	 * Get all countries and prices of dedicated ITFS
	 *
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.originCountry as country, s.phoneType as phoneType, s.implementFeePerNumberNrc as nrc, s.recurringFeePerNumberMrc as mrc, s.ratePerMin as rate from WebexItfsSharedPayPerUsePricing s order by s.originCountry")
	Set<Map<String, Object>> findByOriginCountry();
}
