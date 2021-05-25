package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrWebexServiceCommercial;

/**
 * 
 * This file contains the OdrWebexServiceCommercialRepository.java class.
 * 
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Repository
public interface OdrWebexServiceCommercialRepository extends JpaRepository<OdrWebexServiceCommercial, Integer> {

}
