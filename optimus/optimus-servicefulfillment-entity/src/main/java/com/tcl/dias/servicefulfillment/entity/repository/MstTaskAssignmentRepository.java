package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstTaskAssignment;


@Repository
public interface MstTaskAssignmentRepository extends JpaRepository<MstTaskAssignment, Integer> {

	public Optional<MstTaskAssignment> findFirstByAssignedUserAndMstTaskDef_key(String assignedUser, String key);

}
