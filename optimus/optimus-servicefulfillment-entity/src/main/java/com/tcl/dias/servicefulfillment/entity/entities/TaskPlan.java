package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * This file contains the TaskPlan.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "task_plan")
@NamedQuery(name = "TaskPlan.findAll", query = "SELECT t FROM TaskPlan t")
public class TaskPlan implements Serializable {
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

	@Column(name = "sc_order_id")
	private Integer scOrderId;

	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "site_id")
	private Integer siteId;
	
	@Column(name = "service_code")
	private String serviceCode;


	@Column(name = "targetted_end_time")
	private Timestamp targettedEndTime;

	@Column(name = "targetted_start_time")
	private Timestamp targettedStartTime;

	@Column(name = "planned_end_time")
	private Timestamp plannedEndTime;

	@Column(name = "planned_start_time")
	private Timestamp plannedStartTime;
	

	@Column(name = "active_version")
	private Long activeVersion;

	@Column(name = "preceders")
	private String preceders;
		
	@Column(name = "critical_path")
	private String criticalPath;
	
	//bi-directional many-to-one association to ActivityPlan
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "activity_plan_id")
	private ActivityPlan activityPlan;

	//bi-directional many-to-one association to MstStatus
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "status")
	private MstStatus mstStatus;

	//bi-directional many-to-one association to MstTaskDef
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "task_def_key")
	private MstTaskDef mstTaskDef;

	//bi-directional many-to-one association to Task
	@ManyToOne(fetch=FetchType.LAZY)
	private Task task;

	@Column(name = "order_code")
	private String orderCode;
	
	@Column(name = "site_type")
	private String siteType="A";
	
	
	
	
	


	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public TaskPlan() {
	}

	public TaskPlan(MstTaskDef mstTaskDef, Timestamp startTime, Timestamp endTime, ActivityPlan activityPlan, MstStatus mstStatus, Integer scOrderId, Integer serviceId,String serviceCode) {
		this.mstTaskDef = mstTaskDef;
		this.estimatedStartTime = startTime;
		this.estimatedEndTime = endTime;
		this.plannedStartTime = startTime;
		this.plannedEndTime = endTime;
		this.targettedEndTime=endTime;
		this.targettedStartTime=startTime;
		this.activityPlan = activityPlan;
		this.mstStatus = mstStatus;
		this.scOrderId = scOrderId;
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

	public Integer getScOrderId() {
		return this.scOrderId;
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

	public ActivityPlan getActivityPlan() {
		return this.activityPlan;
	}

	public void setActivityPlan(ActivityPlan activityPlan) {
		this.activityPlan = activityPlan;
	}

	public MstStatus getMstStatus() {
		return this.mstStatus;
	}

	public void setMstStatus(MstStatus mstStatus) {
		this.mstStatus = mstStatus;
	}

	public MstTaskDef getMstTaskDef() {
		return this.mstTaskDef;
	}

	public void setMstTaskDef(MstTaskDef mstTaskDef) {
		this.mstTaskDef = mstTaskDef;
	}

	public Task getTask() {
		return this.task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setPlannedEndTime(Timestamp plannedEndTime) {
		this.plannedEndTime = plannedEndTime;
	}

	public void setPlannedStartTime(Timestamp plannedStartTime) {
		this.plannedStartTime = plannedStartTime;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Timestamp getPlannedEndTime() {
		return plannedEndTime;
	}

	public Timestamp getPlannedStartTime() {
		return plannedStartTime;
	}

	public Long getActiveVersion() {
		return activeVersion;
	}

	public void setActiveVersion(Long activeVersion) {
		this.activeVersion = activeVersion;
	}

	public String getPreceders() {
		return preceders;
	}

	public void setPreceders(String preceders) {
		this.preceders = preceders;
	}

	public String getCriticalPath() {
		return criticalPath;
	}

	public void setCriticalPath(String criticalPath) {
		this.criticalPath = criticalPath;
	}
		
}