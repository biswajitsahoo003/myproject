package com.tcl.dias.location.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.location.entity.entities.MstLocality;

/**
 * This file contains the MstLocalityRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstLocalityRepository extends JpaRepository<MstLocality, Integer> {

	public List<MstLocality> findByName(String name);

}
