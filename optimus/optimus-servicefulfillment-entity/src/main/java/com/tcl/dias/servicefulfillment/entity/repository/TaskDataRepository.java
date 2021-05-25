package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.TaskData;

import java.util.List;

/**
 * This file contains the TaskDataRepository.java class.
 * 
 *
 * @author MAYANSHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface TaskDataRepository extends JpaRepository<TaskData, Integer> {
	
	TaskData findFirstByTask_idOrderByCreatedTimeDesc(Integer id);

	List<TaskData> findByTaskId(Integer taskId);
	
	@Query(value= "SELECT a.* FROM task_data a,task t where a.name ='conduct-lm-test-onnet-wireline_output' and a.task_id = t.id ", nativeQuery = true)
    List<TaskData> findTaskDataForConductLmTest();
	
}
