package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrTeamsDRServiceCommercial;

/**
 * This file contains the OdrWebexServiceCommercialRepository.java class.
 *
 * @author SyedAli
 */

@Repository
public interface OdrTeamsDRServiceCommercialRepository extends JpaRepository<OdrTeamsDRServiceCommercial, Integer> {

}
