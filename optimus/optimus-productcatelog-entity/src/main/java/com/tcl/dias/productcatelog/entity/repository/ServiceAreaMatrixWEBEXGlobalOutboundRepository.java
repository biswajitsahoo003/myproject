package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixWEBEXGlobalOutbound;
import org.springframework.stereotype.Repository;

/**
 * Repository class for to retrieve service area matrix details for Webex Global
 * Outbound product
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Repository
public interface ServiceAreaMatrixWEBEXGlobalOutboundRepository extends JpaRepository<ServiceAreaMatrixWEBEXGlobalOutbound, Integer> {

	/**
	 * Get products by country
	 *
	 * @param country
	 * @return List<ServiceAreaMatrixWEBEXGlobalOutbound>
	 */
	List<ServiceAreaMatrixWEBEXGlobalOutbound> findByCountry(final String country);

	/**
	 * Get products by country
	 *
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.country as name,s.code as code,s.isdcode as isdCode,s.is_packaged_ind as is_packaged_ind from ServiceAreaMatrixWEBEXGlobalOutbound s")
	Set<Map<String, Object>> findByDistinctCountry();

	/**
	 * Get products by name
	 *
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.country as name,s.code as code,s.isdcode as isdCode,s.is_packaged_ind as is_packaged_ind from ServiceAreaMatrixWEBEXGlobalOutbound s where s.country='USA'")
	Set<Map<String, Object>> findByDistinctName();

	/**
	 * Get products by countries.
	 *
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.country as name,s.code as code,s.isdcode as isdCode,s.is_packaged_ind as is_packaged_ind from ServiceAreaMatrixWEBEXGlobalOutbound s where s.is_packaged_ind='Y'")
	Set<Map<String, Object>> findPackagedCountries();

}
