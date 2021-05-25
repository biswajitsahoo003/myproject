package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * 
 *  DiscountDelegationAudit Entity class
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="discount_delegation_audit")
@NamedQuery(name="DiscountDelegationAudit.findAll", query="SELECT d FROM DiscountDelegationAudit d")
public class DiscountDelegationAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="approver_level")
	private Integer approverLevel;

	@Column(name="is_active")
	private Integer isActive;

	@Column(name="quote_id")
	private Integer quoteId;

	@Column(name="request_json")
	private String requestJson;

	private Integer sequence;

	@Column(name="site_id")
	private Integer siteId;

	private String status;
	
	@Column(name="created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_date")
	private Date updatedDate;

	public DiscountDelegationAudit() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApproverLevel() {
		return this.approverLevel;
	}

	public void setApproverLevel(Integer approverLevel) {
		this.approverLevel = approverLevel;
	}

	public Integer getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getQuoteId() {
		return this.quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public String getRequestJson() {
		return this.requestJson;
	}

	public void setRequestJson(String requestJson) {
		this.requestJson = requestJson;
	}

	public Integer getSequence() {
		return this.sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
}