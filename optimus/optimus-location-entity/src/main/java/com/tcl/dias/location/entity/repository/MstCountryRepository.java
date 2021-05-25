package com.tcl.dias.location.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.location.entity.entities.MstCountry;

/**
 * This file contains the MstCountryRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstCountryRepository extends JpaRepository<MstCountry, Integer> {

	public MstCountry findByName(String name);

	/**
	 * Find all countries by code
	 *
	 * @param countriesCode
	 * @return {@link MstCountry}
	 */
	List<MstCountry> findByCodeIn(List<String> countriesCode);

	@Query(value="SELECT distinct name FROM mst_country",nativeQuery = true)
	List<String> findDistinctCountries();

}
