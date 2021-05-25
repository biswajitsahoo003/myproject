package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCAudioCnfView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Repository class for to retrieve service area matrix details for GSC - Audio conference product
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ServiceAreaMatrixGSCAudioCnfViewRepository
		extends JpaRepository<ServiceAreaMatrixGSCAudioCnfView, Integer> {

	/**
	 * Get products based on country name
	 *
	 * @param countryName
	 * @return {@link ServiceAreaMatrixGSCAudioCnfView}
	 */
	List<ServiceAreaMatrixGSCAudioCnfView> findByIsoCountryName(final String countryName);

	/**
	 * Get ISO Code and name
	 *
	 * @return {@link Set<Map<String, String>>}
	 */
	@Query(value = "select distinct s.iso3CountryCode as code, s.isoCountryName as name from ServiceAreaMatrixGSCAudioCnfView s")
	Set<Map<String, String>> findDistinctByIso3CountryCodeAndAndIsoCountryName();
}
