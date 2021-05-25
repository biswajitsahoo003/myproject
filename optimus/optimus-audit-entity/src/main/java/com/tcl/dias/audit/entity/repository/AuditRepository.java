package com.tcl.dias.audit.entity.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.audit.entity.entities.Audit;

/**
 * This file contains the OptimusAuditRepository.java class.
 * 
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface AuditRepository extends JpaRepository<Audit, Integer> {

	public Audit findByQuoteCode(String quoteCode);

	@Query("select a from Audit a where a.createdTime <= :creationDateTime")
	List<Audit> findAllWithCreatedTimeBefore(@Param("creationDateTime") Date creationDateTime);

}
