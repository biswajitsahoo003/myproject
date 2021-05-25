package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.TaskRemark;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRemarkRepository extends JpaRepository<TaskRemark, Integer> {
	
	List<TaskRemark> findByServiceIdOrderByIdDesc(Integer serviceId);


}
