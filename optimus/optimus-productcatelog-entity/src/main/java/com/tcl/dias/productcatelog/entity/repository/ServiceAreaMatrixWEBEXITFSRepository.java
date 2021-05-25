package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixWEBEXITFS;
import org.springframework.stereotype.Repository;

/**
 * The repository class for the vw_service_area_matrix_ucaas_itfs database
 * table.
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Repository
public interface ServiceAreaMatrixWEBEXITFSRepository
		extends JpaRepository<ServiceAreaMatrixWEBEXITFS, Integer> {

	/**
	 * Get All By Country Name
	 *
	 * @param country
	 * @return {@link List< ServiceAreaMatrixWEBEXITFS >}
	 */
	List<ServiceAreaMatrixWEBEXITFS> findByCountry(final String country);

	/**
	 * Get All By Country Name
	 *
	 * @return {@link Set<Map<String, Object>>}
	 */
	@Query(value = "select  s.country as name from ServiceAreaMatrixWEBEXITFS s")
	Set<Map<String, Object>> findByDistinctCountry();

	/**
	 * Get All By Country Name
	 *
	 * @return {@link Set<Map<String, Object>>}
	 */
	@Query(value = "select  s.country as name from ServiceAreaMatrixWEBEXITFS s where s.country='USA'")
	Set<Map<String, Object>> findByDistinctName();
}
