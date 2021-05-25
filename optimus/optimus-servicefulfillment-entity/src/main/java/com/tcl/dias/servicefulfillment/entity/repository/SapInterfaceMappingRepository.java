package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.SapInterfaceMapping;
/**
 * 
 * This file contains the SapInterfaceMappingRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface SapInterfaceMappingRepository extends JpaRepository<SapInterfaceMapping, Integer>{
	List<SapInterfaceMapping> findByStatus(Byte status);
}
