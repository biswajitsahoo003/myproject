package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCUIFNView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Repository class for to retrieve service area matrix details for GSC - UIFN product
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ServiceAreaMatrixGSCUIFNViewRepository extends JpaRepository<ServiceAreaMatrixGSCUIFNView, Integer> {

	/**
	 * Get all products based on country name
	 *
	 * @param countryName
	 * @return
	 */
	List<ServiceAreaMatrixGSCUIFNView> findByIsoCountryName(final String countryName);

	/**
	 * Get ISO name and code
	 *
	 * @return {@link Set<Map<String, String>>}
	 */
	// TODO : There is no pricing for Iceland in Pricing Engine, once the data present - reset this query
	@Query(value = "select distinct s.iso3CountryCode as code, s.isoCountryName as name, s.internationalCountryDialCodes as isdCode from ServiceAreaMatrixGSCUIFNView s where s.isoCountryName not like 'Iceland' and s.countryTypeSrcDest = 'Both'")
	Set<Map<String, Object>> findDistinctByIso3CountryCodeAndAndIsoCountryName();

	@Query(value = "select distinct s.iso3CountryCode as code, s.isoCountryName as name, s.internationalCountryDialCodes as isdCode from ServiceAreaMatrixGSCUIFNView s where s.isoCountryName not like 'Iceland'" )
	Set<Map<String, Object>> findDistinctByIso3CountryCodeAndAndIsoCountryNameAndCountryTypeSrcDest();
}
