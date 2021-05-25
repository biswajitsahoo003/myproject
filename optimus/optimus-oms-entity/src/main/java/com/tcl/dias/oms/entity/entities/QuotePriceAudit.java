package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

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

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "quote_price_audit")
@NamedQuery(name = "QuotePriceAudit.findAll", query = "SELECT q FROM QuotePriceAudit q")
public class QuotePriceAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "from_arc_price")
	private Double fromArcPrice;

	@Column(name = "from_mrc_price")
	private Double fromMrcPrice;

	@Column(name = "from_nrc_price")
	private Double fromNrcPrice;

	@Column(name = "is_deleted_or_refreshed")
	private Byte idDeletedOrRefreshed;

	@Column(name = "quote_ref_id")
	private String quoteRefId;

	@Column(name = "to_mrc_price")
	private Double toMrcPrice;

	@Column(name = "to_nrc_price")
	private Double toNrcPrice;

	@Column(name = "to_arc_price")
	private Double toArcPrice;
	
	@Column(name = "from_effective_usage_price")
	private Double fromEffectiveUsagePrice;
	
	@Column(name = "to_effective_usage_price")
	private Double toEffectiveUsagePrice;

	// bi-directional many-to-one association to QuotePrice
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_price_id")
	private QuotePrice quotePrice;

	@Column(name = "currency_code")
	private String currencyCode;
	
	@Column(name = "from_arc_discount")
	private Double fromArcDiscount;
	
	@Column(name = "to_arc_discount")
	private Double toArcDiscount;
	
	@Column(name = "from_nrc_discount")
	private Double fromNrcDiscount;
	
	@Column(name = "to_nrc_disocunt")
	private Double toNrcDisocunt;
	
	@Column(name = "from_mrc_discount")
	private Double fromMrcDiscount;
	
	@Column(name = "to_mrc_discount")
	private Double toMrcDiscount;
	
	

	public Double getFromArcDiscount() {
		return fromArcDiscount;
	}

	public void setFromArcDiscount(Double fromArcDiscount) {
		this.fromArcDiscount = fromArcDiscount;
	}

	public Double getToArcDiscount() {
		return toArcDiscount;
	}

	public void setToArcDiscount(Double toArcDiscount) {
		this.toArcDiscount = toArcDiscount;
	}

	public Double getFromNrcDiscount() {
		return fromNrcDiscount;
	}

	public void setFromNrcDiscount(Double fromNrcDiscount) {
		this.fromNrcDiscount = fromNrcDiscount;
	}

	public Double getToNrcDisocunt() {
		return toNrcDisocunt;
	}

	public void setToNrcDisocunt(Double toNrcDisocunt) {
		this.toNrcDisocunt = toNrcDisocunt;
	}

	public Double getFromMrcDiscount() {
		return fromMrcDiscount;
	}

	public void setFromMrcDiscount(Double fromMrcDiscount) {
		this.fromMrcDiscount = fromMrcDiscount;
	}

	public Double getToMrcDiscount() {
		return toMrcDiscount;
	}

	public void setToMrcDiscount(Double toMrcDiscount) {
		this.toMrcDiscount = toMrcDiscount;
	}

	public QuotePriceAudit() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Double getFromArcPrice() {
		return this.fromArcPrice;
	}

	public void setFromArcPrice(Double fromArcPrice) {
		this.fromArcPrice = fromArcPrice;
	}

	public Double getFromMrcPrice() {
		return this.fromMrcPrice;
	}

	public void setFromMrcPrice(Double fromMrcPrice) {
		this.fromMrcPrice = fromMrcPrice;
	}

	public Double getFromNrcPrice() {
		return this.fromNrcPrice;
	}

	public void setFromNrcPrice(Double fromNrcPrice) {
		this.fromNrcPrice = fromNrcPrice;
	}

	public Byte getIdDeletedOrRefreshed() {
		return idDeletedOrRefreshed;
	}

	public void setIdDeletedOrRefreshed(Byte idDeletedOrRefreshed) {
		this.idDeletedOrRefreshed = idDeletedOrRefreshed;
	}

	public String getQuoteRefId() {
		return this.quoteRefId;
	}

	public void setQuoteRefId(String quoteRefId) {
		this.quoteRefId = quoteRefId;
	}

	public Double getToMrcPrice() {
		return this.toMrcPrice;
	}

	public void setToMrcPrice(Double toMrcPrice) {
		this.toMrcPrice = toMrcPrice;
	}

	public Double getToNrcPrice() {
		return this.toNrcPrice;
	}

	public void setToNrcPrice(Double toNrcPrice) {
		this.toNrcPrice = toNrcPrice;
	}

	public Double getToArcPrice() {
		return toArcPrice;
	}

	public void setToArcPrice(Double toArcPrice) {
		this.toArcPrice = toArcPrice;
	}

	public QuotePrice getQuotePrice() {
		return this.quotePrice;
	}

	public void setQuotePrice(QuotePrice quotePrice) {
		this.quotePrice = quotePrice;
	}

	/**
	 * @return the fromEffectiveUsagePrice
	 */
	public Double getFromEffectiveUsagePrice() {
		return fromEffectiveUsagePrice;
	}

	/**
	 * @param fromEffectiveUsagePrice the fromEffectiveUsagePrice to set
	 */
	public void setFromEffectiveUsagePrice(Double fromEffectiveUsagePrice) {
		this.fromEffectiveUsagePrice = fromEffectiveUsagePrice;
	}

	/**
	 * @return the toEffectiveUsagePrice
	 */
	public Double getToEffectiveUsagePrice() {
		return toEffectiveUsagePrice;
	}

	/**
	 * @param toEffectiveUsagePrice the toEffectiveUsagePrice to set
	 */
	public void setToEffectiveUsagePrice(Double toEffectiveUsagePrice) {
		this.toEffectiveUsagePrice = toEffectiveUsagePrice;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
}