package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstOmsAttribute;

/**
 * 
 *entity for mstAttribute related queries
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface MstOmsAttributeRepository extends JpaRepository<MstOmsAttribute, Integer> {
	
	
	List<MstOmsAttribute> findByNameAndIsActive(String name,Byte  status);
	
	List<MstOmsAttribute> findByNameInAndIsActive(List<String> name,Byte status);


}
