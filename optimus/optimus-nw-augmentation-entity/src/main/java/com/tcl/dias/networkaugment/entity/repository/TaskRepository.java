package com.tcl.dias.networkaugment.entity.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.Task;


/**
 * This file contains the TaskRepository.java class.
 * 
 *
 * @author MAYANSHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
	
	
	List<Task> findAll(Specification<Task> specification);
	
	Task findByWfTaskId(String wfTaskId);
	
	Task findByIdAndWfTaskId(Integer taskId,String wfTaskId);

	Task findByServiceIdAndMstStatus_codeAndMstTaskDef_key(Integer ServiceId,String status,String key);

	Optional<Task> findFirstBySiteDetail_idAndMstTaskDef_keyOrderByCreatedTimeDesc(Integer siteDetailId, String key);
	
	List<Task> findByServiceIdAndMstTaskDef_key(Integer ServiceId,String key);
	
	@Query(value = "SELECT * FROM task where service_id=:serviceId and task_def_key=:defkey and site_type=:siteType", nativeQuery = true)
	List<Task> findByServiceIdAndMstTaskDef_keyAndSiteType(@Param("serviceId") Integer ServiceId,@Param("defkey")String defkey,@Param("siteType")String siteType);
	
	List<Task> findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeIn(Integer ServiceId,List<String> key,List<String> status);

	
	List<Task> findByServiceIdAndMstStatus_code(Integer ServiceId,String status);

	List<Task> findByServiceIdAndMstStatus_codeIn(Integer ServiceId, List<String> statuses);

	List<Task> findByServiceId(Integer serviceId);
	
	List<Task> findByOrderCodeAndServiceCodeAndServiceId(String orderCode,String serviceCode,Integer serviceId);

	@Query(value = "select id from task where order_code=:orderCode limit 1",nativeQuery=true)
	Task findByOrderCode(String orderCode);

	@Query(value="select so.id from sc_order so join sc_service_detail sc on sc.sc_order_id = so.id join task t on t.service_id = sc.id where t.id =:taskId",nativeQuery = true)
	Integer findOrderIdByTaskId(Integer taskId);
	
	Task findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(Integer ServiceId,String key);
	
	Task findFirstByServiceIdAndMstTaskDef_keyAndSiteTypeOrderByCreatedTimeDesc(Integer ServiceId,String key,String stiteType);


	List<Task> findByMstTaskDef_key(String taskDefKey);

	@Query(value = "select distinct(service_id) from task where task_def_key='product-commissioning-jeopardy' and status in (2) and " +
			"service_id in (:serviceIds)",nativeQuery=true)
	List<Integer> findBilledCircuits(@Param("serviceIds") List<Integer> serviceIds);

	List<Task> findByIdIn(List<Integer> taskIds);

	Task findByScOrderId(Integer scOrderId);

	/**
	 * Gets all unapproved task details within date range
	 *
	 * @param taskCreatedFromDate
	 * @param taskCreatedToDate
	 * @param approvedTaskIds
	 * @return {@link List}
	 */
	@Query(value = "select t.id as taskId, ta.user_name as taskAssignmentUserName, sd.opportunity_id as opportunityId, sd.quote_code as quoteCode, ms.code as status,\n" +
			"sd.account_name as accountName, sd.region as Region, t.created_time as createdDate, t.completed_time as completedDate,\n" +
			"t.created_time as tatCreatedtime\n" +
			"from site_detail sd inner join task t on sd.id = t.site_detail_id\n" +
			"inner join task_assignment ta on t.id = ta.task_id\n" +
			"inner join mst_status ms on t.status = ms.id\n" +
			"where t.created_time between :taskCreatedFromDate and :taskCreatedToDate\n" +
			"and t.id not in (:approvedTaskIds)", nativeQuery = true)
	List<Map<String, Object>> findUnapprovedTaskDetailsWithinDateRange(@Param("taskCreatedFromDate") LocalDate taskCreatedFromDate,
																	   @Param("taskCreatedToDate") LocalDate taskCreatedToDate,
																	   @Param("approvedTaskIds") List<Integer> approvedTaskIds);

	/**
	 * Get all tasks when there is no approved id
	 *
	 * @param taskCreatedFromDate
	 * @param taskCreatedToDate
	 * @return
	 */
	@Query(value = "select t.id as taskId, ta.user_name as taskAssignmentUserName, sd.opportunity_id as opportunityId, sd.quote_code as quoteCode, ms.code as status,\n" +
			"sd.account_name as accountName, sd.region as Region, t.created_time as createdDate, t.completed_time as completedDate,\n" +
			"t.created_time as tatCreatedtime\n" +
			"from site_detail sd inner join task t on sd.id = t.site_detail_id\n" +
			"inner join task_assignment ta on t.id = ta.task_id\n" +
			"inner join mst_status ms on t.status = ms.id\n" +
			"where t.created_time between :taskCreatedFromDate and :taskCreatedToDate\n", nativeQuery = true)
	List<Map<String, Object>> findUnapprovedTaskDetailsWithinDateRange(@Param("taskCreatedFromDate") LocalDate taskCreatedFromDate,
																	   @Param("taskCreatedToDate") LocalDate taskCreatedToDate);

	/**
	 * Gets all approved task details within the date range
	 *
	 * @param taskCreatedFromDate
	 * @param taskCreatedToDate
	 * @return {@link List}
	 */
	@Query(value = "select t.id as taskId, ta.user_name as taskAssignmentUserName, sd.opportunity_id as opportunityId, sd.quote_code as quoteCode, ms.code as status,\n" +
			"sd.account_name as accountName, ctd.tcv, sd.region as Region, t.created_time as createdDate, t.completed_time as completedDate,\n" +
			"ttt.created_time as tatCreatedtime, ttt.end_time as tatEndtime\n" +
			"from site_detail sd inner join task t on sd.id = t.site_detail_id\n" +
			"inner join task_assignment ta on t.id = ta.task_id\n" +
			"inner join mst_status ms on t.status = ms.id\n" +
			"inner join task_tat_time ttt on t.id = ttt.task_id\n" +
			"inner join commercial_task_details ctd on ctd.task_id = t.id\n" +
			"where t.created_time between :taskCreatedFromDate and :taskCreatedToDate\n" +
			"and t.assignee = ta.user_name and ttt.end_time is not null and ctd.updated_time = ( select max(ctd1.updated_time) from commercial_task_details ctd1 where ctd1.task_id=t.id and ctd1.approver_name = t.assignee)", nativeQuery = true)
	List<Map<String, Object>> findApprovedTaskDetailsWithinDateRange(@Param("taskCreatedFromDate") LocalDate taskCreatedFromDate,
																	 @Param("taskCreatedToDate") LocalDate taskCreatedToDate);


	//@Query(value = "select id,task_def_key from Task t where order_code = :orderCode and id < :taskId and status = 2 ", nativeQuery = true )
	List<Task>  findByOrderCodeAndIdLessThan(String orderCode,Integer taskId);

	@Query(value = "SELECT t.* FROM Task t WHERE t.order_code :orderCode AND t.id > :taskId AND t.id < (SELECT id FROM Task WHERE order_code :orderCode AND status = 15)", nativeQuery = true)
	List<Task> findTasksToList(String orderCode, Integer taskId);

}
