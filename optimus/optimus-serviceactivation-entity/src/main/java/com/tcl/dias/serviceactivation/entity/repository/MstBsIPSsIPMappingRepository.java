package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.MstBsIPSsIPMapping;

/**
 * This file contains the MstBsIPSsIPMappingRepository.java Repository class.
 *
 * @author Dimple
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
@Repository
public interface MstBsIPSsIPMappingRepository extends JpaRepository<MstBsIPSsIPMapping, Integer> {

	MstBsIPSsIPMapping findFirstByBsIp(String bsIp);
}
