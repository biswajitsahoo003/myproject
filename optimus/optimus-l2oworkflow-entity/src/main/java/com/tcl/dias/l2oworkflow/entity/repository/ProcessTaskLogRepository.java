package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.ProcessTaskLog;
import com.tcl.dias.l2oworkflow.entity.entities.Task;

/**
 * This file contains the ProcessTaskLogRepository.java class.
 * 
 *
 * @author MAYANSHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface ProcessTaskLogRepository extends JpaRepository<ProcessTaskLog, Integer> {

	List<ProcessTaskLog> findTop10ByGroupFromOrderByCreatedTimeDesc(String groupName);
	
	Page<ProcessTaskLog> findAll(Specification<ProcessTaskLog> tSpecification,Pageable sortedByPriceDescNameAsc);

	@Query(value = "SELECT * FROM process_task_log where sc_order_id=:scOrderId   order by id desc LIMIT 5;", nativeQuery = true)
	List<ProcessTaskLog> findByScOrderId(@Param("scOrderId") Integer scOrderId);

	@Query(value = " SELECT c.feasibility_id as feasibilityId, b.task_id as taskId,b.action as action,b.action_from as actionFrom,\r\n" + 
			"   b.action_to as actionTo,b.group_from as groupFrom,b.group_to as groupTo,b.created_time as createdTime,\r\n" + 
			"   b.quote_code as quoteCode,b.quote_id as quoteId,a.requestor_comments as requestorComment,\r\n" + 
			"   a.responder_comments as responderComment, a.subject as subject,a.status as status, b.active as active \r\n" + 
			"   FROM process_task_log b inner join mf_task_detail a inner join task c on b.task_id = a.task_id and b.task_id = c.id "
			+ "where b.task_id in (:taskIds);", nativeQuery = true)
	List<Map<String,Object>> findByTaskIds(@Param("taskIds") List<Integer> taskIds);
	
	List<ProcessTaskLog> findByTask(Task task );
	
}
