package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrOrder;

/**
 * 
 * This file contains repository class of OdrOrder entity
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OdrOrderRepository extends JpaRepository<OdrOrder, Integer> {

	public OdrOrder findByOpOrderCode(String opOrderCode);

}
