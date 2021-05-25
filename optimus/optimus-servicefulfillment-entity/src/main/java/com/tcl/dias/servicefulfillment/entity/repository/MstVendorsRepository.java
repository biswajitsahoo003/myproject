package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstVendor;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface MstVendorsRepository extends JpaRepository<MstVendor, Integer> {

	List<MstVendor> findAll(Specification<MstVendor> specification);
	
	List<MstVendor> findByMstStatus_code(String code); 

}
