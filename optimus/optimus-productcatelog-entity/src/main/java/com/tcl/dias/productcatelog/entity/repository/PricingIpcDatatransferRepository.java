package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.PricingIpcDatatransfer;

/**
 * 
 * This file contains the PricingIpcDatatransferRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface PricingIpcDatatransferRepository extends JpaRepository<PricingIpcDatatransfer, Integer> {
	
	
	@Query(value="SELECT * FROM pricing_ipc_datatransfer where location_code=:locationCode and (:bw between start_limit and end_limit)",nativeQuery=true)
	public PricingIpcDatatransfer findByLocationCodeAndStartAndEnd(@Param("locationCode") String locationCode,@Param("bw") Integer bw);

	public List<PricingIpcDatatransfer> findByLocationCodeOrderByStartLimitAsc(String locationCode);
	
	@Query(value="SELECT location_code, MIN(start_limit) AS start_limit, MAX(end_limit) AS end_limit, unit, price FROM pricing_ipc_datatransfer WHERE location_code = :locationCode GROUP BY location_code, unit, price ORDER BY start_limit", nativeQuery=true)
	public List<Map<String, Object>> findByLocationCodeGroupByPriceOrderByStartLimitAsc(String locationCode);
}
