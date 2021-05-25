package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.ImpersonationUserAudit;

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
public interface ImpersonationUserAuditRepository extends JpaRepository<ImpersonationUserAudit, Integer> {

	public ImpersonationUserAudit findBySessionId(String sessionStateId);

}
