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
 * This file contains the Process.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name="process")
@NamedQuery(name = "Process.findAll", query = "SELECT p FROM Process p")
public class Process implements Serializable {
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

	@Column(name = "wf_proc_inst_id")
	private String wfProcInstId;

	//bi-directional many-to-one association to Activity
	@OneToMany(mappedBy = "process")
	private Set<Activity> activities;

	//bi-directional many-to-one association to MstProcessDef
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "process_def_key")
	private MstProcessDef mstProcessDef;

	//bi-directional many-to-one association to MstStatus
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "status")
	private MstStatus mstStatus;

	//bi-directional many-to-one association to Stage
	@ManyToOne(fetch=FetchType.LAZY)
	private Stage stage;

	//bi-directional many-to-one association to ProcessPlan
	@OneToMany(mappedBy = "process")
	private Set<ProcessPlan> processPlans;

	public Process() {
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

	public String getWfProcInstId() {
		return this.wfProcInstId;
	}

	public void setWfProcInstId(String wfProcInstId) {
		this.wfProcInstId = wfProcInstId;
	}

	public Set<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	public Activity addActivity(Activity activity) {
		getActivities().add(activity);
		activity.setProcess(this);

		return activity;
	}

	public Activity removeActivity(Activity activity) {
		getActivities().remove(activity);
		activity.setProcess(null);

		return activity;
	}

	public MstProcessDef getMstProcessDef() {
		return this.mstProcessDef;
	}

	public void setMstProcessDef(MstProcessDef mstProcessDef) {
		this.mstProcessDef = mstProcessDef;
	}

	public MstStatus getMstStatus() {
		return this.mstStatus;
	}

	public void setMstStatus(MstStatus mstStatus) {
		this.mstStatus = mstStatus;
	}

	public Stage getStage() {
		return this.stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Set<ProcessPlan> getProcessPlans() {
		return this.processPlans;
	}

	public void setProcessPlans(Set<ProcessPlan> processPlans) {
		this.processPlans = processPlans;
	}

	public ProcessPlan addProcessPlan(ProcessPlan processPlan) {
		getProcessPlans().add(processPlan);
		processPlan.setProcess(this);

		return processPlan;
	}

	public ProcessPlan removeProcessPlan(ProcessPlan processPlan) {
		getProcessPlans().remove(processPlan);
		processPlan.setProcess(null);

		return processPlan;
	}

}