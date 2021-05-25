package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.PricingIpcCustomerNetMargin;

/**
 * 
 * This file holds the repository class for the IPC Customer Net Margin
 * percentage component.
 * 
 * @author Mohamed Danish A
 * @link http://www.tatacommunications.com/
 * @copyright 2019 TATA Communications Limited
 * 
 */
@Repository
public interface PricingIpcCustomerNetMarginRepository extends JpaRepository<PricingIpcCustomerNetMargin, Integer> {

	/**
	 * Find ipc customer net margin price based on cityCode and customerIs
	 * @param cityCode and customerId
	 * @return
	 * 
	 */
	@Query(value = "select * from pricing_ipc_customer_net_margin where city_code = :cityCode and (customer_id = :customerId "
			+ "or customer_id is null) order by customer_id desc limit 1", nativeQuery = true)
	public PricingIpcCustomerNetMargin getCustomerNetMargin(@Param("cityCode") String cityCode,
			@Param("customerId") Integer customerId);

	/**
	 * Find ipc customer net margin price based on cityCode and countryCode
	 * @param cityCode and countryCode
	 * @return
	 * 
	 */
	@Query(value = "select * from pricing_ipc_customer_net_margin where country_code = :countryCode and ( :cityCode is null or "
			+ "city_code = :cityCode) and customer_id is null;", nativeQuery = true)
	public Optional<List<PricingIpcCustomerNetMargin>> findByCountryCodeAndCityCodeAndCustomerIdIsNull(
			@Param("countryCode") String countryCode, @Param("cityCode") String cityCode);

	/**
	 * Update ipc customer net margin price based on netMargin,finalDiscount,customerId,cityCode and countryCode
	 * @param netMargin,finalDiscount,customerId,cityCode and countryCode
	 * @return
	 * 
	 */
	@Modifying
	@Query(value = "update pricing_ipc_customer_net_margin set net_margin_percentage = :netMargin, "
			+ "final_discount_percentage = :finalDiscount where country_code = :countryCode and ( :cityCode is null or "
			+ "city_code = :cityCode) and customer_id = :customerId", nativeQuery = true)
	public int updateCustomerNetMargin(@Param("netMargin") Double netMargin, @Param("finalDiscount") Double finalDiscount, 
			@Param("countryCode") String countryCode, @Param("cityCode") String cityCode, 
			@Param("customerId") Integer customerId);

}
