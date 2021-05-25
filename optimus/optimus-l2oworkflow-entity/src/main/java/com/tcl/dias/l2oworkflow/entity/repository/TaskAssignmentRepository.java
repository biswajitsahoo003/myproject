package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflow.entity.entities.TaskAssignment;
import java.util.Optional;


/**
 * This file contains the TaskAssignmentRepository.java class.
 * 
 *
 * @author MAYANSHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Integer> {
	
	
	
	List<TaskAssignment> findAll(Specification<TaskAssignment> specification);
	
	List<TaskAssignment> findByTaskOrderByIdDesc(Task task);
	
	 Optional<TaskAssignment> findByTaskId(Integer taskId);



}
