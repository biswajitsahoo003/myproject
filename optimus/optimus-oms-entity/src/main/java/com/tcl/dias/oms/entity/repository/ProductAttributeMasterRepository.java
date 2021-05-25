package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;

/**
 * This file contains the ProductAttributeMasterRepository.java class.
 * Repository class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface ProductAttributeMasterRepository extends JpaRepository<ProductAttributeMaster, Integer> {

	List<ProductAttributeMaster> findByNameAndStatus(String name, byte status);
	
	List<ProductAttributeMaster> findByNameIn(List<String> names);
	
	List<ProductAttributeMaster> findByNameInAndStatus(List<String> names, byte status);
	
	ProductAttributeMaster findByName(String name);

}
