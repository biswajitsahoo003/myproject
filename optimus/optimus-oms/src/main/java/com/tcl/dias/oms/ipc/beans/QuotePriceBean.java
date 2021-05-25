package com.tcl.dias.oms.ipc.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.QuotePrice;

/**
 * Dtp class for price specific to components and attributes
 * 
 * 
 * 
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotePriceBean implements Serializable {

	private static final long serialVersionUID = 1248430969347064870L;

	private Integer id;

	private Double catalogMrc;

	private Double catalogNrc;

	private Double catalogArc;

	private Double computedMrc;

	private Double computedNrc;

	private Double computedArc;

	private Double discountInPercent;

	private Double effectiveMrc;

	private Double effectiveNrc;

	private Double effectiveArc;

	private Double effectiveUsagePrice;

	private Double minimumMrc;

	private Double minimumNrc;

	private Double minimumArc;

	private String productFamilyName;

	private Integer quoteId;

	private String referenceId;

	private String referenceName;

	/**
	 * 
	 * @param price
	 */
	public QuotePriceBean(QuotePrice price) {
		if (price != null) {
			this.id = price.getId();
			this.catalogMrc = price.getCatalogMrc();
			this.catalogNrc = price.getCatalogNrc();
			this.catalogArc = price.getCatalogArc();
			this.computedMrc = price.getComputedMrc();
			this.computedNrc = price.getComputedNrc();
			this.computedArc = price.getComputedArc();
			this.discountInPercent = price.getDiscountInPercent();
			this.effectiveMrc = price.getEffectiveMrc();
			this.effectiveNrc = price.getEffectiveNrc();
			this.effectiveArc = price.getEffectiveArc();
			this.effectiveUsagePrice = price.getEffectiveUsagePrice();
			this.minimumMrc = price.getMinimumMrc();
			this.minimumNrc = price.getMinimumNrc();
			this.minimumArc = price.getMinimumArc();
			this.quoteId = price.getQuoteId();
			this.referenceId = price.getReferenceId();
			this.referenceName = price.getReferenceName();
		}
	}

	/**
	 * 
	 * @param price
	 */
	public QuotePriceBean(OrderPrice price) {
		if (price != null) {
			this.id = price.getId();
			this.catalogMrc = price.getCatalogMrc();
			this.catalogNrc = price.getCatalogNrc();
			this.catalogArc = price.getCatalogArc();
			this.computedMrc = price.getComputedMrc();
			this.computedNrc = price.getComputedNrc();
			this.computedArc = price.getComputedArc();
			this.discountInPercent = price.getDiscountInPercent();
			this.effectiveMrc = price.getEffectiveMrc();
			this.effectiveNrc = price.getEffectiveNrc();
			this.effectiveArc = price.getEffectiveArc();
			this.effectiveUsagePrice = price.getEffectiveUsagePrice();
			this.minimumMrc = price.getMinimumMrc();
			this.minimumNrc = price.getMinimumNrc();
			this.minimumArc = price.getMinimumArc();
			this.quoteId = price.getQuoteId();
			this.referenceId = price.getReferenceId();
			this.referenceName = price.getReferenceName();
		}
	}

	public QuotePriceBean() {
		// DO NOTHING
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the catalogMrc
	 */
	public Double getCatalogMrc() {
		return catalogMrc;
	}

	/**
	 * @param catalogMrc
	 *            the catalogMrc to set
	 */
	public void setCatalogMrc(Double catalogMrc) {
		this.catalogMrc = catalogMrc;
	}

	/**
	 * @return the catalogNrc
	 */
	public Double getCatalogNrc() {
		return catalogNrc;
	}

	/**
	 * @param catalogNrc
	 *            the catalogNrc to set
	 */
	public void setCatalogNrc(Double catalogNrc) {
		this.catalogNrc = catalogNrc;
	}

	/**
	 * @return the computedMrc
	 */
	public Double getComputedMrc() {
		return computedMrc;
	}

	/**
	 * @param computedMrc
	 *            the computedMrc to set
	 */
	public void setComputedMrc(Double computedMrc) {
		this.computedMrc = computedMrc;
	}

	/**
	 * @return the computedNrc
	 */
	public Double getComputedNrc() {
		return computedNrc;
	}

	/**
	 * @param computedNrc
	 *            the computedNrc to set
	 */
	public void setComputedNrc(Double computedNrc) {
		this.computedNrc = computedNrc;
	}

	/**
	 * @return the discountInPercent
	 */
	public Double getDiscountInPercent() {
		return discountInPercent;
	}

	/**
	 * @param discountInPercent
	 *            the discountInPercent to set
	 */
	public void setDiscountInPercent(Double discountInPercent) {
		this.discountInPercent = discountInPercent;
	}

	/**
	 * @return the effectiveMrc
	 */
	public Double getEffectiveMrc() {
		return effectiveMrc;
	}

	/**
	 * @param effectiveMrc
	 *            the effectiveMrc to set
	 */
	public void setEffectiveMrc(Double effectiveMrc) {
		this.effectiveMrc = effectiveMrc;
	}

	/**
	 * @return the effectiveNrc
	 */
	public Double getEffectiveNrc() {
		return effectiveNrc;
	}

	/**
	 * @param effectiveNrc
	 *            the effectiveNrc to set
	 */
	public void setEffectiveNrc(Double effectiveNrc) {
		this.effectiveNrc = effectiveNrc;
	}

	/**
	 * @return the minimumMrc
	 */
	public Double getMinimumMrc() {
		return minimumMrc;
	}

	/**
	 * @param minimumMrc
	 *            the minimumMrc to set
	 */
	public void setMinimumMrc(Double minimumMrc) {
		this.minimumMrc = minimumMrc;
	}

	/**
	 * @return the minimumNrc
	 */
	public Double getMinimumNrc() {
		return minimumNrc;
	}

	/**
	 * @param minimumNrc
	 *            the minimumNrc to set
	 */
	public void setMinimumNrc(Double minimumNrc) {
		this.minimumNrc = minimumNrc;
	}

	/**
	 * @return the productFamilyName
	 */
	public String getProductFamilyName() {
		return productFamilyName;
	}

	/**
	 * @param productFamilyName
	 *            the productFamilyName to set
	 */
	public void setProductFamilyName(String productFamilyName) {
		this.productFamilyName = productFamilyName;
	}

	/**
	 * @return the quoteId
	 */
	public Integer getQuoteId() {
		return quoteId;
	}

	/**
	 * @param quoteId
	 *            the quoteId to set
	 */
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	/**
	 * @return the referenceId
	 */
	public String getReferenceId() {
		return referenceId;
	}

	/**
	 * @param referenceId
	 *            the referenceId to set
	 */
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	/**
	 * @return the referenceName
	 */
	public String getReferenceName() {
		return referenceName;
	}

	/**
	 * @param referenceName
	 *            the referenceName to set
	 */
	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
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

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "QuotePriceBean [id=" + id + ", catalogMrc=" + catalogMrc + ", catalogNrc=" + catalogNrc
				+ ", computedMrc=" + computedMrc + ", computedNrc=" + computedNrc + ", discountInPercent="
				+ discountInPercent + ", effectiveMrc=" + effectiveMrc + ", effectiveNrc=" + effectiveNrc
				+ ", minimumMrc=" + minimumMrc + ", minimumNrc=" + minimumNrc + ", productFamilyName="
				+ productFamilyName + ", quoteId=" + quoteId + ", referenceId=" + referenceId + ", referenceName="
				+ referenceName + "]";
	}

}
