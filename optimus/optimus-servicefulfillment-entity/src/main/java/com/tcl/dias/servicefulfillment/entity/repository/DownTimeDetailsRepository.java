package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.DownTimeDetails;

/**
 * This file contains the DownTimeDetailsRepository.java class.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface DownTimeDetailsRepository extends JpaRepository<DownTimeDetails, Integer> {

	public DownTimeDetails findFirstBySolutionIdAndScServiceDetailIdOrderByIdDesc(Integer solutionId,Integer serviceId);
	
	public DownTimeDetails findFirstByOrderCodeAndScServiceDetailIdOrderByIdDesc(String orderCode,Integer serviceId);
	
	public List<DownTimeDetails> findByOrderCodeAndSolutionIdAndScServiceDetailIdInAndIsCpeAlreadyManagedAndProductNameInOrderByIdDesc(String orderCode,Integer solutionId,List<Integer> serviceIds,String isCpeManagedAlready,List<String> productNames);
}
