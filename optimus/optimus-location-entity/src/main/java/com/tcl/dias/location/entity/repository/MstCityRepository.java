package com.tcl.dias.location.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.location.entity.entities.MstCity;
import com.tcl.dias.location.entity.entities.MstState;

/**
 * This file contains the MstCityRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstCityRepository extends JpaRepository<MstCity, Integer> {

	public MstCity findByName(String name);

	public List<MstCity> findByMstState_Name(String cityName);

	public MstCity findByNameAndMstState(String cityName, MstState state);



	/**
	 * Find all cities by country
	 *
	 * @param country
	 * @return {@link MstCity}
	 */
	@Query(value = "SELECT * FROM mst_city city INNER JOIN mst_state state ON city.state_id = state.id "
			+ "INNER JOIN mst_country country ON state.country_id = country.id WHERE country.name = :country", nativeQuery = true)
	List<MstCity> findCityByCountry(@Param("country") final String country);

	/**
	 * Find all cities by state
	 *
	 * @param cityName
	 * @param name
	 * @return {@link MstCity}
	 */
	List<MstCity> findByNameAndMstState_Name(String cityName, String name);

}
