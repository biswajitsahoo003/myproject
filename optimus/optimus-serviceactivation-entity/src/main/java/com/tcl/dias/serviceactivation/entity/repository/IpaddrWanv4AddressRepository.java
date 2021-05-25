package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.IpaddrWanv4Address;

/**
 * This file contains the IpaddrWanv4AddressRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface IpaddrWanv4AddressRepository extends JpaRepository<IpaddrWanv4Address, Integer> {
}
