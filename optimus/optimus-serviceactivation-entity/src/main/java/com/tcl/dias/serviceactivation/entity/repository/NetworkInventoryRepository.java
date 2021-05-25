package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;

import java.util.Optional;

/**
 * This file contains the NetworkInventoryRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface NetworkInventoryRepository extends JpaRepository<NetworkInventory, Integer> {

    Optional<NetworkInventory> findByRequestId(String requestid);
    Optional<NetworkInventory> findByRequestIdAndType(String requestid,String type);
}
