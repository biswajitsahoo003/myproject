package com.tcl.dias.l2oworkflowutils.beans;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * used to map task assignee
 *
 */
public class TaskAssignmentBean {

	private String group;

	private String owner;

	private String status;

	private String userName;

	private ProcessBean process;

	private TaskBean task;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
