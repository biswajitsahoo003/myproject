package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.MfTaskDetailAudit;

@Repository
public interface MfTaskDetailAuditRepository extends JpaRepository<MfTaskDetailAudit, Integer>{

	List<MfTaskDetailAudit> findByTaskId(Integer taskId);
}
