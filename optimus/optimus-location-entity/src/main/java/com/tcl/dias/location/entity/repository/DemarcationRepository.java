package com.tcl.dias.location.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.location.entity.entities.Demarcation;

/**
 * This file contains the DemarcationRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface DemarcationRepository extends JpaRepository<Demarcation, Integer> {

	@Query(value = "SELECT d.* FROM location l,demarcation d where l.demarcation_id=d.id and l.id= :locationId", nativeQuery = true)
	Demarcation findByLocationId(@Param("locationId") Integer locationId);

}
