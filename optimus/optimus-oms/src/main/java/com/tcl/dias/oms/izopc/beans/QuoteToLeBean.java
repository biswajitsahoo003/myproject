package com.tcl.dias.oms.izopc.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.entity.entities.QuoteToLe;

/**
 * This file contains the QuoteToLeBean.java class for leagl entity details
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class QuoteToLeBean implements Serializable {

	private static final long serialVersionUID = 5819134355416795552L;

	private Integer quoteleId;

	private Integer currencyId;

	private Integer customerLegalEntityId;

	private Integer supplierLegalEntityId;

	private Double finalMrc;

	private Double finalNrc;

	private Double finalArc;

	private Double proposedMrc;

	private Double proposedNrc;

	private Double proposedArc;

	private Double totalTcv;

	private String tpsSfdcOptyId;

	private String stage;

	private Set<QuoteToLeProductFamilyBean> productFamilies;

	private Set<LegalAttributeBean> legalAttributes;
	
	private String currency;
	
	private String termInMonths;
	
	private Boolean isDemo;
	
	private String demoType;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTermInMonths() {
		return termInMonths;
	}

	public void setTermInMonths(String termInMonths) {
		this.termInMonths = termInMonths;
	}

	public QuoteToLeBean(QuoteToLe quoteToLe) {
		if (quoteToLe != null) {
			this.quoteleId = quoteToLe.getId();
			this.finalMrc = quoteToLe.getFinalMrc();
			this.finalNrc = quoteToLe.getFinalNrc();
			this.finalArc = quoteToLe.getFinalArc();
			this.proposedMrc = quoteToLe.getProposedMrc();
			this.proposedNrc = quoteToLe.getProposedNrc();
			this.proposedArc = quoteToLe.getProposedArc();
			this.totalTcv = quoteToLe.getTotalTcv();
			this.currencyId = quoteToLe.getCurrencyId();
			this.customerLegalEntityId = quoteToLe.getErfCusCustomerLegalEntityId();
			this.supplierLegalEntityId = quoteToLe.getErfCusSpLegalEntityId();
			this.tpsSfdcOptyId = quoteToLe.getTpsSfdcOptyId();
			this.stage = quoteToLe.getStage();
		}
	}

	/**
	 * @return the quoteleId
	 */
	public Integer getQuoteleId() {
		return quoteleId;
	}

	/**
	 * @param quoteleId
	 *            the quoteleId to set
	 */
	public void setQuoteleId(Integer quoteleId) {
		this.quoteleId = quoteleId;
	}

	/**
	 * @return the currencyId
	 */
	public Integer getCurrencyId() {
		return currencyId;
	}

	/**
	 * @param currencyId
	 *            the currencyId to set
	 */
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	/**
	 * @return the customerLegalEntityId
	 */
	public Integer getCustomerLegalEntityId() {
		return customerLegalEntityId;
	}

	/**
	 * @param customerLegalEntityId
	 *            the customerLegalEntityId to set
	 */
	public void setCustomerLegalEntityId(Integer customerLegalEntityId) {
		this.customerLegalEntityId = customerLegalEntityId;
	}

	/**
	 * @return the supplierLegalEntityId
	 */
	public Integer getSupplierLegalEntityId() {
		return supplierLegalEntityId;
	}

	/**
	 * @param supplierLegalEntityId
	 *            the supplierLegalEntityId to set
	 */
	public void setSupplierLegalEntityId(Integer supplierLegalEntityId) {
		this.supplierLegalEntityId = supplierLegalEntityId;
	}

	/**
	 * @return the finalMrc
	 */
	public Double getFinalMrc() {
		return finalMrc;
	}

	/**
	 * @param finalMrc
	 *            the finalMrc to set
	 */
	public void setFinalMrc(Double finalMrc) {
		this.finalMrc = finalMrc;
	}

	/**
	 * @return the finalNrc
	 */
	public Double getFinalNrc() {
		return finalNrc;
	}

	/**
	 * @param finalNrc
	 *            the finalNrc to set
	 */
	public void setFinalNrc(Double finalNrc) {
		this.finalNrc = finalNrc;
	}

	/**
	 * @return the proposedMrc
	 */
	public Double getProposedMrc() {
		return proposedMrc;
	}

	/**
	 * @param proposedMrc
	 *            the proposedMrc to set
	 */
	public void setProposedMrc(Double proposedMrc) {
		this.proposedMrc = proposedMrc;
	}

	/**
	 * @return the proposedNrc
	 */
	public Double getProposedNrc() {
		return proposedNrc;
	}

	/**
	 * @param proposedNrc
	 *            the proposedNrc to set
	 */
	public void setProposedNrc(Double proposedNrc) {
		this.proposedNrc = proposedNrc;
	}

	/**
	 * @return the tpsSfdcOptyId
	 */
	public String getTpsSfdcOptyId() {
		return tpsSfdcOptyId;
	}

	/**
	 * @param tpsSfdcOptyId
	 *            the tpsSfdcOptyId to set
	 */
	public void setTpsSfdcOptyId(String tpsSfdcOptyId) {
		this.tpsSfdcOptyId = tpsSfdcOptyId;
	}

	/**
	 * @return the productFamilies
	 */
	public Set<QuoteToLeProductFamilyBean> getProductFamilies() {
		return productFamilies;
	}

	/**
	 * @param productFamilies
	 *            the productFamilies to set
	 */
	public void setProductFamilies(Set<QuoteToLeProductFamilyBean> productFamilies) {
		this.productFamilies = productFamilies;
	}

	public Double getFinalArc() {
		return finalArc;
	}

	public void setFinalArc(Double finalArc) {
		this.finalArc = finalArc;
	}

	public Double getProposedArc() {
		return proposedArc;
	}

	public void setProposedArc(Double proposedArc) {
		this.proposedArc = proposedArc;
	}

	/**
	 * @return the legalAttributes
	 */
	public Set<LegalAttributeBean> getLegalAttributes() {
		if (legalAttributes == null) {
			legalAttributes = new HashSet<>();
		}
		return legalAttributes;
	}

	/**
	 * @param legalAttributes
	 *            the legalAttributes to set
	 */
	public void setLegalAttributes(Set<LegalAttributeBean> legalAttributes) {
		this.legalAttributes = legalAttributes;
	}

	public Double getTotalTcv() {
		return totalTcv;
	}

	public void setTotalTcv(Double totalTcv) {
		this.totalTcv = totalTcv;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}
	

	public Boolean getIsDemo() {
		return isDemo;
	}

	public void setIsDemo(Boolean isDemo) {
		this.isDemo = isDemo;
	}
	

	public String getDemoType() {
		return demoType;
	}

	public void setDemoType(String demoType) {
		this.demoType = demoType;
	}

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "QuoteToLeBean [quoteleId=" + quoteleId + ", currencyId=" + currencyId + ", customerLegalEntityId="
				+ customerLegalEntityId + ", supplierLegalEntityId=" + supplierLegalEntityId + ", finalMrc=" + finalMrc
				+ ", finalNrc=" + finalNrc + ", proposedMrc=" + proposedMrc + ", proposedNrc=" + proposedNrc
				+ ", tpsSfdcOptyId=" + tpsSfdcOptyId + ", productFamilies=" + productFamilies + "]";
	}

}