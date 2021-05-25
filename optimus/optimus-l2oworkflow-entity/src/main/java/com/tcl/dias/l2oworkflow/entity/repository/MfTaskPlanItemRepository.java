package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.l2oworkflow.entity.entities.MfTaskPlanItem;

public interface MfTaskPlanItemRepository extends JpaRepository<MfTaskPlanItem, String> {

	MfTaskPlanItem findByCaseInstIdAndPlanItemDefIdAndStatus(String caseInstId , String planItemDefId, String status);
	Optional<MfTaskPlanItem> findByPlanItemInstId(String planItemInstId);
}
