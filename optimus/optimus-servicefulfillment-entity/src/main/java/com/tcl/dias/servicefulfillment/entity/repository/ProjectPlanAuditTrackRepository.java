/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ProjectPlanAuditTrack;

/**
 * @author vivek
 *
 */
@Repository
public interface ProjectPlanAuditTrackRepository extends JpaRepository<ProjectPlanAuditTrack, Integer> {

	ProjectPlanAuditTrack findByStatusAndServiceCode(String status, String serviceCode);
	
	ProjectPlanAuditTrack findByServiceId(Integer serviceId);


}
