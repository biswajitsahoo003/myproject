package com.tcl.dias.serviceinventory.entity.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.SIContractInfo;
import com.tcl.dias.serviceinventory.entity.entities.SIOrder;

/**
 * This file contains the SIContractInfoRepository.java class.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface SIContractInfoRepository extends JpaRepository<SIContractInfo, Integer> {

	List<SIContractInfo> findBySiOrder(SIOrder siOrder);
	
	public SIContractInfo findFirstBySiOrder_id(Integer orderId);

}
