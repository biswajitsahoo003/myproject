package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.TeamsDRLicenseCountry;

/**
 * The Repository class for the vw_mstmdr_license_country table
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface TeamsDRLicenseCountryRepository extends JpaRepository<TeamsDRLicenseCountry, Integer> {

	/**
	 * Method to find codes based on countries.
	 * 
	 * @param countries
	 * @return
	 */
	@Query(value = "SELECT v.countryCode from TeamsDRLicenseCountry v where v.country IN (:countries)")
	Set<String> findByCountries(@Param("countries") Set<String> countries);

	/**
	 * Find licenses and providers by list of countries and agreement type
	 *
	 * @param countries
	 * @param agreementType
	 * @return
	 */
	@Query(value = "select disp_nm as license,vendor as provider from product_catalog_uat.vw_mstmdr_license where disp_nm in\n" +
			" (SELECT disp_nm FROM product_catalog_uat.vw_mstmdr_license_country where country in\n" +
			" (:countries) \n" + "" +
			"group by disp_nm  having count(disp_nm) = :countryCount) and agreement = :agreementType", nativeQuery = true)
	List<Map<String, String>> findByCountriesAndAgreement(@Param("countries") Set<String> countries,
			@Param("countryCount") Integer countryCount, @Param("agreementType") String agreementType);
}
