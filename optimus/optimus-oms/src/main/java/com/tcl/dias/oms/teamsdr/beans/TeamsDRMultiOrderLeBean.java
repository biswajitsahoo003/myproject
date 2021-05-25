package com.tcl.dias.oms.teamsdr.beans;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.gsc.beans.GscMultipleLEOrderSolutionBean;

/**
 * Teams DR Multi Order LE bean
 * 
 * @author Srinivasa Raghavan
 */
public class TeamsDRMultiOrderLeBean {

	private Integer orderLeId;

	private String orderLeCode;

	private Integer currencyId;

	private Integer customerLegalEntityId;

	private String customerlegalEntityName;

	private Integer supplierLegalEntityId;

	private Double finalMrc;

	private Double finalNrc;

	private Double finalArc;

	private Double proposedMrc;

	private Double proposedNrc;

	private Double proposedArc;

	private Double totalTcv;

	private String tpsSfdcParentOptyId;

	private String tpsSfdcCopfId;

	private String stage;

	private String subStage;

	private Set<LegalAttributeBean> legalAttributes;

	private String termInMonths;

	private String currency;

	private String classification;

	private Double creditLimit;

	private Double securityDepositAmount;

	private Byte isMultiCircuit;

	private String orderType;

	private String orderCategory;

	// for demo orders
	private Boolean isDemo;
	private String demoType;

	private String contractPeriod;
	private Boolean isDocusign;
	private Boolean isManualCofSigned;
	private List<GscMultipleLEOrderSolutionBean> voiceOrderSolutions;
	private TeamsDROrderSolutionBean teamsDROrderSolution;

	private Boolean isWholesale = false;

