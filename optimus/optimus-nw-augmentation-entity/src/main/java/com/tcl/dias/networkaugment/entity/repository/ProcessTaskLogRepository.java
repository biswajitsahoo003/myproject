package com.tcl.dias.networkaugment.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.ProcessTaskLog;

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

	@Query(value = "SELECT * FROM process_task_log where group_from=:groupName order by id desc LIMIT 10;", nativeQuery = true)
	List<ProcessTaskLog> findTop10ByGroupFromOrderByCreatedTimeDesc(@Param("groupName") String groupName);
	
	@Query(value = "SELECT * FROM process_task_log where group_from=:groupName and action_from=:userName order by id desc LIMIT 10;", nativeQuery = true)
	List<ProcessTaskLog> findTop10ByGroupFromAndActionFromOrderByCreatedTimeDesc(@Param("groupName") String groupName,@Param("userName") String userName);
	
	List<ProcessTaskLog> findAll(Specification<ProcessTaskLog> tSpecification);
	
	Page<ProcessTaskLog> findAll(Specification<ProcessTaskLog> tSpecification,Pageable pageable);
	
	@Query(value = "SELECT * FROM process_task_log where sc_order_id=:scOrderId   order by id desc LIMIT 5;", nativeQuery = true)
	List<ProcessTaskLog> findByScOrderId(@Param("scOrderId") Integer scOrderId);
	
	
	@Query(value = "SELECT * FROM process_task_log where sc_order_id=:scOrderId and order_code=:orderCode  order by id desc LIMIT 5;", nativeQuery = true)
	List<ProcessTaskLog> findByScOrderIdAndOrderCode(@Param("scOrderId") Integer scOrderId,@Param("orderCode") String orderCode);
	
	

}
