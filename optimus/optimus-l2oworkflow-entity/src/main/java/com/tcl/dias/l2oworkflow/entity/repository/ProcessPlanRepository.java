package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.ProcessPlan;

/**
 * 
 * This file contains the ProcessPlanRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ProcessPlanRepository extends JpaRepository<ProcessPlan, Integer> {

	@Query(value = "SELECT * FROM process_plan  where  stage_plan_id=:stagePlanId order by actual_end_time  is NUll asc, actual_end_time asc, actual_start_time  is NUll asc, actual_start_time asc, planned_start_time asc", nativeQuery = true)
	List<ProcessPlan> findByStagePlanId(@Param("stagePlanId") Integer stagePlanId);

	/**
	 * Find process plan by stage plan id and process def key
	 *
	 * @param stagePlanId
	 * @param processDefKey
	 * @return {@link ProcessPlan}
	 */
	@Query(value = "SELECT * FROM process_plan where stage_plan_id=:stagePlanId and process_def_key=:processDefKey limit 1", nativeQuery = true)
	ProcessPlan findByStagePlanIdAndProcessDefKey(@Param("stagePlanId") Integer stagePlanId, @Param("processDefKey") String processDefKey);
	
	@Query(value = "SELECT * FROM process_plan where service_id=:serviceId and process_def_key=:defkey", nativeQuery = true)
	ProcessPlan findByServiceIdAndMstProcessDefKey(@Param("serviceId") Integer serviceId,@Param("defkey") String defkey);
	
	@Query(value = "SELECT * FROM process_plan where site_id=:siteId and process_def_key=:defkey", nativeQuery = true)
	ProcessPlan findBySiteIdAndMstProcessDefKey(@Param("siteId") Integer siteId,@Param("defkey") String defkey);

	/**
	 * Find process plan by status, order id, service id, service code
	 *
	 * @param orderId
	 * @param serviceId
	 * @param serviceCode
	 * @param status
	 * @return {@link List<ProcessPlan>}
	 */
	@Query(value = "SELECT * FROM process_plan where sc_order_id=:orderId and service_id=:serviceId and service_code=:serviceCode and status=:status", nativeQuery = true)
    List<ProcessPlan> findByStatusInProgress(@Param("orderId") Integer orderId, @Param("serviceId") Integer serviceId, @Param("serviceCode") String serviceCode, @Param("status") Integer status);

	/**
	 * Find process plan by order id, service id, service code
	 *
	 * @param orderId
	 * @param serviceId
	 * @param serviceCode
	 * @return {@link List<ProcessPlan>}
	 */
	@Query(value = "select * from process_plan where sc_order_id=:orderId and service_id=:serviceId and service_code=:serviceCode", nativeQuery = true)
	List<ProcessPlan> findBy(@Param("orderId") Integer orderId, @Param("serviceId") Integer serviceId, @Param("serviceCode") String serviceCode);
	
	
	List<ProcessPlan> findByServiceId(Integer serviceId);

	List<ProcessPlan> findBySiteId(Integer siteId);

}
