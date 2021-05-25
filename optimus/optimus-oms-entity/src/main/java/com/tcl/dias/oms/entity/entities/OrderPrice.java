package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;

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
@Table(name = "order_price")
@NamedQuery(name = "OrderPrice.findAll", query = "SELECT o FROM OrderPrice o")
public class OrderPrice implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "catalog_mrc")
	private Double catalogMrc;

	@Column(name = "catalog_nrc")
	private Double catalogNrc;

	@Column(name = "catalog_arc")
	private Double catalogArc;

	@Column(name = "computed_mrc")
	private Double computedMrc;

	@Column(name = "computed_nrc")
	private Double computedNrc;

	@Column(name = "computed_arc")
	private Double computedArc;

	@Column(name = "discount_in_percent")
	private Double discountInPercent;

	@Column(name = "effective_mrc")
	private Double effectiveMrc;

	@Column(name = "effective_nrc")
	private Double effectiveNrc;

	@Column(name = "effective_arc")
	private Double effectiveArc;

	@Column(name = "effective_usage_price")
	private Double effectiveUsagePrice;

	@Column(name = "minimum_mrc")
	private Double minimumMrc;

	@Column(name = "minimum_nrc")
	private Double minimumNrc;

	@Column(name = "minimum_arc")
	private Double minimumArc;

	@Column(name = "quote_id")
	private Integer quoteId;

	@Column(name = "reference_id")
	private String referenceId;

	@Column(name = "reference_name")
	private String referenceName;

	private Integer version;

	// bi-directional many-to-one association to MstProductFamily
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_family_id")
	private MstProductFamily mstProductFamily;

	public OrderPrice() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getCatalogMrc() {
		return this.catalogMrc;
	}

	public void setCatalogMrc(Double catalogMrc) {
		this.catalogMrc = catalogMrc;
	}

	public Double getCatalogNrc() {
		return this.catalogNrc;
	}

	public void setCatalogNrc(Double catalogNrc) {
		this.catalogNrc = catalogNrc;
	}

	public Double getComputedMrc() {
		return this.computedMrc;
	}

	public void setComputedMrc(Double computedMrc) {
		this.computedMrc = computedMrc;
	}

	public Double getComputedNrc() {
		return this.computedNrc;
	}

	public void setComputedNrc(Double computedNrc) {
		this.computedNrc = computedNrc;
	}

	public Double getDiscountInPercent() {
		return this.discountInPercent;
	}

	public void setDiscountInPercent(Double discountInPercent) {
		this.discountInPercent = discountInPercent;
	}

	public Double getEffectiveMrc() {
		return this.effectiveMrc;
	}

	public void setEffectiveMrc(Double effectiveMrc) {
		this.effectiveMrc = effectiveMrc;
	}

	public Double getEffectiveNrc() {
		return this.effectiveNrc;
	}

	public void setEffectiveNrc(Double effectiveNrc) {
		this.effectiveNrc = effectiveNrc;
	}

	public Double getMinimumMrc() {
		return this.minimumMrc;
	}

	public void setMinimumMrc(Double minimumMrc) {
		this.minimumMrc = minimumMrc;
	}

	public Double getMinimumNrc() {
		return this.minimumNrc;
	}

	public void setMinimumNrc(Double minimumNrc) {
		this.minimumNrc = minimumNrc;
	}

	public Integer getQuoteId() {
		return this.quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public String getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceName() {
		return this.referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public MstProductFamily getMstProductFamily() {
		return this.mstProductFamily;
	}

	public void setMstProductFamily(MstProductFamily mstProductFamily) {
		this.mstProductFamily = mstProductFamily;
	}

	public Double getCatalogArc() {
		return catalogArc;
	}

	public void setCatalogArc(Double catalogArc) {
		this.catalogArc = catalogArc;
	}

	public Double getComputedArc() {
		return computedArc;
	}

	public void setComputedArc(Double computedArc) {
		this.computedArc = computedArc;
	}

	public Double getEffectiveArc() {
		return effectiveArc;
	}

	public void setEffectiveArc(Double effectiveArc) {
		this.effectiveArc = effectiveArc;
	}

	public Double getMinimumArc() {
		return minimumArc;
	}

	public void setMinimumArc(Double minimumArc) {
		this.minimumArc = minimumArc;
	}

	public Double getEffectiveUsagePrice() {
		return effectiveUsagePrice;
	}

	public void setEffectiveUsagePrice(Double effectiveUsagePrice) {
		this.effectiveUsagePrice = effectiveUsagePrice;
	}

}