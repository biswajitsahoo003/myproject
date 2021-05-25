package com.tcl.dias.serviceactivation.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.IpaddrLanv4Address;

/**
 * This file contains the IpaddrLanv4AddressRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface IpaddrLanv4AddressRepository extends JpaRepository<IpaddrLanv4Address, Integer> {

	@Query(value = "select * from ipaddr_lanv4_address where ip_address_details_IP_Address_Details=:ipaddressId", nativeQuery = true)
	List<IpaddrLanv4Address> findByIpAddressDetailId(@Param("ipaddressId") Integer ipaddressId);
}
