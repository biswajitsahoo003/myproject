package com.tcl.dias.servicefulfillment.entity.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.Task;


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
	
	List<Task> findByServiceIdAndMstTaskDef_key(Integer ServiceId,String key);
	
	List<Task> findByServiceIdAndMstTaskDef_keyAndMstStatus_codeNot(Integer ServiceId,String key,String status);
	
	List<Task> findByServiceIdAndMstTaskDef_keyInAndMstStatus_code(Integer ServiceId,List<String> key,String status);
	
	@Query(value = "SELECT * FROM task where service_id=:serviceId and task_def_key=:defkey and site_type=:siteType", nativeQuery = true)
	List<Task> findByServiceIdAndMstTaskDef_keyAndSiteType(@Param("serviceId") Integer ServiceId,@Param("defkey")String defkey,@Param("siteType")String siteType);
	
	@Query(value = "SELECT * FROM task where service_id=:serviceId and task_def_key=:defkey and site_type=:siteType and wf_process_inst_id=:processInstanceId", nativeQuery = true)
	List<Task> findByServiceIdAndMstTaskDef_keyAndSiteTypeAndProcessInstanceId(@Param("serviceId") Integer ServiceId,@Param("defkey")String defkey,@Param("siteType")String siteType,@Param("processInstanceId")String processInstanceId);
	
	List<Task> findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeIn(Integer ServiceId,List<String> key,List<String> status);

	
	List<Task> findByServiceIdAndMstStatus_code(Integer ServiceId,String status);

	List<Task> findByServiceIdAndMstStatus_codeIn(Integer ServiceId, List<String> statuses);
	
	List<Task> findByServiceIdAndMstStatus_codeNotIn(Integer ServiceId, List<String> statuses);

	List<Task> findByServiceId(Integer serviceId);
	
	List<Task> findByOrderCodeAndServiceCodeAndServiceId(String orderCode,String serviceCode,Integer serviceId);

	
	Task findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(Integer ServiceId,String key);
	
	Task findFirstByServiceIdAndMstTaskDef_keyAndSiteTypeOrderByCreatedTimeDesc(Integer ServiceId,String key,String statusType);
	
	Task findFirstByServiceIdAndMstTaskDef_keyAndSiteTypeAndMstStatus_codeOrderByCreatedTimeDesc(Integer ServiceId,String key,String statusType,String status);
	
	List<Task> findByServiceIdInAndMstTaskDef_keyAndSiteTypeAndMstStatus_codeOrderByCreatedTimeDesc(List<Integer> serviceIds,String key,String statusType,String status);
	
	Task findFirstByServiceIdAndOrderCodeAndMstTaskDef_keyAndMstStatus_codeOrderByCreatedTimeDesc(Integer serviceId,String orderCode,String key,String status);
	
	Task findFirstByServiceIdAndMstTaskDef_keyAndMstStatus_codeNotOrderByCreatedTimeDesc(Integer serviceId,String key, String status);


	Task findFirstByServiceIdInAndOrderCodeAndMstTaskDef_keyAndMstStatus_codeOrderByCreatedTimeDesc(List<Integer> serviceIds,String orderCode,String key,String status);
	
	Task findFirstByServiceIdInAndOrderCodeAndMstTaskDef_keyOrderByCreatedTimeDesc(List<Integer> serviceIds,String orderCode,String key);

	List<Task> findByMstTaskDef_key(String taskDefKey);

	@Query(value = "select distinct(service_id) from task where task_def_key='product-commissioning-jeopardy' and status in (2) and " +
			"service_id in (:serviceIds)",nativeQuery=true)
	List<Integer> findBilledCircuits(@Param("serviceIds") List<Integer> serviceIds);

	

	List<Task> findByIdIn(List<Integer> taskIds);

	List<Task> findByServiceIdAndMstTaskDef_keyIn(Integer ServiceId,List<String> keyL);

	List<Task> findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeNot(Integer serviceId, List<String> ipTxList,String status);
	
	Task findFirstByOrderCodeAndServiceIdAndServiceCodeOrderByIdDesc(String orderCode, Integer serviceId, String serviceCode);

	List<Task> findByScOrderId(Integer scOrderId);
	
	Task findFirstByServiceIdAndMstTaskDef_keyAndGscFlowGroupId(Integer serviceId, String key,Integer flowGroupId);	

	Task findFirstByServiceIdAndMstTaskDef_keyAndScComponentId(Integer serviceId, String key,Integer scComponentId);
}
