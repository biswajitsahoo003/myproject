package com.tcl.dias.networkaugment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.MstTaskDef;

import java.util.List;

/**
 * This file contains the MstTaskDefRepository.java class.
 * 
 *
 * @author MAYANSHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface MstTaskDefRepository extends JpaRepository<MstTaskDef, Integer> {

	MstTaskDef findByKey(String key);

	@Query(value = "SELECT * FROM mst_task_def where activity_def_key=:activityKey and `key`=:taskKey limit 1", nativeQuery = true)
	MstTaskDef findByActivityKeyAndTaskKey(@Param("activityKey") String activityKey, @Param("taskKey") String taskKey);

	List<MstTaskDef> findByAssignedGroupIn(List<String> assignedGroup);

	MstTaskDef findByOwnerGroupAndKeyStartsWith(String group,String key);

}
