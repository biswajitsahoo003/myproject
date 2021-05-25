package com.tcl.dias.l2oworkflow.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * This file contains the Activity.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name="activity")
@NamedQuery(name = "Activity.findAll", query = "SELECT a FROM Activity a")
public class Activity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "completed_time")
	private Timestamp completedTime;

	@Column(name = "created_time")
	private Timestamp createdTime;

	private Timestamp duedate;

	@Column(name = "updated_time")
	private Timestamp updatedTime;

	@Column(name = "wf_activity_id")
	private String wfActivityId;

	//bi-directional many-to-one association to MstActivityDef
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "activity_def_key")
	private MstActivityDef mstActivityDef;

	//bi-directional many-to-one association to MstStatus
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "status")
	private MstStatus mstStatus;

	//bi-directional many-to-one association to Process
	@ManyToOne(fetch=FetchType.LAZY)
	private Process process;

	//bi-directional many-to-one association to ActivityPlan
	@OneToMany(mappedBy = "activity")
	private Set<ActivityPlan> activityPlans;

	//bi-directional many-to-one association to Task
	@OneToMany(mappedBy = "activity")
	private Set<Task> tasks;

	public Activity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCompletedTime() {
		return this.completedTime;
	}

	public void setCompletedTime(Timestamp completedTime) {
		this.completedTime = completedTime;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getDuedate() {
		return this.duedate;
	}

	public void setDuedate(Timestamp duedate) {
		this.duedate = duedate;
	}

	public Timestamp getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getWfActivityId() {
		return this.wfActivityId;
	}

	public void setWfActivityId(String wfActivityId) {
		this.wfActivityId = wfActivityId;
	}

	public MstActivityDef getMstActivityDef() {
		return this.mstActivityDef;
	}

	public void setMstActivityDef(MstActivityDef mstActivityDef) {
		this.mstActivityDef = mstActivityDef;
	}

	public MstStatus getMstStatus() {
		return this.mstStatus;
	}

	public void setMstStatus(MstStatus mstStatus) {
		this.mstStatus = mstStatus;
	}

	public Process getProcess() {
		return this.process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public Set<ActivityPlan> getActivityPlans() {
		return this.activityPlans;
	}

	public void setActivityPlans(Set<ActivityPlan> activityPlans) {
		this.activityPlans = activityPlans;
	}

	public ActivityPlan addActivityPlan(ActivityPlan activityPlan) {
		getActivityPlans().add(activityPlan);
		activityPlan.setActivity(this);

		return activityPlan;
	}

	public ActivityPlan removeActivityPlan(ActivityPlan activityPlan) {
		getActivityPlans().remove(activityPlan);
		activityPlan.setActivity(null);

		return activityPlan;
	}

	public Set<Task> getTasks() {
		return this.tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	public Task addTask(Task task) {
		getTasks().add(task);
		task.setActivity(this);

		return task;
	}

	public Task removeTask(Task task) {
		getTasks().remove(task);
		task.setActivity(null);

		return task;
	}

}