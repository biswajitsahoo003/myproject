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
 * This file contains the QuoteDirectRoutingCity.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "quote_dr_city")
@NamedQuery(name = "QuoteDirectRoutingCity.findAll", query = "SELECT q FROM QuoteDirectRoutingCity q")
public class QuoteDirectRoutingCity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "mrc")
	private BigDecimal mrc;

	@Column(name = "nrc")
	private BigDecimal nrc;

	@Column(name = "arc")
	private BigDecimal arc;

	@Column(name = "tcv")
	private BigDecimal tcv;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "quoteDirectRoutingCity")
	private List<QuoteDirectRoutingMediaGateways> quoteDirectRoutingMediaGateways;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "quote_dr_id")
	private QuoteDirectRouting quoteDirectRouting;

	@Column(name = "media_gateway_type")
	private String mediaGatewayType;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "quote_version")
	private Integer quoteVersion;

	public QuoteDirectRoutingCity() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<QuoteDirectRoutingMediaGateways> getQuoteDirectRoutingMediaGateways() {
		return quoteDirectRoutingMediaGateways;
	}

	public void setQuoteDirectRoutingMediaGateways(
			List<QuoteDirectRoutingMediaGateways> quoteDirectRoutingMediaGateways) {
		this.quoteDirectRoutingMediaGateways = quoteDirectRoutingMediaGateways;
	}

	public String getMediaGatewayType() {
		return mediaGatewayType;
	}

	public void setMediaGatewayType(String mediaGatewayType) {
		this.mediaGatewayType = mediaGatewayType;
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

	public QuoteDirectRouting getQuoteDirectRouting() {
		return quoteDirectRouting;
	}

	public void setQuoteDirectRouting(QuoteDirectRouting quoteDirectRouting) {
		this.quoteDirectRouting = quoteDirectRouting;
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
}
