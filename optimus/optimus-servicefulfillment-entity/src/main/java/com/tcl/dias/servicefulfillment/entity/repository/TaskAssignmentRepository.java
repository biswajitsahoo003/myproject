package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskAssignment;


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
