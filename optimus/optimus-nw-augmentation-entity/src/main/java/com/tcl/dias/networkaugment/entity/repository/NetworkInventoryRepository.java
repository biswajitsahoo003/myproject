package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.NetworkInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
    Optional<NetworkInventory> findByOrderCode(String orderCode);

}
