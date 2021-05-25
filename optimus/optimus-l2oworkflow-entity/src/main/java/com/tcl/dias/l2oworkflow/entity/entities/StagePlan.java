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
 * This file contains the StagePlan.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "stage_plan")
@NamedQuery(name = "StagePlan.findAll", query = "SELECT s FROM StagePlan s")
public class StagePlan implements Serializable {
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

	private Byte sequence;

	@Column(name = "service_id")
	private Integer serviceId;
	
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
	
	@Column(name = "site_id")
	private Integer siteId;
	
	@Column(name = "active_version")
	private Long activeVersion;

	//bi-directional many-to-one association to ProcessPlan
	@OneToMany(mappedBy = "stagePlan")
	private Set<ProcessPlan> processPlans;

	//bi-directional many-to-one association to MstStageDef
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "stage_def_key")
	private MstStageDef mstStageDef;

	//bi-directional many-to-one association to MstStatus
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "status")
	private MstStatus mstStatus;

	//bi-directional many-to-one association to Stage
	@ManyToOne(fetch=FetchType.LAZY)
	private Stage stage;
	
	
	

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@Column(name = "order_code")
	private String orderCode;

	public StagePlan() {
	}

	public StagePlan(MstStageDef mstStageDef, Timestamp estimatedStartTime) {
		this.mstStageDef = mstStageDef;
		this.estimatedStartTime = estimatedStartTime;
	}


	public StagePlan(MstStageDef mstStageDef, Timestamp startTime, Integer serviceId,String serviceCode, Integer scOrderId, MstStatus mstStatus) {
		this.mstStageDef = mstStageDef;
		this.estimatedStartTime = startTime;
		this.plannedStartTime = startTime;
		this.serviceId = serviceId;
		this.serviceCode=serviceCode;
		this.scOrderId = scOrderId;
		this.mstStatus = mstStatus;
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

	public Byte getSequence() {
		return this.sequence;
	}

	public void setSequence(Byte sequence) {
		this.sequence = sequence;
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

	public Set<ProcessPlan> getProcessPlans() {
		return this.processPlans;
	}

	public void setProcessPlans(Set<ProcessPlan> processPlans) {
		this.processPlans = processPlans;
	}

	public ProcessPlan addProcessPlan(ProcessPlan processPlan) {
		getProcessPlans().add(processPlan);
		processPlan.setStagePlan(this);

		return processPlan;
	}

	public ProcessPlan removeProcessPlan(ProcessPlan processPlan) {
		getProcessPlans().remove(processPlan);
		processPlan.setStagePlan(null);

		return processPlan;
	}

	public MstStageDef getMstStageDef() {
		return this.mstStageDef;
	}

	public void setMstStageDef(MstStageDef mstStageDef) {
		this.mstStageDef = mstStageDef;
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

	public Timestamp getPlannedEndTime() {
		return plannedEndTime;
	}

	public void setPlannedEndTime(Timestamp plannedEndTime) {
		this.plannedEndTime = plannedEndTime;
	}

	public Timestamp getPlannedStartTime() {
		return plannedStartTime;
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

	public Long getActiveVersion() {
		return activeVersion;
	}

	public void setActiveVersion(Long activeVersion) {
		this.activeVersion = activeVersion;
	}

}