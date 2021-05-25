/**
 * 
 */
package com.tcl.dias.nso.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.nso.beans.LegalAttributeBean;
import com.tcl.dias.nso.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.entity.entities.QuoteToLe;

/**
 * @author KarMani
 *
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
	
	private String termInMonths;
	
	private String currency;

	private String classification;
	
	private Double creditLimit;
	
	private Double securityDepositAmount;

	public Double getPartnerOptyExpectedArc() {
		return partnerOptyExpectedArc;
	}

	public void setPartnerOptyExpectedArc(Double partnerOptyExpectedArc) {
		this.partnerOptyExpectedArc = partnerOptyExpectedArc;
	}

	public Double getPartnerOptyExpectedNrc() {
		return partnerOptyExpectedNrc;
	}

	public void setPartnerOptyExpectedNrc(Double partnerOptyExpectedNrc) {
		this.partnerOptyExpectedNrc = partnerOptyExpectedNrc;
	}

	public String getPartnerOptyExpectedCurrency() {
		return partnerOptyExpectedCurrency;
	}

	public void setPartnerOptyExpectedCurrency(String partnerOptyExpectedCurrency) {
		this.partnerOptyExpectedCurrency = partnerOptyExpectedCurrency;
	}

	private Double partnerOptyExpectedArc;

	private Double partnerOptyExpectedNrc;

	private String partnerOptyExpectedCurrency;

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
			this.classification = quoteToLe.getClassification();
			this.creditLimit = quoteToLe.getTpsSfdcCreditLimit();
			this.securityDepositAmount = quoteToLe.getTpsSfdcSecurityDepositAmount();
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

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	/**
	 * @return the creditLimit
	 */
	public Double getCreditLimit() {
		return creditLimit;
	}

	/**
	 * @param creditLimit the creditLimit to set
	 */
	public void setCreditLimit(Double creditLimit) {
		this.creditLimit = creditLimit;
	}

	/**
	 * @return the securityDepositAmount
	 */
	public Double getSecurityDepositAmount() {
		return securityDepositAmount;
	}

	/**
	 * @param securityDepositAmount the securityDepositAmount to set
	 */
	public void setSecurityDepositAmount(Double securityDepositAmount) {
		this.securityDepositAmount = securityDepositAmount;
	}

	@Override
	public String toString() {
		return "QuoteToLeBean{" +
				"quoteleId=" + quoteleId +
				", currencyId=" + currencyId +
				", customerLegalEntityId=" + customerLegalEntityId +
				", supplierLegalEntityId=" + supplierLegalEntityId +
				", finalMrc=" + finalMrc +
				", finalNrc=" + finalNrc +
				", finalArc=" + finalArc +
				", proposedMrc=" + proposedMrc +
				", proposedNrc=" + proposedNrc +
				", proposedArc=" + proposedArc +
				", totalTcv=" + totalTcv +
				", tpsSfdcOptyId='" + tpsSfdcOptyId + '\'' +
				", stage='" + stage + '\'' +
				", productFamilies=" + productFamilies +
				", legalAttributes=" + legalAttributes +
				", termInMonths='" + termInMonths + '\'' +
				", currency='" + currency + '\'' +
				", classification='" + classification + '\'' +
				", creditLimit='" + creditLimit + '\'' +
				", securityDepositAmount='" + securityDepositAmount + '\'' +
				'}';
	}
}

