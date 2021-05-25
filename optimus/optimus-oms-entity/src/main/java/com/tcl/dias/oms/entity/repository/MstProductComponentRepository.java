package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.oms.entity.entities.MstProductComponent;

/**
 * This file contains the MstProductComponentRepository.java class. Repository
 * class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface MstProductComponentRepository extends JpaRepository<MstProductComponent, Integer> {

	MstProductComponent findByName(String name);

	List<MstProductComponent> findByNameAndStatus(String name, Byte status);
	
	List<MstProductComponent> findByNameIn(List<String> name);
	
	

}
