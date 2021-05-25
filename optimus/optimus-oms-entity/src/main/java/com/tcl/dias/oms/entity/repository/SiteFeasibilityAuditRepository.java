package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.SiteFeasibilityAudit;

/**
 * 
 * Repository class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface SiteFeasibilityAuditRepository extends JpaRepository<SiteFeasibilityAudit, Integer> {

	List<SiteFeasibilityAudit> findBySiteFeasibility(SiteFeasibility siteFeasibility);

}
