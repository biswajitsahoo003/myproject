package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixTeamsDR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;

/**
 * The persistent class for the vw_mstmdr_country_matrix database table.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface ServiceAreaMatrixTeamsDRRepository extends JpaRepository<ServiceAreaMatrixTeamsDR, Integer> {

	/**
	 * Get all countries
	 *
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.country as country,s.countryIsoCode as code,s.isdCode as isdCode,s.isRegulated as isRegulated,s.isException as isException,s.isGsc as isGsc ,s.isDomesticVoice as isDomesticVoice,s.isGlobalOutbound as isGlobalOutbound from ServiceAreaMatrixTeamsDR s order by s.country")
	Set<Map<String, Object>> findByCountry();
}