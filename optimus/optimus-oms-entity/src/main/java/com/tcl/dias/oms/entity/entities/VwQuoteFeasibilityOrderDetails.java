package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="vw_quote_feasibility_order_details")
@NamedQuery(name="VwQuoteFeasibilityOrderDetails.findAll", query="SELECT v FROM VwQuoteFeasibilityOrderDetails v")
public class VwQuoteFeasibilityOrderDetails implements Serializable{
	private static final long serialVersionUID = 1L;

	
	@Column(name="quote_id")
	private Integer quoteId;
	
	@Column(name="quote_code")
	private String quoteCode;
	
	@Column(name="quote_created_time")
	private Timestamp quoteCreatedTime;
	
	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name="site_code")
	private String siteCode;
	
	@Column(name="fp_status")
	private String fpStatus;
	
	@Column(name="feasibility_code")
	private String feasibilityCode;
	
	@Column(name="feasibility_mode")
	private String feasibilityMode;
	
	@Column(name="provider")
	private String provider;
	
	@Column(name="feasibility_check")
	private String feasibilityCheck;
	
	@Column(name="feasibility_selected")
	private Byte feasibilitySelected;
	
	@Column(name="task_def_key")
	private String taskDefKey;
	
	@Column(name="status")
	private String status;
	
	@Column(name="created_time")
	private Timestamp createdTime;
	
	@Column(name="updated_time")
	private Timestamp updatedTime;
	
	@Column(name="completed_time")
	private Timestamp completedTime;
	
	@Column(name="subject")
	private String subject;
	
	@Column(name="assigned_group")
	private String assignedGroup;
	
	@Column(name="feasibility_status")
	private String feasibilityStatus;
	
	@Column(name="task_selected")
	private String taskSelected;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="feasibility_response_date")
	private Date feasibilityResponseDate;
	
	@Column(name="final_last_mile_provider")
	private String finalLastMileProvider;
	
	@Column(name="order_code")
	private String orderCode;
	
	@Column(name="order_created_time")
	private Timestamp orderCreatedTime;
	
	@Column(name="bw_in_mbps")
	private String bwInMbps;
	
	@Column(name="Mast_3KM_avg_mast_ht")
	private String mast3kmAvgMastHt;

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public Timestamp getQuoteCreatedTime() {
		return quoteCreatedTime;
	}

	public void setQuoteCreatedTime(Timestamp quoteCreatedTime) {
		this.quoteCreatedTime = quoteCreatedTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getFpStatus() {
		return fpStatus;
	}

	public void setFpStatus(String fpStatus) {
		this.fpStatus = fpStatus;
	}

	public String getFeasibilityCode() {
		return feasibilityCode;
	}

	public void setFeasibilityCode(String feasibilityCode) {
		this.feasibilityCode = feasibilityCode;
	}

	public String getFeasibilityMode() {
		return feasibilityMode;
	}

	public void setFeasibilityMode(String feasibilityMode) {
		this.feasibilityMode = feasibilityMode;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getFeasibilityCheck() {
		return feasibilityCheck;
	}

	public void setFeasibilityCheck(String feasibilityCheck) {
		this.feasibilityCheck = feasibilityCheck;
	}

	public Byte getFeasibilitySelected() {
		return feasibilitySelected;
	}

	public void setFeasibilitySelected(Byte feasibilitySelected) {
		this.feasibilitySelected = feasibilitySelected;
	}

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Timestamp getCompletedTime() {
		return completedTime;
	}

	public void setCompletedTime(Timestamp completedTime) {
		this.completedTime = completedTime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAssignedGroup() {
		return assignedGroup;
	}

	public void setAssignedGroup(String assignedGroup) {
		this.assignedGroup = assignedGroup;
	}

	public String getFeasibilityStatus() {
		return feasibilityStatus;
	}

	public void setFeasibilityStatus(String feasibilityStatus) {
		this.feasibilityStatus = feasibilityStatus;
	}

	public String getTaskSelected() {
		return taskSelected;
	}

	public void setTaskSelected(String taskSelected) {
		this.taskSelected = taskSelected;
	}

	public Date getFeasibilityResponseDate() {
		return feasibilityResponseDate;
	}

	public void setFeasibilityResponseDate(Date feasibilityResponseDate) {
		this.feasibilityResponseDate = feasibilityResponseDate;
	}

	public String getFinalLastMileProvider() {
		return finalLastMileProvider;
	}

	public void setFinalLastMileProvider(String finalLastMileProvider) {
		this.finalLastMileProvider = finalLastMileProvider;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Timestamp getOrderCreatedTime() {
		return orderCreatedTime;
	}

	public void setOrderCreatedTime(Timestamp orderCreatedTime) {
		this.orderCreatedTime = orderCreatedTime;
	}

	public String getBwInMbps() {
		return bwInMbps;
	}

	public void setBwInMbps(String bwInMbps) {
		this.bwInMbps = bwInMbps;
	}

	public String getMast3kmAvgMstHt() {
		return mast3kmAvgMastHt;
	}

	public void setMast3kmAvgMstHt(String mast3kmAvgMstHt) {
		this.mast3kmAvgMastHt = mast3kmAvgMstHt;
	}


	

	
}