package com.tcl.dias.l2oworkflowutils.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * AssigneeResponse class used to get the Assignee details
 * 
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class AssigneeResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1556125181609818916L;
	private String status;
	
	private Integer id;
	

	private String name;

	private String assigneeNameFrom;

	private Integer taskId;

	private String assigneeNameTo;

	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAssigneeNameFrom() {
		return assigneeNameFrom;
	}

	public void setAssigneeNameFrom(String assigneeNameFrom) {
		this.assigneeNameFrom = assigneeNameFrom;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getAssigneeNameTo() {
		return assigneeNameTo;
	}

	public void setAssigneeNameTo(String assigneeNameTo) {
		this.assigneeNameTo = assigneeNameTo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "AssigneeResponse [status=" + status + ", id=" + id + ", name=" + name + ", assigneeNameFrom="
				+ assigneeNameFrom + ", taskId=" + taskId + ", assigneeNameTo=" + assigneeNameTo + "]";
	}
	
	

}
