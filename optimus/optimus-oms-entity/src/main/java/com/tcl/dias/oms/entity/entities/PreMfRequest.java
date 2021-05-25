package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.OneToOne;
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
@Table(name = "pre_mf_request ")
@NamedQuery(name = "PreMfRequest .findAll", query = "SELECT o FROM PreMfRequest o")
public class PreMfRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "site_code")
	private String siteCode;
	
	@Column(name = "erf_loc_sitea_location_id")
	private Integer erfLocSiteaLocationId;
	
	@Column(name = "customer_id")
	private Integer customerId;
	
	@Column(name = "opportunity_id")
	private Integer opportunityId;
	
	@Column(name = "bandwidth")
	private String bandwidth;
	
	@Column(name = "mf_product_id")
	private Integer mfProductId;
	
	@Column(name = "local_contact_number")
	private String localContactNumber;
	
	@Column(name = "local_contact_name")
	private String localContactName;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "feasibility")
	private Byte feasibility;
	
	@Column(name = "mf_quote_id")
	private Integer mfQuoteId;
	
	@Column(name = "mf_quote_code")
	private String mfQuoteCode;
	
	@Column(name = "request_data")
	private String requestData;
	
	@Column(name = "site_type")
	private String siteType;
	
	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	
	@Column(name = "created_by")
	private Integer created_by;
	
	

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
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

	public Integer getErfLocSiteaLocationId() {
		return erfLocSiteaLocationId;
	}

	public void setErfLocSiteaLocationId(Integer erfLocSiteaLocationId) {
		this.erfLocSiteaLocationId = erfLocSiteaLocationId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(Integer opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public Integer getMfProductId() {
		return mfProductId;
	}

	public void setMfProductId(Integer mfProductId) {
		this.mfProductId = mfProductId;
	}

	public String getLocalContactNumber() {
		return localContactNumber;
	}

	public void setLocalContactNumber(String localContactNumber) {
		this.localContactNumber = localContactNumber;
	}

	public String getLocalContactName() {
		return localContactName;
	}

	public void setLocalContactName(String localContactName) {
		this.localContactName = localContactName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Byte getFeasibility() {
		return feasibility;
	}

	public void setFeasibility(Byte feasibility) {
		this.feasibility = feasibility;
	}

	public Integer getMfQuoteId() {
		return mfQuoteId;
	}

	public void setMfQuoteId(Integer mfQuoteId) {
		this.mfQuoteId = mfQuoteId;
	}

	public String getMfQuoteCode() {
		return mfQuoteCode;
	}

	public void setMfQuoteCode(String mfQuoteCode) {
		this.mfQuoteCode = mfQuoteCode;
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


}
