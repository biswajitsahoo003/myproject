package com.tcl.dias.networkaugment.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcl.dias.networkaugment.entity.entities.ScContractInfo;

/**
 * 
 * This file contains the ScContractInfoRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface ScContractInfoRepository extends JpaRepository<ScContractInfo, Integer> {

	public List<ScContractInfo> findByScOrder_id(Integer orderId);
	
	public ScContractInfo findFirstByScOrder_id(Integer orderId);
	
	@Query(value = "SELECT * FROM sc_contract_info where sc_order_id= :scOrderId", nativeQuery = true)
	Map<String, Object> findByScOrderId(@Param("scOrderId") Integer scOrderId);

}
