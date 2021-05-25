package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.LmComponent;

import java.util.List;

/**
 * This file contains the LmComponentRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface LmComponentRepository extends JpaRepository<LmComponent, Integer> {
    List<LmComponent> findByServiceDetail_id(Integer serviceDetailId);
}
