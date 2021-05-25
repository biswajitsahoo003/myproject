package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.WimaxLastmile;

/**
 * This file contains the WimaxLastmileRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface WimaxLastmileRepository extends JpaRepository<WimaxLastmile, Integer> {
    WimaxLastmile findByLmComponent_LmComponentId(Integer lmComponentId);
}
