package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.ProcessPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
	
	@Query(value = "SELECT distinct(msd.name) as stage,count(msd.name) as count,msd.key as stageKey FROM process sp ,stage s, mst_process_def msd, mst_status ms,sc_service_detail sc where  s.service_id=sc.id and sc.is_migrated_order='N' and sc.status in (1,4,5,10,8) and s.id = sp.stage_id and sp.process_def_key=msd.key and  sp.status=ms.id and ms.code='INPROGRESS' GROUP BY msd.name HAVING count >= 1 order by sp.id asc", nativeQuery = true)
	List<Map<String, String>> findInProgressProcessCount();
	
	
	@Query(value = "SELECT * FROM process_plan where service_id=:serviceId and process_def_key=:defkey and site_type=:siteType", nativeQuery = true)
	ProcessPlan findByServiceIdAndMstProcessDefKey(@Param("serviceId") Integer serviceId,@Param("defkey") String defkey, String siteType);
	
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
	
	@Query(value = "SELECT pp.order_code AS orderCode, pp.service_code AS serviceCode, pp.service_id AS serviceId, sp.sc_order_id AS scOrderId, pp.process_Def_key AS processKey, sco.erf_cust_customer_name as customerName, mpd.name FROM stage_plan sp "
			+ "INNER JOIN process_plan pp ON sp.id = pp.stage_plan_id "
			+ "INNER JOIN mst_process_def mpd ON mpd.key = pp.process_def_key "
			+ "INNER JOIN mst_status ms ON pp.status = ms.id "
			+ "INNER JOIN sc_order sco ON sp.sc_order_id=sco.id "
			+ "WHERE sp.stage_def_key =:defkey AND ms.code='INPROGRESS' ORDER BY pp.process_def_key", nativeQuery = true)
	List<Map<String, String>> findInProgressByStageKey(@Param("defkey") String defkey);
	
	@Query(value = "SELECT sp.order_code AS orderCode, sp.service_code AS serviceCode, sp.service_id AS serviceId, sp.sc_order_id AS scOrderId,"
			+ " ap.activity_def_key AS processKey, sco.erf_cust_customer_name as customerName, mad.name FROM stage sp, process pp,activity ap, mst_activity_def mad,"
			+ " mst_status ms,sc_order sco,sc_service_detail sc WHERE sp.service_id=sc.id and sc.is_migrated_order='N' and sc.status in (1,4,5,10,8) and sp.id = pp.stage_id"
			+ " and pp.id = ap.process_id and mad.key = ap.activity_def_key and ap.status = ms.id and sp.sc_order_id=sco.id and pp.process_def_key =:pocessDefkey"
			+ " AND ms.code='INPROGRESS' ORDER BY ap.activity_def_key; ", nativeQuery = true)
	List<Map<String, String>> findInProgressByProcessKey(@Param("pocessDefkey") String pocessDefkey);
	

}
