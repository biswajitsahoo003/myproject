package com.tcl.dias.l2oworkflowutils.beans;

import java.sql.Timestamp;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used to map the process task log
 */
public class ProcessTaskLogBean {

	private String action;

	private String actionFrom;

	private String actionTo;

	private Timestamp createdTime;


	private String orderCode;

	private Integer scOrderId;

	private Integer serviceId;
	private String serviceCode;


	private String active;

	private String description;

	private String groupFrom;

	private String groupTo;
	
	private ProcessBean process;
	
	private String errorMessage;
	
	
	
	
	

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private TaskBean task;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActionFrom() {
		return actionFrom;
	}

	public void setActionFrom(String actionFrom) {
		this.actionFrom = actionFrom;
	}

	public String getActionTo() {
		return actionTo;
	}

	public void setActionTo(String actionTo) {
		this.actionTo = actionTo;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getScOrderId() {
		return scOrderId;
	}

	public void setScOrderId(Integer scOrderId) {
		this.scOrderId = scOrderId;
	}


	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGroupFrom() {
		return groupFrom;
	}

	public void setGroupFrom(String groupFrom) {
		this.groupFrom = groupFrom;
	}

	public String getGroupTo() {
		return groupTo;
	}

	public void setGroupTo(String groupTo) {
		this.groupTo = groupTo;
	}

	public ProcessBean getProcess() {
		return process;
	}

	public void setProcess(ProcessBean process) {
		this.process = process;
	}

	public TaskBean getTask() {
		return task;
	}

	public void setTask(TaskBean task) {
		this.task = task;
	}
	
	

}
