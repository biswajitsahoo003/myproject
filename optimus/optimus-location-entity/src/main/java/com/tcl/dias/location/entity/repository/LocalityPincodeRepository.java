package com.tcl.dias.location.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.location.entity.entities.LocalityPincode;

/**
 * This file contains the LocalityPincodeRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface LocalityPincodeRepository extends JpaRepository<LocalityPincode, Integer> {

}
