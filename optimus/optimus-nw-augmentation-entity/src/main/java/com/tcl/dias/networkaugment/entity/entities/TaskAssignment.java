package com.tcl.dias.networkaugment.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * This file contains the TaskAssignment.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "task_assignment")
@NamedQuery(name = "TaskAssignment.findAll", query = "SELECT t FROM TaskAssignment t")
public class TaskAssignment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "group_name")
	private String groupName;

	private String owner;

	@Column(name = "process_id")
	private Integer processId;

	@Column(name = "user_name")
	private String userName;

	//bi-directional many-to-one association to Task
	@ManyToOne(fetch=FetchType.LAZY)
	private Task task;

	public TaskAssignment() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Integer getProcessId() {
		return this.processId;
	}

	public void setProcessId(Integer processId) {
		this.processId = processId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Task getTask() {
		return this.task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}