package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstPartnerAbbreviation;

/**
 * This file contains the MstPartnerAbbreviationRepository.java class.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */

@Repository
public interface MstPartnerAbbreviationRepository extends JpaRepository<MstPartnerAbbreviation, Integer> {

	MstPartnerAbbreviation findByName(String name);

}
