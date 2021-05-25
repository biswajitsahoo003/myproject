package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrScheduleDetailsAudit;

/**
 * Repository class of odr_schedule_Details entity
 * @author archchan
 *
 */
@Repository
public interface OdrScheduleDetailsAuditRepository extends JpaRepository<OdrScheduleDetailsAudit, Integer>{

	List<OdrScheduleDetailsAudit> findByOrderCodeOrderByIdAsc(String orderCode);
	
}
