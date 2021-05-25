package com.tcl.dias.oms.gsc.beans;

import java.util.ArrayList;
import java.util.List;

import com.tcl.dias.oms.entity.entities.OrderToLe;

/**
 * Order to le bean for GSC multi LE
 * 
 * @author Srinivasa Raghavan
 *
 */
public class GscMultiOrderLeBean {

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

	private String tpsSfdcCopfId;

	private String stage;

	private String subStage;

	private String paymentCurrency;

	private String termsInMonths;

	private String orderType;

	private Integer erfServiceInventoryParentOrderId;

	private String sourceSystem;

	private String orderCategory;

	private List<String> erfServiceInventoryTpsServiceId = new ArrayList<>();

	private Boolean isDocusign;
	private Boolean isManualCofSigned = false;
	// Only for MACD case
	private Integer supplierLegalId;
	private String classification;
	private String isGscMultiMacd;
	private Boolean isWholesale = false;

	private Double partnerOptyExpectedArc;
	private Double partnerOptyExpectedNrc;
	private String partnerOptyExpectedCurrency;

	private List<GscMultipleLEOrderSolutionBean> voiceSolutions;

	public GscMultiOrderLeBean() {
		// Default constructor
	}

	public GscMultiOrderLeBean(OrderToLe orderToLe) {
		this.orderLeId = orderToLe.getId();
		this.currencyId = orderToLe.getCurrencyId();
		this.customerLegalEntityId = orderToLe.getErfCusCustomerLegalEntityId();
		this.supplierLegalEntityId = orderToLe.getErfCusSpLegalEntityId();
		this.finalMrc = orderToLe.getFinalMrc();
		this.finalNrc = orderToLe.getFinalNrc();
		this.finalArc = orderToLe.getFinalArc();
		this.proposedMrc = orderToLe.getProposedMrc();
		this.proposedNrc = orderToLe.getProposedNrc();
		this.proposedArc = orderToLe.getProposedArc();
		this.totalTcv = orderToLe.getTotalTcv();
		this.tpsSfdcCopfId = orderToLe.getTpsSfdcCopfId();
		this.stage = orderToLe.getStage();
		this.paymentCurrency = orderToLe.getCurrencyCode();
		this.termsInMonths = orderToLe.getTermInMonths();
		this.erfServiceInventoryParentOrderId = orderToLe.getErfServiceInventoryParentOrderId();
		this.orderType = orderToLe.getOrderType();
		this.sourceSystem = orderToLe.getSourceSystem();
		this.orderCategory = orderToLe.getOrderCategory();
		this.erfServiceInventoryParentOrderId = orderToLe.getErfServiceInventoryParentOrderId();
		this.orderLeCode = orderToLe.getOrderLeCode();
		this.subStage = orderToLe.getSubStage();
	}

	public Integer getOrderLeId() {
		return orderLeId;
	}

