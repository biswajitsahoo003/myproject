package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstTaskAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskDef;

/**
 * 
 * This file contains the MstTaskAttributeRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstTaskAttributeRepository extends JpaRepository<MstTaskAttribute, Integer> {

	@Query(value = "SELECT * FROM mst_task_attributes", nativeQuery = true)
	public List<Map<String, Object>> findAllTaskAttributes();

	public MstTaskAttribute findByMstTaskDef_keyAndNodeName( String taskDefKey, String nodeName);
	
	List<MstTaskAttribute> findByMstTaskDef(MstTaskDef mstTaskDef);

}
