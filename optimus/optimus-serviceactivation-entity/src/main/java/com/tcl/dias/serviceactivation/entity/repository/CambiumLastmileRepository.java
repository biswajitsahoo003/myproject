package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.CambiumLastmile;
/**
 * This file contains the CambiumLastmileRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CambiumLastmileRepository extends JpaRepository<CambiumLastmile, Integer> {

    CambiumLastmile findByLmComponent_LmComponentId(Integer lmComponentId);
}
