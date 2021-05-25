package com.tcl.dias.serviceactivation.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.MigrationStatus;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;

/**
 * This file contains the MigrationStatusRepository.java Repository class.
 *
 * @author Diksha Garg
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface MigrationStatusRepository extends JpaRepository<MigrationStatus, Integer> {
	List<MigrationStatus> findByServiceCode(String serviceCode);
	List<MigrationStatus> findByServiceCodeOrderByIdDesc(String serviceCode);
	List<MigrationStatus> findByServiceTypeAndResponseContainingOrderByIdDesc(String serviceType, String responseRegex);
	List<MigrationStatus> findByServiceCodeAndResponse(String serviceCode, String response);
}
