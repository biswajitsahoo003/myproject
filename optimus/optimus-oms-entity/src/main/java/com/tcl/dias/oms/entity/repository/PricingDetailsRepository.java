package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.PricingEngineResponse;

/**
 * Repository class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface PricingDetailsRepository extends JpaRepository<PricingEngineResponse, Integer> {

	List<PricingEngineResponse> findBySiteCodeAndPricingType(String siteCode, String pricingType);
	
	PricingEngineResponse findFirstBySiteCodeAndPricingTypeOrderByDateTimeDesc(String siteCode, String pricingType);
	
	List<PricingEngineResponse> findBySiteCodeAndPricingTypeNotIn(String siteCode,String type);
	
	List<PricingEngineResponse> findBySiteCodeInAndPricingTypeNotIn(List<String> site,String pricingType);
	
	List<PricingEngineResponse> findBySiteCode(String siteCode);
	
	PricingEngineResponse findFirstBySiteCodeOrderByIdDesc(String siteCode);
	
	List<PricingEngineResponse> findBySiteCodeAndPricingTypeOrderByIdDesc(String siteCode, String pricingType);
	
	List<PricingEngineResponse> findBySiteCodeInAndPricingType(List<String> site,String pricingType);
}
