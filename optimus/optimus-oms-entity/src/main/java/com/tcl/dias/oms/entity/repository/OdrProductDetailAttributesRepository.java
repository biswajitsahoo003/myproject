package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrProductDetailAttributes;

/**
 * 
 * This file contains the repository for the Order product detail attribute
 * table that is used in the IPC to persist orders product solution attributes.
 * 
 *
 * @author Mohamed Danish A
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Repository
public interface OdrProductDetailAttributesRepository extends JpaRepository<OdrProductDetailAttributes, Integer> {

}
