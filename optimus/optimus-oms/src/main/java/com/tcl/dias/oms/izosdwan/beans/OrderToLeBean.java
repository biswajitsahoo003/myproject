package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.entity.entities.OrderToLe;

/**
 * Bean class to hold Izosdwan order to le data
 * @author Madhumiethaa Palanisamy
 *
 */

@JsonInclude(Include.NON_NULL)
public class OrderToLeBean implements Serializable {

	private static final long serialVersionUID = -1699263772250041260L;

	private Integer id;

	private Integer currencyId;

	private Date endDate;

	private Integer erfCusCustomerLegalEntityId;

	private Integer erfCusSpLegalEntityId;

	private Double finalMrc;

	private Double finalNrc;

	private Double finalArc;

	private Double totalTcv;

	private Integer orderVersion;

	private Double proposedMrc;

	private Double proposedNrc;

	private Double proposedArc;

	private String stage;

	private Date startDate;

	private String tpsSfdcCopfId;

	private Set<OrderToLeProductFamilyBean> orderToLeProductFamilyBeans;

	private Set<LegalAttributeBean> legalAttributes;
	
	private String termInMonths;
	
	private String currency;
	
	private String orderType;
	
	private String orderCategory;
	
	private Date cancellationDate;
	
	private String orderCodes;

	private String classification;

	private Double partnerOptyExpectedArc;

	private Double partnerOptyExpectedNrc;

	private String partnerOptyExpectedCurrency;

	public String getPartnerOptyExpectedCurrency() {
		return partnerOptyExpectedCurrency;
	}

	public void setPartnerOptyExpectedCurrency(String partnerOptyExpectedCurrency) {
		this.partnerOptyExpectedCurrency = partnerOptyExpectedCurrency;
	}


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

	public OrderToLeBean(OrderToLe orderToLe) {
		if (orderToLe != null) {
			this.id = orderToLe.getId();
			this.finalMrc = orderToLe.getFinalMrc();
			this.finalNrc = orderToLe.getFinalNrc();
			this.finalArc = orderToLe.getFinalArc();
			this.proposedMrc = orderToLe.getProposedMrc();
			this.proposedNrc = orderToLe.getProposedNrc();
			this.proposedArc = orderToLe.getProposedArc();
			this.currencyId = orderToLe.getCurrencyId();
			this.erfCusCustomerLegalEntityId = orderToLe.getErfCusCustomerLegalEntityId();
			this.erfCusSpLegalEntityId = orderToLe.getErfCusSpLegalEntityId();
			this.tpsSfdcCopfId = orderToLe.getTpsSfdcCopfId();
			this.totalTcv = orderToLe.getTotalTcv();
			this.classification = orderToLe.getClassification();
			this.orderCodes = orderToLe.getOrder().getOrderCode();
			this.stage = orderToLe.getStage();
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
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the erfCusCustomerLegalEntityId
	 */
	public Integer getErfCusCustomerLegalEntityId() {
		return erfCusCustomerLegalEntityId;
	}

	/**
	 * @param erfCusCustomerLegalEntityId
	 *            the erfCusCustomerLegalEntityId to set
	 */
	public void setErfCusCustomerLegalEntityId(Integer erfCusCustomerLegalEntityId) {
		this.erfCusCustomerLegalEntityId = erfCusCustomerLegalEntityId;
	}

	/**
	 * @return the erfCusSpLegalEntityId
	 */
	public Integer getErfCusSpLegalEntityId() {
		return erfCusSpLegalEntityId;
	}

	/**
	 * @param erfCusSpLegalEntityId
	 *            the erfCusSpLegalEntityId to set
	 */
	public void setErfCusSpLegalEntityId(Integer erfCusSpLegalEntityId) {
		this.erfCusSpLegalEntityId = erfCusSpLegalEntityId;
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
	 * @return the orderVersion
	 */
	public Integer getOrderVersion() {
		return orderVersion;
	}

	/**
	 * @param orderVersion
	 *            the orderVersion to set
	 */
	public void setOrderVersion(Integer orderVersion) {
		this.orderVersion = orderVersion;
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
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}

	/**
	 * @param stage
	 *            the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the tpsSfdcCopfId
	 */
	public String getTpsSfdcCopfId() {
		return tpsSfdcCopfId;
	}

	/**
	 * @param tpsSfdcCopfId
	 *            the tpsSfdcCopfId to set
	 */
	public void setTpsSfdcCopfId(String tpsSfdcCopfId) {
		this.tpsSfdcCopfId = tpsSfdcCopfId;
	}

	/**
	 * @return the orderToLeProductFamilyBeans
	 */
	public Set<OrderToLeProductFamilyBean> getOrderToLeProductFamilyBeans() {
		return orderToLeProductFamilyBeans;
	}

	/**
	 * @param orderToLeProductFamilyBeans
	 *            the orderToLeProductFamilyBeans to set
	 */
	public void setOrderToLeProductFamilyBeans(Set<OrderToLeProductFamilyBean> orderToLeProductFamilyBeans) {
		this.orderToLeProductFamilyBeans = orderToLeProductFamilyBeans;
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

	public Double getTotalTcv() {
		return totalTcv;
	}

	public void setTotalTcv(Double totalTcv) {
		this.totalTcv = totalTcv;
	}

	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the orderCategory
	 */
	public String getOrderCategory() {
		return orderCategory;
	}

	/**
	 * @param orderCategory the orderCategory to set
	 */
	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	/**
	 * @return the cancellationDate
	 */
	public Date getCancellationDate() {
		return cancellationDate;
	}

	/**
	 * @param cancellationDate the cancellationDate to set
	 */
	public void setCancellationDate(Date cancellationDate) {
		this.cancellationDate = cancellationDate;
	}


	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}
	/**
	 * @return the orderCode
	 */
	public String getOrderCodes() {
		return orderCodes;
	}

	/**
	 * @param orderCode the orderCode to set
	 */
	public void setOrderCodes(String orderCodes) {
		this.orderCodes = orderCodes;
	}

}
