package com.tcl.dias.networkaugment.entity.entities;

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
 * This file contains the Stage.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name="stage")
@NamedQuery(name = "Stage.findAll", query = "SELECT s FROM Stage s")
public class Stage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "completed_time")
	private Timestamp completedTime;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "due_date")
	private Timestamp dueDate;

	@Column(name = "order_code")
	private String orderCode;

	@Column(name = "sc_order_id")
	private Integer scOrderId;

	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "service_code")
	private String serviceCode;

	@Column(name = "updated_time")
	private Timestamp updatedTime;

	@Column(name = "wf_stage_id")
	private String wfStageId;

	//bi-directional many-to-one association to Process
	@OneToMany(mappedBy = "stage")
	private Set<Process> processes;

	//bi-directional many-to-one association to MstStageDef
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "stage_def_key")
	private MstStageDef mstStageDef;

	//bi-directional many-to-one association to MstStatus
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "status")
	private MstStatus mstStatus;

	//bi-directional many-to-one association to StagePlan
//	@OneToMany(mappedBy = "stage")
//	private Set<StagePlan> stagePlans;

	
	@Column(name = "site_detail_id")
	private Integer siteDetailId;

	@Column(name = "mf_detail_id")
	private Integer mfDetailId;
	

	public Stage() {
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

	public Timestamp getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}

	public String getOrderCode() {
		return this.orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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


	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Timestamp getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getWfStageId() {
		return this.wfStageId;
	}

	public void setWfStageId(String wfStageId) {
		this.wfStageId = wfStageId;
	}

	public Set<Process> getProcesses() {
		return this.processes;
	}

	public void setProcesses(Set<Process> processes) {
		this.processes = processes;
	}

	public Process addProcess(Process process) {
		getProcesses().add(process);
		process.setStage(this);

		return process;
	}

	public Process removeProcess(Process process) {
		getProcesses().remove(process);
		process.setStage(null);

		return process;
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

//	public Set<StagePlan> getStagePlans() {
//		return this.stagePlans;
//	}
//
//	public void setStagePlans(Set<StagePlan> stagePlans) {
//		this.stagePlans = stagePlans;
//	}

//	public StagePlan addStagePlan(StagePlan stagePlan) {
//		getStagePlans().add(stagePlan);
//		stagePlan.setStage(this);
//
//		return stagePlan;
//	}
//
//	public StagePlan removeStagePlan(StagePlan stagePlan) {
//		getStagePlans().remove(stagePlan);
//		stagePlan.setStage(null);
//
//		return stagePlan;
//	}

	public Integer getSiteDetailId() {
		return siteDetailId;
	}

	public void setSiteDetailId(Integer siteDetailId) {
		this.siteDetailId = siteDetailId;
	}

	public Integer getMfDetailId() {
		return mfDetailId;
	}

	public void setMfDetailId(Integer mfDetailId) {
		this.mfDetailId = mfDetailId;
	}
}