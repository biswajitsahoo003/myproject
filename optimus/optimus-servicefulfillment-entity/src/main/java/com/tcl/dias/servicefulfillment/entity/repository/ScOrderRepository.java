package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;

/**
 * 
 * This file contains the ScOrderRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ScOrderRepository extends JpaRepository<ScOrder, Integer> {

    @Query(value = "SELECT * FROM sc_order where id= :scOrderId and is_active='Y'", nativeQuery = true)
    Map<String, Object> findByScOrderId(@Param("scOrderId") Integer scOrderId);
   
    @Query(value = "SELECT * FROM sc_order where op_order_code= :opOrderCode and is_active='Y'", nativeQuery = true)
    Map<String, Object> findByOrderCode(@Param("opOrderCode") String opOrderCode);
   

	
	ScOrder findByOpOrderCodeAndIsActive( String opOrderCode, String isActive);
	
	ScOrder findByOpOrderCodeAndIsActiveAndIsMigratedOrder( String opOrderCode, String isActive, String isMigratedOrder);
	
	List<ScOrder> findByOpOrderCodeInAndIsActive(List<String> opOrderCode, String isActive);

	/**
	 * return record grouped by orderCode.
	 *
	 * @param opOrderCode
	 * @return {@link ScOrder}
	 */
	@Query(value = "SELECT * FROM sc_order where op_order_code=:opOrderCode and is_active='Y' group by op_order_code",nativeQuery = true)
    ScOrder findDistinctByOpOrderCode(String opOrderCode);

	List<ScOrder> findAllByErfCustLeNameAndIsActive(String customerName, String isActive);

	List<ScOrder> findAllByTpsSfdcCuidAndIsActive(String cuId, String isActive);

	/**
	 * returns list of distinct customer legal names.
	 * @param customerLeName
	 * @return
	 */
	@Query(value = "SELECT distinct(erf_cust_le_name) FROM sc_order where erf_cust_le_name like (%:customerLeName%) and is_active='Y'" , nativeQuery = true)
    List<String> findAllDistinctCustomerLeName(String customerLeName);

	List<ScOrder> findAllByOpOrderCodeAndIsActive(String opOrderCode, String isActive);
	
}
