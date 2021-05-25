package com.tcl.dias.serviceactivation.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.InterfaceProtocolMapping;

/**
 * This file contains the InterfaceProtocolMappingRepository.java Repository
 * class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface InterfaceProtocolMappingRepository extends JpaRepository<InterfaceProtocolMapping, Integer> {

	List<InterfaceProtocolMapping> findByServiceDetailIdAndRouterDetailNotNullAndEndDateIsNull(Integer serviceId);
	
	List<InterfaceProtocolMapping> findByServiceDetailIdAndBgpNotNullAndEndDateIsNull(Integer serviceId);
	
	List<InterfaceProtocolMapping> findByServiceDetailIdAndEndDateIsNull(Integer serviceId);
	
	List<InterfaceProtocolMapping> findByServiceDetailIdAndCpeNotNullAndEndDateIsNull(Integer serviceId);
	
	@Query(value = "select bgp_bgp_id from interface_protocol_mapping where interface_protocol_mapping_id =:ipmId", nativeQuery = true)
	Integer findByInterfaceProtocolMappingId(@Param("ipmId") Integer ipmId);
}
