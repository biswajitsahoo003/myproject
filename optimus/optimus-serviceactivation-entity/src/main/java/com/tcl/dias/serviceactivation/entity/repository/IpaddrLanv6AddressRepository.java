package com.tcl.dias.serviceactivation.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv6Address;

/**
 * This file contains the IpaddrLanv6AddressRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface IpaddrLanv6AddressRepository extends JpaRepository<IpaddrLanv6Address, Integer> {
	
	@Query(value = "select * from ipaddr_lanv4_address where ip_address_details_IP_Address_Details=:ipaddressId", nativeQuery = true)
	List<IpaddrLanv6Address> findByIpAddressDetailId(@Param("ipaddressId") Integer ipaddressId);
}
