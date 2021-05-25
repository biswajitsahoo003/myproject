package com.tcl.dias.audit.entity.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.audit.entity.entities.ThirdPartyServiceAudit;

/**
 * 
 * This file contains the ThirdPartyServiceAuditRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface ThirdPartyServiceAuditRepository extends JpaRepository<ThirdPartyServiceAudit, Integer> {

	@Query("select a from ThirdPartyServiceAudit a where a.createdTime <= :creationDateTime")
	List<ThirdPartyServiceAudit> findAllWithCreatedTimeBefore(@Param("creationDateTime") Date creationDateTime);

}
