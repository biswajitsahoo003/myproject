package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.UtilityAudit;



/**
 * Repository for Utility audit 
 * @author archchan
 *
 */
@Repository
public interface UtilityAuditRepository extends JpaRepository<UtilityAudit, Integer> {

}
