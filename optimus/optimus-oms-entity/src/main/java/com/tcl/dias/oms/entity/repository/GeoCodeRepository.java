package com.tcl.dias.oms.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.GeoCode;

/**
 * 
 * Repository class for GeoCode entity
 * 
 *
 * @author Prabhu A 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface GeoCodeRepository extends JpaRepository<GeoCode, Integer> {

	Optional<GeoCode> findByZipcode(String zipcode);
}
