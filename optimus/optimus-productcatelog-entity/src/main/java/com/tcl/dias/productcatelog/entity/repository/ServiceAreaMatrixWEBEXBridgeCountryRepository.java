package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixWEBEXbridgecountrydetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Repository class for to retrieve service area matrix details for Webex All
 * products
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Repository
public interface ServiceAreaMatrixWEBEXBridgeCountryRepository
		extends JpaRepository<ServiceAreaMatrixWEBEXbridgecountrydetail, Integer> {

	/**
	 * Get products by country
	 *
	 * @return
	 */
	@Query(value = "select  s.country as name,s.code as code,s.isdcode as isdCode from ServiceAreaMatrixWEBEXbridgecountrydetail s where s.region='AMER'")
	Set<Map<String, Object>> findByDistinctAMER();

	/**
	 * Get products by country
	 *
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.country as name,s.code as code,s.isdcode as isdCode from ServiceAreaMatrixWEBEXbridgecountrydetail s where s.region='APAC'")
	Set<Map<String, Object>> findByDistinctAPAC();

	/**
	 * Get products by bridge region EMEA
	 *
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.country as name,s.code as code,s.isdcode as isdCode from ServiceAreaMatrixWEBEXbridgecountrydetail s where s.region='EMEA'")
	Set<Map<String, Object>> findByDistinctEMEA();

	/**
	 * Get products by bridge region
	 *
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.country as name,s.code as code,s.isdcode as isdCode from ServiceAreaMatrixWEBEXbridgecountrydetail s where s.region=?1")
	Set<Map<String, Object>> findByRegion(final String region);

	/**
	 * Get products by distinctname
	 *
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.country as name,s.code as code,s.isdcode as isdCode from ServiceAreaMatrixWEBEXbridgecountrydetail s where s.code='USA'")
	Set<Map<String, Object>> findByDistinctName();


	/**
	 * To get countries based on region.
	 *
	 * @return
	 */
	@Query(value = "select  s.country as name from ServiceAreaMatrixWEBEXbridgecountrydetail s where s.region=?1")
	List<String> findCountriesByRegion(String region);

}
