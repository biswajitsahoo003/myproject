package com.tcl.dias.l2oworkflow.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the site_detail database table.
 * 
 */
@Entity
@Table(name="site_detail")
@NamedQuery(name="SiteDetail.findAll", query="SELECT s FROM SiteDetail s")
public class SiteDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="is_active")
	private Integer isActive;

	@Column(name="location_id")
	private Integer locationId;

	@Column(name="quote_code")
	private String quoteCode;

	@Column(name="quote_id")
	private Integer quoteId;

	@Column(name="site_code")
	private String siteCode;

	@Column(name="site_id")
	private Integer siteId;

	private String status;
	
	@Column(name="created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_time")
	private Date createdTime;
	
	@Column(name="updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_time")
	private Date updatedTime;
	
	@Column(name="quote_created_by")
	private String quoteCreatedBy;
	
	@Column(name="quote_type")
	private String quoteType;
	
	@Column(name="account_name")
	private String accountName;

	@Column(name="contract_term")
	private String contractTerm;
	
	@Column(name="opportunity_id")
	private String opportunityId;
	
	@Column(name="quote_created_user_type")
	private String quoteCreatedUserType;

	//bi-directional many-to-one association to Task
	@OneToMany(mappedBy="siteDetail")
	private List<Task> tasks;
	
	@Column(name="site_detail")
	private String siteDetail;
	
	@Column(name="region")
	private String region;
	
	@Column(name="discount_approval_level")
	private Integer discountApprovalLevel;

	/*
	 * @Column(name = "req_comments") private String reqComments;
	 */

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSiteDetail() {
		return siteDetail;
	}

	public void setSiteDetail(String siteDetail) {
		this.siteDetail = siteDetail;
	}

	public SiteDetail() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getLocationId() {
		return this.locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getQuoteCode() {
		return this.quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public Integer getQuoteId() {
		return this.quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public String getSiteCode() {
		return this.siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public Integer getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Task> getTasks() {
		return this.tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public Task addTask(Task task) {
		getTasks().add(task);
		task.setSiteDetail(this);

		return task;
	}

	public Task removeTask(Task task) {
		getTasks().remove(task);
		task.setSiteDetail(null);

		return task;
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

	public String getQuoteCreatedBy() {
		return quoteCreatedBy;
	}

	public void setQuoteCreatedBy(String quoteCreatedBy) {
		this.quoteCreatedBy = quoteCreatedBy;
	}

	public String getQuoteType() {
		return quoteType;
	}

	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getContractTerm() {
		return contractTerm;
	}

	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
	}

	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	public Integer getDiscountApprovalLevel() {
		return discountApprovalLevel;
	}

	public void setDiscountApprovalLevel(Integer discountApprovalLevel) {
		this.discountApprovalLevel = discountApprovalLevel;
	}

	public String getQuoteCreatedUserType() {
		return quoteCreatedUserType;
	}

	public void setQuoteCreatedUserType(String quoteCreatedUserType) {
		this.quoteCreatedUserType = quoteCreatedUserType;
	}

	/*
	 * public String getReqComments() { return reqComments; }
	 * 
	 * public void setReqComments(String reqComments) { this.reqComments =
	 * reqComments; }
	 */
}