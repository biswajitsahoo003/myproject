package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixWEBEXLNS;
import org.springframework.stereotype.Repository;

/**
 * Repository class for to retrieve service area matrix details for webex - LNS
 * product
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface ServiceAreaMatrixWEBEXLNSRepository extends JpaRepository<ServiceAreaMatrixWEBEXLNS, Integer> {

	/**
	 * Get All By Country Name
	 *
	 * @param country
	 * @return {@link List< ServiceAreaMatrixWEBEXLNS >}
	 */
	List<ServiceAreaMatrixWEBEXLNS> findByCountry(final String country);

	/**
	 * Get All By Country Name
	 *
	 * @return {@link List< ServiceAreaMatrixWEBEXLNS >}
	 */
	@Query(value = "select  s.country as name,s.code as code,s.isdcode as isdCode,s.is_packaged_ind as is_packaged_ind from ServiceAreaMatrixWEBEXLNS s")
	Set<Map<String, Object>> findByDistinctCountry();

	/**
	 * Get All By Country Name
	 *
	 * @return {@link Set<Map<String, Object>>}
	 */
	@Query(value = "select  s.country as name,s.code as code,s.isdcode as isdCode,s.is_packaged_ind as is_packaged_ind from ServiceAreaMatrixWEBEXLNS s where s.country='USA'")
	Set<Map<String, Object>> findByDistinctName();

	/**
	 * Get All By Country Name
	 *
	 * @return {@link Set<Map<String, Object>>}
	 */
	@Query(value = "select  s.country as name,s.code as code,s.isdcode as isdCode,s.is_packaged_ind as is_packaged_ind from ServiceAreaMatrixWEBEXLNS s where s.is_packaged_ind='Y'")
	Set<Map<String, Object>> findPackagedCountries();
}
