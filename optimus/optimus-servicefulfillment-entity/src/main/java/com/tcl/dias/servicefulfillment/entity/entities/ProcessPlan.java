package com.tcl.dias.servicefulfillment.entity.entities;

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
 * This file contains the ProcessPlan.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "process_plan")
@NamedQuery(name = "ProcessPlan.findAll", query = "SELECT p FROM ProcessPlan p")
public class ProcessPlan implements Serializable {
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
	
	@Column(name="service_code")
	private String serviceCode;

	@Column(name = "sc_order_id")
	private Integer orderId;
	
	@Column(name = "site_id")
	private Integer siteId;
	

	@Column(name = "active_version")
	private Long activeVersion;
	
	@Column(name = "start_preceders")
	private String startPreceders;
	
	@Column(name = "end_preceders")
	private String endPreceders;
	
	@Column(name = "critical_path")
	private String criticalPath;

	//bi-directional many-to-one association to ActivityPlan
	@OneToMany(mappedBy = "processPlan")
	private Set<ActivityPlan> activityPlans;

	//bi-directional many-to-one association to MstProcessDef
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "process_def_key")
	private MstProcessDef mstProcessDef;

	//bi-directional many-to-one association to MstStatus
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "status")
	private MstStatus mstStatus;

	//bi-directional many-to-one association to Process
	@ManyToOne(fetch=FetchType.LAZY)
	private Process process;

	//bi-directional many-to-one association to StagePlan
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "stage_plan_id")
	private StagePlan stagePlan;

	
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

	public ProcessPlan() {
	}

	public ProcessPlan(MstProcessDef mstProcessDef, Timestamp startTime, StagePlan stagePlan, MstStatus mstStatus,
					   Integer orderId, Integer serviceId,String serviceCode) {
		this.mstProcessDef = mstProcessDef;
		this.estimatedStartTime = startTime;
		this.plannedStartTime = startTime;
		this.stagePlan = stagePlan;
		this.targettedStartTime=startTime;
		this.mstStatus = mstStatus;
		this.serviceId = serviceId;
		this.serviceCode=serviceCode;
		this.orderId = orderId;
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

	public Set<ActivityPlan> getActivityPlans() {
		return this.activityPlans;
	}

	public void setActivityPlans(Set<ActivityPlan> activityPlans) {
		this.activityPlans = activityPlans;
	}

	public ActivityPlan addActivityPlan(ActivityPlan activityPlan) {
		getActivityPlans().add(activityPlan);
		activityPlan.setProcessPlan(this);

		return activityPlan;
	}

	public ActivityPlan removeActivityPlan(ActivityPlan activityPlan) {
		getActivityPlans().remove(activityPlan);
		activityPlan.setProcessPlan(null);

		return activityPlan;
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

	public Process getProcess() {
		return this.process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public StagePlan getStagePlan() {
		return this.stagePlan;
	}

	public void setStagePlan(StagePlan stagePlan) {
		this.stagePlan = stagePlan;
	}
	

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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
    
	public String getStartPreceders() {
		return startPreceders;
	}

	public void setStartPreceders(String startPreceders) {
		this.startPreceders = startPreceders;
	}

	public String getEndPreceders() {
		return endPreceders;
	}

	public void setEndPreceders(String endPreceders) {
		this.endPreceders = endPreceders;
	}

	public String getCriticalPath() {
		return criticalPath;
	}

	public void setCriticalPath(String criticalPath) {
		this.criticalPath = criticalPath;
	}    
}