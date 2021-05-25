package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv6Address;

/**
 * This file contains the IpaddrWanv6AddressRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface IpaddrWanv6AddressRepository extends JpaRepository<IpaddrWanv6Address, Integer> {
}
