package com.tcl.dias.nso.dto;

import java.io.Serializable;

import com.tcl.dias.oms.entity.entities.QuotePrice;

/**
 * This file contains the QuotePriceDto.java class.
 * 
 * Dto Class
 * 
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class QuotePriceDto implements Serializable {

	private static final long serialVersionUID = -3947718173027517385L;

	private Integer id;

	private Double catalogMrc;

	private Double catalogNrc;

	private Double computedMrc;

	private Double computedNrc;

	private Double discountInPercent;

	private Double effectiveMrc;

	private Double effectiveNrc;

	private Double minimumMrc;

	private Double minimumNrc;

	private String productFamilyName;

	private Integer quoteId;

	private String referenceId;

	private String referenceName;

	/**
	 * @param id
	 * @param catalogMrc
	 * @param catalogNrc
	 * @param computedMrc
	 * @param computedNrc
	 * @param discountInPercent
	 * @param effectiveMrc
	 * @param effectiveNrc
	 * @param minimumMrc
	 * @param minimumNrc
	 * @param productFamilyName
	 * @param quoteId
	 * @param referenceId
	 * @param referenceName
	 */
	public QuotePriceDto(QuotePrice price) {
		if (price != null) {
			this.id = price.getId();
			this.catalogMrc = price.getCatalogMrc();
			this.catalogNrc = price.getCatalogNrc();
			this.computedMrc = price.getComputedMrc();
			this.computedNrc = price.getCatalogNrc();
			this.discountInPercent = price.getDiscountInPercent();
			this.effectiveMrc = price.getEffectiveMrc();
			this.effectiveNrc = price.getCatalogNrc();
			this.minimumMrc = price.getMinimumMrc();
			this.minimumNrc = price.getMinimumNrc();
			this.quoteId = price.getQuoteId();
			this.referenceId = price.getReferenceId();
			this.referenceName = price.getReferenceName();
		}
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

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "QuotePriceDto [id=" + id + ", catalogMrc=" + catalogMrc + ", catalogNrc=" + catalogNrc
				+ ", computedMrc=" + computedMrc + ", computedNrc=" + computedNrc + ", discountInPercent="
				+ discountInPercent + ", effectiveMrc=" + effectiveMrc + ", effectiveNrc=" + effectiveNrc
				+ ", minimumMrc=" + minimumMrc + ", minimumNrc=" + minimumNrc + ", productFamilyName="
				+ productFamilyName + ", quoteId=" + quoteId + ", referenceId=" + referenceId + ", referenceName="
				+ referenceName + "]";
	}

}
