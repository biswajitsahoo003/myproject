package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.PmAssignment;

@Repository
public interface PmAssignmentRepository extends JpaRepository<PmAssignment, Integer>,
		PagingAndSortingRepository<PmAssignment, Integer>, JpaSpecificationExecutor<PmAssignment> {

	public PmAssignment findFirstByCuidOrderByIdDesc(String cuid);

}