	public void setOrderLeId(Integer orderLeId) {
		this.orderLeId = orderLeId;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getCustomerLegalEntityId() {
		return customerLegalEntityId;
	}

	public void setCustomerLegalEntityId(Integer customerLegalEntityId) {
		this.customerLegalEntityId = customerLegalEntityId;
	}

	public Integer getSupplierLegalEntityId() {
		return supplierLegalEntityId;
	}

	public void setSupplierLegalEntityId(Integer supplierLegalEntityId) {
		this.supplierLegalEntityId = supplierLegalEntityId;
	}

	public Double getFinalMrc() {
		return finalMrc;
	}

	public void setFinalMrc(Double finalMrc) {
		this.finalMrc = finalMrc;
	}

	public Double getFinalNrc() {
		return finalNrc;
	}

	public void setFinalNrc(Double finalNrc) {
		this.finalNrc = finalNrc;
	}

	public Double getFinalArc() {
		return finalArc;
	}

	public void setFinalArc(Double finalArc) {
		this.finalArc = finalArc;
	}

	public Double getProposedMrc() {
		return proposedMrc;
	}

	public void setProposedMrc(Double proposedMrc) {
		this.proposedMrc = proposedMrc;
	}

	public Double getProposedNrc() {
		return proposedNrc;
	}

	public void setProposedNrc(Double proposedNrc) {
		this.proposedNrc = proposedNrc;
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

	public String getTpsSfdcCopfId() {
		return tpsSfdcCopfId;
	}

	public void setTpsSfdcCopfId(String tpsSfdcCopfId) {
		this.tpsSfdcCopfId = tpsSfdcCopfId;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getPaymentCurrency() {
		return paymentCurrency;
	}

	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}

	public String getTermsInMonths() {
		return termsInMonths;
	}

	public void setTermsInMonths(String termsInMonths) {
		this.termsInMonths = termsInMonths;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Integer getErfServiceInventoryParentOrderId() {
		return erfServiceInventoryParentOrderId;
	}

	public void setErfServiceInventoryParentOrderId(Integer erfServiceInventoryParentOrderId) {
		this.erfServiceInventoryParentOrderId = erfServiceInventoryParentOrderId;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public List<String> getErfServiceInventoryTpsServiceId() {
		return erfServiceInventoryTpsServiceId;
	}

	public void setErfServiceInventoryTpsServiceId(List<String> erfServiceInventoryTpsServiceId) {
		this.erfServiceInventoryTpsServiceId = erfServiceInventoryTpsServiceId;
	}

	public Boolean getDocusign() {
		return isDocusign;
	}

	public void setDocusign(Boolean docusign) {
		isDocusign = docusign;
	}

	public Boolean getManualCofSigned() {
		return isManualCofSigned;
	}

	public void setManualCofSigned(Boolean manualCofSigned) {
		isManualCofSigned = manualCofSigned;
	}

	public Integer getSupplierLegalId() {
		return supplierLegalId;
	}

	public void setSupplierLegalId(Integer supplierLegalId) {
		this.supplierLegalId = supplierLegalId;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getIsGscMultiMacd() {
		return isGscMultiMacd;
	}

	public void setIsGscMultiMacd(String isGscMultiMacd) {
		this.isGscMultiMacd = isGscMultiMacd;
	}

	public Boolean getWholesale() {
		return isWholesale;
	}

	public void setWholesale(Boolean wholesale) {
		isWholesale = wholesale;
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

	public List<GscMultipleLEOrderSolutionBean> getVoiceSolutions() {
		return voiceSolutions;
	}

	public void setVoiceSolutions(List<GscMultipleLEOrderSolutionBean> voiceSolutions) {
		this.voiceSolutions = voiceSolutions;
	}

	public String getOrderLeCode() {
		return orderLeCode;
	}

	public void setOrderLeCode(String orderLeCode) {
		this.orderLeCode = orderLeCode;
	}

	public String getSubStage() {
		return subStage;
	}

	public void setSubStage(String subStage) {
		this.subStage = subStage;
	}

	public String getCustomerlegalEntityName() {
		return customerlegalEntityName;
	}

	public void setCustomerlegalEntityName(String customerlegalEntityName) {
		this.customerlegalEntityName = customerlegalEntityName;
	}

	@Override
	public String toString() {
		return "GscMultiOrderLeBean{" +
				"orderLeId=" + orderLeId +
				", orderLeCode='" + orderLeCode + '\'' +
				", currencyId=" + currencyId +
				", customerLegalEntityId=" + customerLegalEntityId +
				", customerlegalEntityName='" + customerlegalEntityName + '\'' +
				", supplierLegalEntityId=" + supplierLegalEntityId +
				", finalMrc=" + finalMrc +
				", finalNrc=" + finalNrc +
				", finalArc=" + finalArc +
				", proposedMrc=" + proposedMrc +
				", proposedNrc=" + proposedNrc +
				", proposedArc=" + proposedArc +
				", totalTcv=" + totalTcv +
				", tpsSfdcCopfId='" + tpsSfdcCopfId + '\'' +
				", stage='" + stage + '\'' +
				", subStage='" + subStage + '\'' +
				", paymentCurrency='" + paymentCurrency + '\'' +
				", termsInMonths='" + termsInMonths + '\'' +
				", orderType='" + orderType + '\'' +
				", erfServiceInventoryParentOrderId=" + erfServiceInventoryParentOrderId +
				", sourceSystem='" + sourceSystem + '\'' +
				", orderCategory='" + orderCategory + '\'' +
				", erfServiceInventoryTpsServiceId=" + erfServiceInventoryTpsServiceId +
				", isDocusign=" + isDocusign +
				", isManualCofSigned=" + isManualCofSigned +
				", supplierLegalId=" + supplierLegalId +
				", classification='" + classification + '\'' +
				", isGscMultiMacd='" + isGscMultiMacd + '\'' +
				", isWholesale=" + isWholesale +
				", partnerOptyExpectedArc=" + partnerOptyExpectedArc +
				", partnerOptyExpectedNrc=" + partnerOptyExpectedNrc +
				", partnerOptyExpectedCurrency='" + partnerOptyExpectedCurrency + '\'' +
				", voiceSolutions=" + voiceSolutions +
				'}';
	}
}
