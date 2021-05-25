package com.tcl.dias.l2oworkflow.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the mf_details database table.
 * 
 */
@Entity
@Table(name="mf_detail")
@NamedQuery(name="MfDetail.findAll", query="SELECT m FROM MfDetail m")
public class MfDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_time")
	private Date createdTime;

	@Column(name="is_active")
	private Integer isActive;

	@Lob
	@Column(name="mf_details")
	private String mfDetails;

	@Column(name="quote_code")
	private String quoteCode;

	@Column(name="quote_id")
	private Integer quoteId;

	@Column(name="quote_le_id")
	private Integer quoteLeId;

	@Column(name="site_code")
	private String siteCode;

	@Column(name="site_id")
	private Integer siteId;

	private String status;

	@Column(name="updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_time")
	private Date updatedTime;
	
	@Column(name="region")
	private String region;
	
	@Column(name="assigned_to")
	private String assignedTo;
	
	@Column(name="site_type")
	private String siteType;
	

	@Column(name="is_pre_mf_task")
	private String isPreMfTask;
	
	
	@Column(name="quote_created_user_type")
	private String quoteCreatedUserType;
	
	@Lob
	@Column(name="mf_link_response_json")
	private String mfLinkResponseJson;
	
	@Lob
	@Column(name="system_link_response_json")
	private String systemLinkResponseJson;

	@Column(name="link_id")
	private Integer linkId;

	public Integer getLinkId() {
		return linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}

	public String getIsPreMfTask() {
		return isPreMfTask;
	}

	public void setIsPreMfTask(String isPreMfTask) {
		this.isPreMfTask = isPreMfTask;
	}

	public MfDetail() {
		//DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getMfDetails() {
		return mfDetails;
	}

	public void setMfDetails(String mfDetails) {
		this.mfDetails = mfDetails;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public Integer getQuoteLeId() {
		return quoteLeId;
	}

	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getQuoteCreatedUserType() {
		return quoteCreatedUserType;
	}

	public void setQuoteCreatedUserType(String quoteCreatedUserType) {
		this.quoteCreatedUserType = quoteCreatedUserType;
	}
	
	public String getMfLinkResponseJson() {
		return mfLinkResponseJson;
	}

	public void setMfLinkResponseJson(String mfLinkResponseJson) {
		this.mfLinkResponseJson = mfLinkResponseJson;
	}

	public String getSystemLinkResponseJson() {
		return systemLinkResponseJson;
	}

	public void setSystemLinkResponseJson(String systemLinkResponseJson) {
		this.systemLinkResponseJson = systemLinkResponseJson;
	}
	
	
}
