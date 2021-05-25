package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCLNSView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Repository class for to retrieve service area matrix details for GSC - LNS product
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ServiceAreaMatrixGSCLNSViewRepository extends JpaRepository<ServiceAreaMatrixGSCLNSView, Integer> {

	/**
	 * Get All By Country Name
	 *
	 * @param country
	 * @return {@link List<ServiceAreaMatrixGSCLNSView>}
	 */
	List<ServiceAreaMatrixGSCLNSView> findByIsoCountryName(final String country);

	/**
	 * Get ISO name and code
	 *
	 * @return {@link Set<Map<String, String>>}
	 */
	@Query(value = "select distinct s.iso3CountryCode as code, s.isoCountryName as name, s.internationalCountryDialCodes as isdCode from ServiceAreaMatrixGSCLNSView s")
	Set<Map<String, Object>> findDistinctByIso3CountryCodeAndAndIsoCountryName();

	@Query(value = "select distinct s.cityCode as code, s.cityName as name from ServiceAreaMatrixGSCLNSView s where s.cityCode is not null and s.cityName is not null")
	Set<Map<String, Object>> findLnsCityCodesAndNames();

}
