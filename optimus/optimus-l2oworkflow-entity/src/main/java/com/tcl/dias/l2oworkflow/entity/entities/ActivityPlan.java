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
 * This file contains the ActivityPlan.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "activity_plan")
@NamedQuery(name = "ActivityPlan.findAll", query = "SELECT a FROM ActivityPlan a")
public class ActivityPlan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "actual_end_time")
	private Timestamp actualEndTime;

	@Column(name = "actual_start_time")
	private Timestamp actualStartTime;


	@Column(name = "estimated_end_time")
	private Timestamp estimatedEndTime;

	@Column(name = "estimated_start_time")
	private Timestamp estimatedStartTime;

	private Integer sequence;

	@Column(name = "targetted_end_time")
	private Timestamp targettedEndTime;

	@Column(name = "targetted_start_time")
	private Timestamp targettedStartTime;

	@Column(name = "planned_end_time")
	private Timestamp plannedEndTime;

	@Column(name = "planned_start_time")
	private Timestamp plannedStartTime;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "site_id")
	private Integer siteId;
	
	@Column(name = "service_code")
	private String serviceCode;

	@Column(name = "sc_order_id")
	private Integer orderId;

	//bi-directional many-to-one association to Activity
	@ManyToOne(fetch=FetchType.LAZY)
	private Activity activity;

	//bi-directional many-to-one association to MstActivityDef
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "activity_def_key")
	private MstActivityDef mstActivityDef;

	//bi-directional many-to-one association to MstStatus
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "status")
	private MstStatus mstStatus;

	//bi-directional many-to-one association to ProcessPlan
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "process_plan_id")
	private ProcessPlan processPlan;

	//bi-directional many-to-one association to TaskPlan
	@OneToMany(mappedBy = "activityPlan")
	private Set<TaskPlan> taskPlans;
	
	@Column(name = "order_code")
	private String orderCode;
	

	@Column(name = "active_version")
	private Long activeVersion;
	
	

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public ActivityPlan() {
	}

	public ActivityPlan(MstActivityDef mstActivityDef, Timestamp startTime, ProcessPlan processPlan, MstStatus mstStatus, Integer orderId, Integer serviceId,String serviceCode) {
		this.mstActivityDef = mstActivityDef;
		this.estimatedStartTime = startTime;
		this.plannedStartTime = startTime;
		this.targettedStartTime=startTime;
		this.processPlan = processPlan;
		this.mstStatus = mstStatus;
		this.orderId = orderId;
		this.serviceId = serviceId;
		this.serviceCode=serviceCode;
	}

    public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getActualEndTime() {
		return this.actualEndTime;
	}

	public void setActualEndTime(Timestamp actualEndTime) {
		this.actualEndTime = actualEndTime;
	}

	public Timestamp getActualStartTime() {
		return this.actualStartTime;
	}

	public void setActualStartTime(Timestamp actualStartTime) {
		this.actualStartTime = actualStartTime;
	}


	public Timestamp getEstimatedEndTime() {
		return this.estimatedEndTime;
	}

	public void setEstimatedEndTime(Timestamp estimatedEndTime) {
		this.estimatedEndTime = estimatedEndTime;
	}

	public Timestamp getEstimatedStartTime() {
		return this.estimatedStartTime;
	}

	public void setEstimatedStartTime(Timestamp estimatedStartTime) {
		this.estimatedStartTime = estimatedStartTime;
	}

	public Integer getSequence() {
		return this.sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Timestamp getTargettedEndTime() {
		return this.targettedEndTime;
	}

	public void setTargettedEndTime(Timestamp targettedEndTime) {
		this.targettedEndTime = targettedEndTime;
	}

	public Timestamp getTargettedStartTime() {
		return this.targettedStartTime;
	}

	public void setTargettedStartTime(Timestamp targettedStartTime) {
		this.targettedStartTime = targettedStartTime;
	}

	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
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

	public ProcessPlan getProcessPlan() {
		return this.processPlan;
	}

	public void setProcessPlan(ProcessPlan processPlan) {
		this.processPlan = processPlan;
	}

	public Set<TaskPlan> getTaskPlans() {
		return this.taskPlans;
	}

	public void setTaskPlans(Set<TaskPlan> taskPlans) {
		this.taskPlans = taskPlans;
	}

	public TaskPlan addTaskPlan(TaskPlan taskPlan) {
		getTaskPlans().add(taskPlan);
		taskPlan.setActivityPlan(this);

		return taskPlan;
	}

	public TaskPlan removeTaskPlan(TaskPlan taskPlan) {
		getTaskPlans().remove(taskPlan);
		taskPlan.setActivityPlan(null);

		return taskPlan;
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

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public void setPlannedEndTime(Timestamp plannedEndTime) {
		this.plannedEndTime = plannedEndTime;
	}

	public void setPlannedStartTime(Timestamp plannedStartTime) {
		this.plannedStartTime = plannedStartTime;
	}

	public Timestamp getPlannedEndTime() {
		return plannedEndTime;
	}

	public Timestamp getPlannedStartTime() {
		return plannedStartTime;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Long getActiveVersion() {
		return activeVersion;
	}

	public void setActiveVersion(Long activeVersion) {
		this.activeVersion = activeVersion;
	}
	
	
}