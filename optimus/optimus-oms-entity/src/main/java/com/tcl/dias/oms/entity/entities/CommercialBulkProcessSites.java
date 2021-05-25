package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * Entity Class
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "commercial_bulk_process_sites")
@NamedQuery(name = "CommercialBulkProcessSites.findAll", query = "SELECT p FROM CommercialBulkProcessSites p")
public class CommercialBulkProcessSites implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "quote_id")
	private Integer quoteId;

	@Column(name = "quote_code")
	private String quoteCode;
	
	@Column(name = "task_id")    
	private Integer taskId;

	// bi-directional many-to-one association to PricingEngineResponse
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	private QuoteIllSite quoteIllSite;

	@Column(name = "status")
	private String status;
	
	@Lob
	@Column(name = "response_json")
	private String responseJson;

	@Column(name = "site_status")
	private String siteStatus;
	
	@Column(name = "attachement_id")
	private Integer attachementId;
	
	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "updated_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;
	
	@Lob
	@Column(name = "remarks")
	private String remarks;
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public CommercialBulkProcessSites() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public QuoteIllSite getQuoteIllSite() {
		return quoteIllSite;
	}

	public void setQuoteIllSite(QuoteIllSite quoteIllSite) {
		this.quoteIllSite = quoteIllSite;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	public String getSiteStatus() {
		return siteStatus;
	}

	public void setSiteStatus(String siteStatus) {
		this.siteStatus = siteStatus;
	}

	public Integer getAttachementId() {
		return attachementId;
	}

	public void setAttachementId(Integer attachementId) {
		this.attachementId = attachementId;
	}

	

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
    
	
}