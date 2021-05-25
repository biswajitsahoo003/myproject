package com.tcl.dias.l2oworkflow.entity.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.MfDetail;
import com.tcl.dias.l2oworkflow.entity.entities.Task;


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
	
	Page<Task> findAll(Specification<Task> specification,Pageable pageable);
	
	Task findByWfTaskId(String wfTaskId);
	
	Task findByServiceIdAndMstStatus_codeAndMstTaskDef_key(Integer ServiceId,String status,String key);
	
	List<Task> findByServiceIdAndMstTaskDef_key(Integer ServiceId,String key);
	
	List<Task> findBySiteIdAndMstTaskDef_key(Integer siteId,String key);
	
	List<Task> findByServiceIdAndMstStatus_code(Integer ServiceId,String status);

	List<Task> findByServiceIdAndMstStatus_codeIn(Integer ServiceId, List<String> statuses);

	List<Task> findByServiceId(Integer serviceId);
	
	Task findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(Integer ServiceId,String key);
	
	Optional<Task> findFirstBySiteDetail_idAndMstTaskDef_keyOrderByCreatedTimeDesc(Integer siteDetailId,String key);
	
	List<Task> findFirstBySiteDetail_idAndMstStatus_code(Integer site,String status);
	
	List<Task> findBySiteDetail_idAndMstStatus_code(Integer site,String status);
	
	List<Task> findBySiteDetail_id(Integer site);
	
	Optional<Task> findByIdAndMstStatus_codeNotIn(Integer id, String status);
	
	List<Task> findByWfProcessInstId(String wfProcessInstId);
	
	List<Task> findByWfProcessInstIdAndMstTaskDef_key(String wfProcessInstId,String taskDefKey);
	
	List<Task> findByQuoteId(Integer quoteId);
	
	Optional<Task> findById(Integer id);
	
	List <Task> findByQuoteIdAndSiteDetail_id(Integer quoteId, Integer site);
	
	List<Task> findBySiteCode(String siteCode);

	List<Task> findBySiteId(Integer siteId);
	
	List<Task> findByQuoteCode(String quoteCode);

	List<Task> findBySiteCodeAndMstTaskDef_keyIn(String wfProcessInstId,List<String> keys);
	
	Optional<Task> findFirstBySiteDetail_idAndMstTaskDef_keyAndMstStatus_idOrderByCreatedTimeDesc(Integer siteDetailId,String key,Integer status);
	

	/**
	 * Gets all approved task details within the date range
	 *
	 * @param taskCreatedFromDate
	 * @param taskCreatedToDate
	 * @return {@link List}
	 */
	@Query(value = "select t.id as taskId, ta.user_name as taskAssignmentUserName, sd.opportunity_id as opportunityId, sd.quote_code as quoteCode, ms.code as status,\n" +
			"sd.account_name as accountName, ctd.tcv, sd.region as Region, t.created_time as createdDate, t.completed_time as completedDate,\n" +
			"ttt.created_time as tatCreatedtime, ttt.end_time as tatEndtime, t.task_def_key as taskDefKey\n" +
			"from site_detail sd inner join task t on sd.id = t.site_detail_id\n" +
			"inner join task_assignment ta on t.id = ta.task_id\n" +
			"inner join mst_status ms on t.status = ms.id\n" +
			"inner join task_tat_time ttt on t.id = ttt.task_id\n" +
			"inner join commercial_task_details ctd on ctd.task_id = t.id\n" +
			"where t.created_time between :taskCreatedFromDate and :taskCreatedToDate\n" +
			"and t.assignee = ta.user_name and ttt.end_time is not null and ctd.updated_time = ( select max(ctd1.updated_time) from commercial_task_details ctd1 where ctd1.task_id=t.id and ctd1.approver_name = t.assignee)", nativeQuery = true)
	List<Map<String, Object>> findApprovedTaskDetailsWithinDateRange(@Param("taskCreatedFromDate") LocalDate taskCreatedFromDate,
																	 @Param("taskCreatedToDate") LocalDate taskCreatedToDate);

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
			"t.created_time as tatCreatedtime, t.task_def_key as taskDefKey\n" +
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
			"t.created_time as tatCreatedtime, t.task_def_key as taskDefKey\n" +
			"from site_detail sd inner join task t on sd.id = t.site_detail_id\n" +
			"inner join task_assignment ta on t.id = ta.task_id\n" +
			"inner join mst_status ms on t.status = ms.id\n" +
			"where t.created_time between :taskCreatedFromDate and :taskCreatedToDate\n", nativeQuery = true)
	List<Map<String, Object>> findUnapprovedTaskDetailsWithinDateRange(@Param("taskCreatedFromDate") LocalDate taskCreatedFromDate,
																	   @Param("taskCreatedToDate") LocalDate taskCreatedToDate);

	List<Task> findByMfDetail(MfDetail mfDetail);
	
	Optional<Task> findByFeasibilityId(String feasibilityId);

}
