package com.tcl.dias.l2oworkflow.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.TaskData;

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
	

	
}
