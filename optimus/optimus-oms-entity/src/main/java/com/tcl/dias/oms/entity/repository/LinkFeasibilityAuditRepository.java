package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.LinkFeasibilityAudit;


/**
 * Repository class for LinkFeasibilityAudit entity
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface LinkFeasibilityAuditRepository  extends JpaRepository<LinkFeasibilityAudit, Integer> {
	
	List<LinkFeasibilityAudit> findByLinkFeasibility(LinkFeasibility linkFeasibility); 

}
