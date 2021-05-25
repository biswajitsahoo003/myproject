package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.StagePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 
 * This file contains the StagePlanRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface StagePlanRepository extends JpaRepository<StagePlan, Integer> {

	@Query(value = "SELECT * FROM stage_plan where service_id=:serviceId order by id asc", nativeQuery = true)
	List<StagePlan> findByServiceId(@Param("serviceId") Integer serviceId);

	@Query(value = "SELECT distinct(msd.name) as stage,count(msd.name) as count,msd.key as stageKey FROM stage_plan sp , mst_stage_def msd, mst_status ms where order_code=:scOrderCode and sp.stage_def_key=msd.key and  sp.status=ms.id and ms.code='INPROGRESS' GROUP BY msd.name HAVING count >= 1 order by sp.id asc", nativeQuery = true)
	List<Map<String, String>> findStageCountByOrderCode(@Param("scOrderCode") String scOrderCode);
	
	@Query(value = "SELECT distinct(msd.name) as stage,count(msd.name) as count,msd.key as stageKey FROM stage_plan sp , mst_stage_def msd, mst_status ms where  sp.stage_def_key=msd.key and  sp.status=ms.id and ms.code='INPROGRESS' GROUP BY msd.name HAVING count >= 1 order by sp.id asc", nativeQuery = true)
	List<Map<String, String>> findInProgressStageCount();

	@Query(value = "SELECT count(distinct(id)) as orderCount FROM sc_order where is_active = 'Y' and is_migrated_order='N'", nativeQuery = true)
	Map<String, String> findActiveOrderCount();

	@Query(value = "SELECT * FROM stage_plan where service_id=:serviceId and stage_def_key=:defkey", nativeQuery = true)
	StagePlan findByServiceIdAndMstStageDefKey(@Param("serviceId") Integer serviceId,@Param("defkey") String defkey);
	
	@Query(value = "SELECT * FROM stage_plan where site_id=:siteId and stage_def_key=:defkey", nativeQuery = true)
	StagePlan findBySiteIdAndMstStageDefKey(@Param("siteId") Integer siteId,@Param("defkey") String defkey);

	@Query(value = "SELECT m.sc_order_id as scOrderId, m.service_id as serviceId, m.service_code as serviceCode, n.op_order_code as orderCode FROM stage_plan m, sc_order n where m.status=4 and m.stage_def_key=:defkey and m.sc_order_id=n.id", nativeQuery = true)
	List<Map<String, String>> findInProgressByStageKey(@Param("defkey") String defkey);
	
	
	StagePlan findFirstByServiceIdAndMstStageDefKeyOrderByIdDesc(@Param("serviceId") Integer serviceId,@Param("defkey") String defkey);
	
	List<StagePlan> findBySiteId( Integer serviceId);

	@Query(value = "SELECT * FROM stage_plan where service_id in (:serviceId) and stage_def_key in (:stages)", nativeQuery = true)
	List<StagePlan> findByServiceIdAndMstStageDefKey(@Param("serviceId") List<Integer> serviceId, @Param("stages") List<String> stages);

	@Query(value = "SELECT * FROM stage_plan where service_id in (:serviceId) and stage_def_key in ('order_enrichment_stage', 'experience_survey_stage')", nativeQuery = true)
	List<StagePlan> findByServiceIdAndMstStageDefKey(@Param("serviceId") List<Integer> serviceId);

	

}
