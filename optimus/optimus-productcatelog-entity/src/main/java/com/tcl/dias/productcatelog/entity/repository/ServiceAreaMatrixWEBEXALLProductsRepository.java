package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixWEBEXALLProducts;
import org.springframework.stereotype.Repository;

/**
 * Repository class for to retrieve service area matrix details for Webex All
 * products
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Repository
public interface ServiceAreaMatrixWEBEXALLProductsRepository
		extends JpaRepository<ServiceAreaMatrixWEBEXALLProducts, Integer> {
	/**
	 * Get All By Country Name
	 *
	 * @param country
	 * @return {@link List< ServiceAreaMatrixWEBEXALLProducts >}
	 */
	List<ServiceAreaMatrixWEBEXALLProducts> findByCountry(final String country);

	/**
	 * findByDistinctCountry.
	 *
	 * @return {@link Set<Map<String, Object>>}
	 */
	@Query(value = "select  s.country as name,s.code as code,s.isdcode as isdCode from ServiceAreaMatrixWEBEXALLProducts s")
	Set<Map<String, Object>> findByDistinctCountry();

	/**
	 * findByDistinctName.
	 *
	 * @return {@link Set<Map<String, Object>>}
	 */
	@Query(value = "select  s.country as name,s.code as code,s.isdcode as isdCode from ServiceAreaMatrixWEBEXALLProducts s where s.country='USA'")
	Set<Map<String, Object>> findByDistinctName();
}
