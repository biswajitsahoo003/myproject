package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "quote_site_differential_commercial")
@NamedQuery(name = "QuoteSiteDifferentialCommercial.findAll", query = "SELECT q FROM QuoteSiteDifferentialCommercial q")
public class QuoteSiteDifferentialCommercial implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	// bi-directional many-to-one association to ProductSolution
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_to_le_id")
	private QuoteToLe quoteToLe;
	
	@Column(name = "quote_code")
	private String quoteCode;
	
	@Column(name="quote_site_id")
	private Integer quoteSiteId;
	
	@Column(name="quote_site_code")
	private String quoteSiteCode;
	
	@Column(name="quote_link_id")
	private Integer quoteLinkId;
	
	@Column(name="quote_link_code")
	private String quoteLinkCode;
	
	@Column(name="tps_service_id")
	private String tpsServiceId;
	
	@Column(name="existing_mrc")
	private Double existingMrc;
	
	@Column(name="existing_nrc")
	private Double existingNrc;
	
	@Column(name="service_type")
	private String serviceType;
	
	@Column(name="differential_mrc")
	private Double differentialMrc;
	
	@Column(name="differential_nrc")
	private Double differentialNrc;

	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public QuoteToLe getQuoteToLe() {
		return quoteToLe;
	}

	public void setQuoteToLe(QuoteToLe quoteToLe) {
		this.quoteToLe = quoteToLe;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public Integer getQuoteSiteId() {
		return quoteSiteId;
	}

	public void setQuoteSiteId(Integer quoteSiteId) {
		this.quoteSiteId = quoteSiteId;
	}

	public String getQuoteSiteCode() {
		return quoteSiteCode;
	}

	public void setQuoteSiteCode(String quoteSiteCode) {
		this.quoteSiteCode = quoteSiteCode;
	}

	public Integer getQuoteLinkId() {
		return quoteLinkId;
	}

	public void setQuoteLinkId(Integer quoteLinkId) {
		this.quoteLinkId = quoteLinkId;
	}

	public String getTpsServiceId() {
		return tpsServiceId;
	}

	public void setTpsServiceId(String tpsServiceId) {
		this.tpsServiceId = tpsServiceId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Double getDifferentialMrc() {
		return differentialMrc;
	}

	public void setDifferentialMrc(Double differentialMrc) {
		this.differentialMrc = differentialMrc;
	}

	public Double getDifferentialNrc() {
		return differentialNrc;
	}

	public void setDifferentialNrc(Double differentialNrc) {
		this.differentialNrc = differentialNrc;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Double getExistingMrc() {
		return existingMrc;
	}

	public void setExistingMrc(Double existingMrc) {
		this.existingMrc = existingMrc;
	}

	public Double getExistingNrc() {
		return existingNrc;
	}

	public void setExistingNrc(Double existingNrc) {
		this.existingNrc = existingNrc;
	}

	public String getQuoteLinkCode() {
		return quoteLinkCode;
	}

	public void setQuoteLinkCode(String quoteLinkCode) {
		this.quoteLinkCode = quoteLinkCode;
	}

	

}
