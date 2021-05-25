package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
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
@Table(name = "quote_vrf_sites")
@NamedQuery(name = "QuoteVrfSites.findAll", query = "SELECT q FROM QuoteVrfSites q")
public class QuoteVrfSites implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	
	@Column(name = "vrf_name")
	private String vrfName;
	
	@Column(name = "vrf_type")
	private String vrfType;
	
	@Column(name = "updated_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;
	
	@Column(name = "arc")
	private Double arc;

	@Column(name = "nrc")
	private Double nrc;

	@Column(name = "mrc")
	private Double mrc;

	@Column(name = "tcv")
	private Double tcv;
	
	@Column(name = "site_type")
	private String siteType;
	
	// bi-directional many-to-one association to quoteIllSite
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	private QuoteIllSite quoteIllSite;
	
	
	@Column(name = "tps_service_id")
	private String tpsServiceId;
	
	

	

	public String getTpsServiceId() {
		return tpsServiceId;
	}

	public void setTpsServiceId(String tpsServiceId) {
		this.tpsServiceId = tpsServiceId;
	}

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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getVrfName() {
		return vrfName;
	}

	public void setVrfName(String vrfName) {
		this.vrfName = vrfName;
	}

	public String getVrfType() {
		return vrfType;
	}

	public void setVrfType(String vrfType) {
		this.vrfType = vrfType;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public QuoteIllSite getQuoteIllSite() {
		return quoteIllSite;
	}

	public void setQuoteIllSite(QuoteIllSite quoteIllSite) {
		this.quoteIllSite = quoteIllSite;
	}
	
	
	
	
	

		

	
}