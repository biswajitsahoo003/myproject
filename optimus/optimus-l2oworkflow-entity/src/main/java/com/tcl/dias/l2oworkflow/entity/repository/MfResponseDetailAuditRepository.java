package com.tcl.dias.l2oworkflow.entity.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.MfResponseDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MfResponseDetailAudit;

/**
 * 
 * This file contains the MfResponseDetailAuditRepository.java class.
 * 
 *
 * @author Kruthika S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MfResponseDetailAuditRepository extends JpaRepository<MfResponseDetailAudit, Integer> {
	
	List<MfResponseDetailAudit> findByMfResponseDetail(MfResponseDetail mfResponseDetail);
	
	@Query(value = " SELECT * from mf_response_detail_audit where mf_response_detail_id in (:ids);", nativeQuery = true)
	List<MfResponseDetailAudit> findByMfResponseDetailId(@Param("ids") Integer ids);
	
	


	
	
}
