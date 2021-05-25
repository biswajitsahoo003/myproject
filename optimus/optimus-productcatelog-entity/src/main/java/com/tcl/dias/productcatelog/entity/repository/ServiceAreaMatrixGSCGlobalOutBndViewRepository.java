package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCGlobalOutBndView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Repository class for to retrieve service area matrix details for GSC - Global Outbound product
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ServiceAreaMatrixGSCGlobalOutBndViewRepository
		extends JpaRepository<ServiceAreaMatrixGSCGlobalOutBndView, Integer> {

	/**
	 * Get products by country name
	 *
	 * @param countryName
	 * @return
	 */
	List<ServiceAreaMatrixGSCGlobalOutBndView> findByIsoCountryName(final String countryName);

	/**
	 * Get ISO name and code
	 *
	 * @return {@link Set<Map<String, String>>}
	 */
	@Query(value = "select distinct s.iso3CountryCode as code, s.isoCountryName as name, s.internationalCountryDialCodes as isdCode from ServiceAreaMatrixGSCGlobalOutBndView s")
	Set<Map<String, Object>> findDistinctByIso3CountryCodeAndAndIsoCountryName();
}
