package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCDVView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Repository class for to retrieve service area matrix details for GSC - Domestic voice product
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ServiceAreaMatrixGSCDVViewRepository extends JpaRepository<ServiceAreaMatrixGSCDVView, Integer> {

	/**
	 * Get all products by country name
	 *
	 * @param countryName
	 * @return
	 */
	List<ServiceAreaMatrixGSCDVView> findByIsoCountryName(final String countryName);

	/**
	 * Get ISO name and code
	 *
	 * @return {@link Set<Map<String, String>>}
	 */
	@Query(value = "select distinct s.iso3CountryCode as code, s.isoCountryName as name, s.internationalCountryDialCodes as isdCode from ServiceAreaMatrixGSCDVView s")
	Set<Map<String, Object>> findDistinctByIso3CountryCodeAndAndIsoCountryName();

	/**
	 * Get ISO name and code and MPLS
	 *
	 * @return {@link Set<Map<String, Object>>}
	 */
	@Query(value = "select distinct s.iso3CountryCode as code, s.isoCountryName as name, s.internationalCountryDialCodes as isdCode from ServiceAreaMatrixGSCDVView s where isApplicableOnMPLS = 'Y'")
	Set<Map<String, Object>> findDistinctByIso3CountryCodeAndAndIsoCountryNameAndMPLS();

}
