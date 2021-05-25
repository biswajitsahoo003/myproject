package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.TaskTatTime;

@Repository
public interface TaskTatTimeRepository extends JpaRepository<TaskTatTime, Integer> {
	
	TaskTatTime findFirstByTask_idOrderByIdDesc(Integer taskId);
	
	

}