	public TeamsDRMultiOrderLeBean() {
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

	public String getPartnerOptyExpectedCurrency() {
		return partnerOptyExpectedCurrency;
	}

	public void setPartnerOptyExpectedCurrency(String partnerOptyExpectedCurrency) {
		this.partnerOptyExpectedCurrency = partnerOptyExpectedCurrency;
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

	public TeamsDRMultiOrderLeBean(OrderToLe orderToLe) {
		if (orderToLe != null) {
			this.orderLeId = orderToLe.getId();
			this.finalMrc = orderToLe.getFinalMrc();
			this.finalNrc = orderToLe.getFinalNrc();
			this.finalArc = orderToLe.getFinalArc();
			this.proposedMrc = orderToLe.getProposedMrc();
			this.proposedNrc = orderToLe.getProposedNrc();
			this.proposedArc = orderToLe.getProposedArc();
			this.totalTcv = orderToLe.getTotalTcv();
			this.currencyId = orderToLe.getCurrencyId();
			this.customerLegalEntityId = orderToLe.getErfCusCustomerLegalEntityId();
			this.supplierLegalEntityId = orderToLe.getErfCusSpLegalEntityId();
			this.tpsSfdcCopfId = orderToLe.getTpsSfdcCopfId();
			this.stage = orderToLe.getStage();
			this.classification = orderToLe.getClassification();
			this.creditLimit = orderToLe.getTpsSfdcCreditLimit();
			this.securityDepositAmount = orderToLe.getTpsSfdcSecurityDepositAmount();
			this.isMultiCircuit = orderToLe.getIsMultiCircuit();
			this.contractPeriod = orderToLe.getTermInMonths();
			this.currency = orderToLe.getCurrencyCode();
			this.orderCategory = orderToLe.getOrderCategory();
			this.orderType = orderToLe.getOrderType();
			this.orderLeCode = orderToLe.getOrderLeCode();
			this.subStage = orderToLe.getSubStage();
		}
	}

	public static OrderToLe toQuoteToLe(TeamsDRMultiOrderLeBean orderLeBean) {
		OrderToLe orderToLe = new OrderToLe();
		orderToLe.setId(orderLeBean.orderLeId);
		orderToLe.setFinalMrc(orderLeBean.finalMrc);
		orderToLe.setFinalNrc(orderLeBean.finalNrc);
		orderToLe.setFinalArc(orderLeBean.finalArc);
		orderToLe.setProposedMrc(orderLeBean.proposedMrc);
		orderToLe.setProposedNrc(orderLeBean.proposedNrc);
		orderToLe.setProposedArc(orderLeBean.getProposedArc());
		orderToLe.setTotalTcv(orderLeBean.totalTcv);
		orderToLe.setCurrencyId(orderLeBean.currencyId);
		orderToLe.setErfCusCustomerLegalEntityId(orderLeBean.customerLegalEntityId);
		orderToLe.setErfCusSpLegalEntityId(orderLeBean.supplierLegalEntityId);
		orderToLe.setTpsSfdcCopfId(orderLeBean.getTpsSfdcCopfId());
		orderToLe.setStage(orderLeBean.stage);
		orderToLe.setTermInMonths(orderLeBean.getContractPeriod());
		orderToLe.setCurrencyCode(orderLeBean.getCurrency());
		orderToLe.setClassification(orderLeBean.classification);
		orderToLe.setOrderCategory(orderLeBean.orderCategory);
		orderToLe.setOrderType(orderLeBean.orderType);
		orderToLe.setOrderLeCode(orderLeBean.orderLeCode);
		orderToLe.setSubStage(orderLeBean.subStage);
		orderToLe.setIsWholesale(orderLeBean.isWholesale ? CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE);
		return orderToLe;
	}

	TeamsDRMultiOrderLeBean(String a) {

	}

	/**
	 * @return the quoteleId
	 */
	public Integer getOrderLeId() {
		return orderLeId;
	}

	/**
	 * @param orderLeId the quoteleId to set
	 */
	public void setOrderLeId(Integer orderLeId) {
		this.orderLeId = orderLeId;
	}

	/**
	 * @return the currencyId
	 */
	public Integer getCurrencyId() {
		return currencyId;
	}

	/**
	 * @param currencyId the currencyId to set
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
	 * @param customerLegalEntityId the customerLegalEntityId to set
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
	 * @param supplierLegalEntityId the supplierLegalEntityId to set
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
	 * @param finalMrc the finalMrc to set
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
	 * @param finalNrc the finalNrc to set
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
	 * @param proposedMrc the proposedMrc to set
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
	 * @param proposedNrc the proposedNrc to set
	 */
	public void setProposedNrc(Double proposedNrc) {
		this.proposedNrc = proposedNrc;
	}

	public String getTpsSfdcCopfId() {
		return tpsSfdcCopfId;
	}

	public void setTpsSfdcCopfId(String tpsSfdcCopfId) {
		this.tpsSfdcCopfId = tpsSfdcCopfId;
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
	 * @param legalAttributes the legalAttributes to set
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

	public String getSubStage() {
		return subStage;
	}

	public void setSubStage(String subStage) {
		this.subStage = subStage;
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

	public Byte getIsMultiCircuit() {
		return isMultiCircuit;
	}

	public void setIsMultiCircuit(Byte isMultiCircuit) {
		this.isMultiCircuit = isMultiCircuit;
	}

	public String getContractPeriod() {
		return contractPeriod;
	}

	public void setContractPeriod(String contractPeriod) {
		this.contractPeriod = contractPeriod;
	}

	public Boolean getIsDocusign() {
		return isDocusign;
	}

	public void setIsDocusign(Boolean isDocusign) {
		this.isDocusign = isDocusign;
	}

	public Boolean getIsManualCofSigned() {
		return isManualCofSigned;
	}

	public void setIsManualCofSigned(Boolean isManualCofSigned) {
		this.isManualCofSigned = isManualCofSigned;
	}

	public TeamsDROrderSolutionBean getTeamsDROrderSolution() {
		return teamsDROrderSolution;
	}

	public void setTeamsDROrderSolution(TeamsDROrderSolutionBean teamsDROrderSolution) {
		this.teamsDROrderSolution = teamsDROrderSolution;
	}

	public List<GscMultipleLEOrderSolutionBean> getVoiceOrderSolutions() {
		return voiceOrderSolutions;
	}

	public void setVoiceOrderSolutions(List<GscMultipleLEOrderSolutionBean> voiceOrderSolutions) {
		this.voiceOrderSolutions = voiceOrderSolutions;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getTpsSfdcParentOptyId() {
		return tpsSfdcParentOptyId;
	}

	public void setTpsSfdcParentOptyId(String tpsSfdcParentOptyId) {
		this.tpsSfdcParentOptyId = tpsSfdcParentOptyId;
	}

	public String getCustomerlegalEntityName() {
		return customerlegalEntityName;
	}

	public void setCustomerlegalEntityName(String customerlegalEntityName) {
		this.customerlegalEntityName = customerlegalEntityName;
	}

	public String getOrderLeCode() {
		return orderLeCode;
	}

	public void setOrderLeCode(String orderLeCode) {
		this.orderLeCode = orderLeCode;
	}

	@Override
	public String toString() {
		return "TeamsDRMultiQuoteLeBean{" + "quoteleId=" + orderLeId + ", orderLeCode=" + orderLeCode + ", currencyId="
				+ currencyId + ", customerLegalEntityId=" + customerLegalEntityId + ", customerlegalEntityName='"
				+ customerlegalEntityName + '\'' + ", supplierLegalEntityId=" + supplierLegalEntityId + ", finalMrc="
				+ finalMrc + ", finalNrc=" + finalNrc + ", finalArc=" + finalArc + ", proposedMrc=" + proposedMrc
				+ ", proposedNrc=" + proposedNrc + ", proposedArc=" + proposedArc + ", totalTcv=" + totalTcv
				+ ", tpsSfdcParentOptyId='" + tpsSfdcParentOptyId + '\'' + ", tpsSfdcCopfId='" + tpsSfdcCopfId + '\''
				+ ", stage='" + stage + '\'' + ", subStage='" + subStage + '\'' + ", legalAttributes=" + legalAttributes
				+ ", termInMonths='" + termInMonths + '\'' + ", currency='" + currency + '\'' + ", classification='"
				+ classification + '\'' + ", creditLimit=" + creditLimit + ", securityDepositAmount="
				+ securityDepositAmount + ", isMultiCircuit=" + isMultiCircuit + ", orderType='" + orderType + '\''
				+ ", orderCategory='" + orderCategory + '\'' + ", isDemo=" + isDemo + ", demoType='" + demoType + '\''
				+ ", contractPeriod='" + contractPeriod + '\'' + ", isDocusign=" + isDocusign + ", isManualCofSigned="
				+ isManualCofSigned + ", voiceSolutions=" + voiceOrderSolutions + ", teamsDRSolution="
				+ teamsDROrderSolution + ", isWholesale=" + isWholesale + ", partnerOptyExpectedArc="
				+ partnerOptyExpectedArc + ", partnerOptyExpectedNrc=" + partnerOptyExpectedNrc
				+ ", partnerOptyExpectedCurrency='" + partnerOptyExpectedCurrency + '\'' + '}';
	}
}
