package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.AuditCwbTrailDetails;



/**
 * This file contains the AuditTrailRepository.java class.
 * 
 *
 * @author Santosh.Tidke
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface AuditCwbTrailDetailsRepository extends JpaRepository<AuditCwbTrailDetails, Integer>{

	public abstract List<AuditCwbTrailDetails> findAllByQuoteId(Integer quoteId);
}
