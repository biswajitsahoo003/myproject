package com.tcl.dias.oms.entity.entities;

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
 * 
 * Entity Class
 * 
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "commercial_quote_audit")
@NamedQuery(name = "commercial_quote_audit.findAll", query = "SELECT m FROM MstProductFamily m")
public class CommercialQuoteAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "quote_id")
	private Integer quoteId;
	
	@Column(name = "site_id")
	private String siteId;
	
	@Column(name = "commercial_action")
	private String commercialAction;
	
	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Lob
	@Column(name = "approve_json")
	private String approveJson;
	
	
	
	

	public String getApproveJson() {
		return approveJson;
	}

	public void setApproveJson(String approveJson) {
		this.approveJson = approveJson;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getCommercialAction() {
		return commercialAction;
	}

	public void setCommercialAction(String commercialAction) {
		this.commercialAction = commercialAction;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	
	
	

}
