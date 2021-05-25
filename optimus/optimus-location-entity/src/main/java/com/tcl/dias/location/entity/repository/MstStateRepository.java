package com.tcl.dias.location.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.location.entity.entities.MstCountry;
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
public interface MstStateRepository extends JpaRepository<MstState, Integer> {
	
	public MstState findByName(String name);
	
	public List<MstState> findByMstCountry_Name(String countryName);
	
	public MstState findByNameAndMstCountry(String name,MstCountry mstCountry);
	
	public List<MstState> findByNameAndMstCountry_Name(String name,String mstCountry);

	@Query("SELECT s from MstState s where s.name like :name%")
	public List<MstState> findByFirstName(String name);




}
