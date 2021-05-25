package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.RadwinLastmile;
/**
 * This file contains the RadwinLastmileRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface RadwinLastmileRepository extends JpaRepository<RadwinLastmile, Integer> {
    RadwinLastmile findByLmComponent_LmComponentId(Integer lmComponentId);
}
