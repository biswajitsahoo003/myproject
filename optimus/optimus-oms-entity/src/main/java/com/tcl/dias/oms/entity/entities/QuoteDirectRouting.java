package com.tcl.dias.oms.entity.entities;

import javax.persistence.CascadeType;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * This file contains the QuoteDirectRouting.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "quote_dr")
@NamedQuery(name = "QuoteDirectRouting.findAll", query = "SELECT q FROM QuoteDirectRouting q")
public class QuoteDirectRouting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	private String country;

	@Column(name = "mrc")
	private BigDecimal mrc;

	@Column(name = "nrc")
	private BigDecimal nrc;

	@Column(name = "arc")
	private BigDecimal arc;

	@Column(name = "tcv")
	private BigDecimal tcv;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "quoteDirectRouting")
	private List<QuoteDirectRoutingCity> quoteDirectRoutingCityDetails;

	// bi-directional many-to-one association to QuoteTeamsDR
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_teamsdr_id")
	private QuoteTeamsDR quoteTeamsDR;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "quote_version")
	private Integer quoteVersion;

	public QuoteDirectRouting() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public BigDecimal getMrc() {
		return mrc;
	}

	public void setMrc(BigDecimal mrc) {
		this.mrc = mrc;
	}

	public BigDecimal getNrc() {
		return nrc;
	}

	public void setNrc(BigDecimal nrc) {
		this.nrc = nrc;
	}

	public BigDecimal getArc() {
		return arc;
	}

	public void setArc(BigDecimal arc) {
		this.arc = arc;
	}

	public BigDecimal getTcv() {
		return tcv;
	}

	public void setTcv(BigDecimal tcv) {
		this.tcv = tcv;
	}

	public List<QuoteDirectRoutingCity> getQuoteDirectRoutingCityDetails() {
		return quoteDirectRoutingCityDetails;
	}

	public void setQuoteDirectRoutingCityDetails(List<QuoteDirectRoutingCity> quoteDirectRoutingCityDetails) {
		this.quoteDirectRoutingCityDetails = quoteDirectRoutingCityDetails;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getQuoteVersion() {
		return quoteVersion;
	}

	public void setQuoteVersion(Integer quoteVersion) {
		this.quoteVersion = quoteVersion;
	}

	public QuoteTeamsDR getQuoteTeamsDR() {
		return quoteTeamsDR;
	}

	public void setQuoteTeamsDR(QuoteTeamsDR quoteTeamsDR) {
		this.quoteTeamsDR = quoteTeamsDR;
	}
}
