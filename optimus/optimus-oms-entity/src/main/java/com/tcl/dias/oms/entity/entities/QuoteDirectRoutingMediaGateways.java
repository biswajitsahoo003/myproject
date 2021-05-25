package com.tcl.dias.oms.entity.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * This file contains the QuoteDirectRoutingMediaGateways.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "quote_dr_mediagateways")
@NamedQuery(name = "QuoteDirectRoutingMediaGateways.findAll", query = "SELECT q FROM QuoteDirectRoutingMediaGateways q")
public class QuoteDirectRoutingMediaGateways {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "quote_dr_cityid")
	private QuoteDirectRoutingCity quoteDirectRoutingCity;

	private Double tcv;

	private String name;

	private int quantity;

	@Column(name = "arc")
	private Double arc;

	@Column(name = "mrc")
	private Double mrc;

	@Column(name = "nrc")
	private Double nrc;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "quote_version")
	private Integer quoteVersion;

	public QuoteDirectRoutingMediaGateways() {
	}

	public Integer getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Double getTcv() {
		return this.tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public Double getArc() {
		return this.arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getMrc() {
		return this.mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return this.nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public QuoteDirectRoutingCity getQuoteDirectRoutingCity() {
		return quoteDirectRoutingCity;
	}

	public void setQuoteDirectRoutingCity(QuoteDirectRoutingCity quoteDirectRoutingCity) {
		this.quoteDirectRoutingCity = quoteDirectRoutingCity;
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
