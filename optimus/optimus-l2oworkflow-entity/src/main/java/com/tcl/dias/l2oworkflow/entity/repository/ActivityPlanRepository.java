package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.ActivityPlan;

/**
 * 
 * Repository. This file holds repository methods of ActivityPlanRepository
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ActivityPlanRepository extends JpaRepository<ActivityPlan, Integer> {

	@Query(value = "SELECT * FROM activity_plan where  process_plan_id=:processPlanId order by actual_end_time  is NUll asc, actual_end_time asc, actual_start_time  is NUll asc, actual_start_time asc, planned_start_time asc", nativeQuery = true)
	List<ActivityPlan> findByProcessPlanId(@Param("processPlanId") Integer processPlanId);

	/**
	 * Find Activity plan by process plan id and activity def key
	 *
	 * @param processPlanId
	 * @param activityDefKey
	 * @return {@link ActivityPlan >}
	 */
	@Query(value = "SELECT * FROM activity_plan where process_plan_id=:processPlanId and activity_def_key=:activityDefKey limit 1", nativeQuery = true)
    ActivityPlan findByProcessPlanIdAndActivityDefKey(@Param("processPlanId") Integer processPlanId, @Param("activityDefKey") String activityDefKey);
	
	@Query(value = "SELECT * FROM activity_plan where service_id=:serviceId and activity_def_key=:defkey", nativeQuery = true)
    ActivityPlan findByServiceIdAndMstActivityDefKey(@Param("serviceId") Integer serviceId,@Param("defkey") String defkey);
	
	@Query(value = "SELECT * FROM activity_plan where site_id=:siteId and activity_def_key=:defkey", nativeQuery = true)
    ActivityPlan findBySiteIdAndMstActivityDefKey(@Param("siteId") Integer siteId,@Param("defkey") String defkey);
	
	List<ActivityPlan> findByServiceId( Integer serviceId);

	List<ActivityPlan> findBySiteId(Integer siteId);


}
