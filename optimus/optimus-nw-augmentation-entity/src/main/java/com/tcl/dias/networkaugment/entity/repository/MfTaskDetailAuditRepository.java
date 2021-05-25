package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.MfTaskDetailAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MfTaskDetailAuditRepository extends JpaRepository<MfTaskDetailAudit, Integer>{

	List<MfTaskDetailAudit> findByTaskId(Integer taskId);
}
