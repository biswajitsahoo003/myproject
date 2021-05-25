package com.tcl.dias.l2oworkflow.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.TaskTatTime;

@Repository
public interface TaskTatTimeRepository extends JpaRepository<TaskTatTime, Integer> {
	
	

}
