package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

/**
 * 
 * AssigneeRequest class used for the request related to details
 * 
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class AssigneeRequest extends BaseRequest{
	
	private String assigneeNameFrom;
	
	
	private Integer taskId;
	
	private String description;
	
	
	
	
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	private List<Integer> taskIds;
	
	
	private String assigneeNameTo;
	
	private String type;
	
	
	


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
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


	public List<Integer> getTaskIds() {
		return taskIds;
	}


	public void setTaskIds(List<Integer> taskIds) {
		this.taskIds = taskIds;
	}


	
	
	
	

}
