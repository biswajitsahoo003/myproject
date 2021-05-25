package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixTeamsDRCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;

/**
 * The persistent class for the vw_mstmdr_ctry_city database table.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface ServiceAreaMatrixTeamsDRCityRepository extends JpaRepository<ServiceAreaMatrixTeamsDRCity, Integer> {

	/**
	 * Get all cities based on country
	 * @param country
	 * @return Set<Map<String, Object>>
	 */
	@Query(value = "select  s.countryCode as code,s.countryName as country,s.cityName as city from ServiceAreaMatrixTeamsDRCity s where s.countryName=?1 order by s.cityName")
	Set<Map<String, Object>> findByCountry(final String country);


}