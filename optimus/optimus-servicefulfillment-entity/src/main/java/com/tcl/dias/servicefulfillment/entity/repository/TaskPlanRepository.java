package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.TaskPlan;

/**
 * This file contains the TaskPlanRepository.java class.
 *
 * @author prasath
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface TaskPlanRepository extends JpaRepository<TaskPlan, Integer> {

    /**
     * Find task plan by activity plan id and task def key
     *
     * @param activityPlanId
     * @param mstTaskDefKey
     * @return {@link TaskPlan}
     */
    @Query(value = "SELECT * FROM task_plan where activity_plan_id=:activityPlanId and task_def_key=:mstTaskDefKey limit 1", nativeQuery = true)
    TaskPlan findByActivityPlanIdAndTaskDefKey(@Param("activityPlanId") Integer activityPlanId, @Param("mstTaskDefKey") String mstTaskDefKey);
    
    
	@Query(value = "SELECT * FROM task_plan where service_id=:serviceId and task_def_key=:defkey and site_type=:siteType", nativeQuery = true)
    TaskPlan findByServiceIdAndMstTaskDefKey(@Param("serviceId") Integer serviceId,@Param("defkey") String defkey, String siteType);
	
	@Query(value = "SELECT * FROM task_plan where service_id in(:serviceIds) and task_def_key in(:defkeys) and site_type=:siteType", nativeQuery = true)
    List<TaskPlan> findByServiceIdInAndMstTaskDefKey(@Param("serviceIds") List<Integer> serviceIds,@Param("defkeys") List<String> defkeys, String siteType);
	
    
	@Query(value = "SELECT * FROM task_plan where site_id=:siteId and task_def_key=:defkey", nativeQuery = true)
    TaskPlan findBySiteIdAndMstTaskDefKey(@Param("siteId") Integer siteId,@Param("defkey") String defkey);

    @Query(value = "SELECT * FROM task_plan where activity_plan_id=:activityPlanId", nativeQuery = true)
    List<TaskPlan> findByActivityPlanId(@Param("activityPlanId") Integer activityPlanId);
    
    List<TaskPlan> findByServiceId( Integer serviceId);
    
	@Query(value = "SELECT * FROM task_plan where  activity_plan_id=:activityPlanId order by actual_end_time  is NUll asc, actual_end_time asc, actual_start_time  is NUll asc, actual_start_time asc, planned_start_time asc", nativeQuery = true)
	List<TaskPlan> findByActivityPlanIdSort(@Param("activityPlanId") Integer activityPlanId);


	List<TaskPlan> findBySiteId(Integer siteId);


}
