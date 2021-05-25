package com.tcl.dias.location.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.location.entity.entities.CountryToRegion;
import com.tcl.dias.location.entity.entities.MstCountry;
import com.tcl.dias.location.entity.entities.MstRegion;

/**
 * This file contains the MstCountryRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CountryToRegionRepository extends JpaRepository<CountryToRegion, Integer> {

	public  CountryToRegion findByCountryId(MstCountry mstCountry);

	

}
