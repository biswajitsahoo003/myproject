package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.l2oworkflow.entity.entities.MfTaskDetail;

public interface MfTaskDetailRepository extends JpaRepository<MfTaskDetail, Integer> {
	
	public List<MfTaskDetail> findBySiteIdAndAssignedFromOrderByIdDesc(Integer siteId, String assignedFrom);
	
	public MfTaskDetail findBySiteIdAndTaskIdAndSubject(Integer siteId, Integer taskId, String subject);
	
	public MfTaskDetail findBySiteIdAndTaskId(Integer siteId, Integer taskId);

	public MfTaskDetail findByTaskId(Integer taskId);
	
	List<MfTaskDetail> findByQuoteId(Integer quoteId);
	
	public List<MfTaskDetail> findBySiteIdAndRequestorTaskId(Integer siteId, Integer taskId);
	
	public List<MfTaskDetail> findBySiteIdAndRequestorTaskIdOrderByIdDesc(Integer siteId, Integer taskId);

	public List<MfTaskDetail> findByRequestorTaskId(Integer taskId);

	public MfTaskDetail findByRequestorTaskIdAndSubjectAndAssignedGroupAndStatus(Integer taskId, String subject,
			String assignedTo, String status);

	public MfTaskDetail findByTaskIdAndSubjectAndAssignedGroupAndStatus(Integer taskId, String subject,
			String assignedTo, String string);

	public List<MfTaskDetail> findByRequestorTaskIdAndAssignedToStartsWithAndStatusIn(Integer taskId, String string,
			List<String> asList);
	
	Integer findRequestorTaskIdByTaskId(Integer takId);

}
